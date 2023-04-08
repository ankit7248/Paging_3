package com.example.paging3.Paging
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paging3.Db.QuoteDatabase
import com.example.paging3.Retrofit.QuoteApi
import com.example.paging3.models.QuoteRemoteKeys
import com.example.paging3.models.Result



@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMeditor(
    private val quoteApi: QuoteApi,
    private val quoteDatabase: QuoteDatabase
) : RemoteMediator<Int, Result>() {


    val quoteDAO = quoteDatabase.quoteDao()
    val quoteRemoteKeysDAO = quoteDatabase.remoteKeysDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        //Fetch Quotes from Api
        //Save these Quotes + RemoteKeys Data into Db
        // Logic for states - REFRESH , PREPEND , APPEND

        // REFRESH -> If the load the data first time it be in Refresh states
        // APPEND -> when we scrolling in bottom side so Append active
        // PREPEND -> when we scrolling in Top side then Prepend active

        return try{

            val currentPage = when (loadType){

                LoadType.REFRESH -> {

                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1  // it check is the current is not null if it is null then it will return 1 page otherwise it will go next page and minus -1 so that we get current page
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state) // so we check we got entry or not
                    val prevPage = remoteKeys?.prevPage  // is prev page has data then we return the data
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys!= null // if the prev page  is null then we return the endOfPaginationReached
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state) // so we check we got entry or not
                     val nextPage = remoteKeys?.nextPage  // is next page has data then we return the data
                         ?: return MediatorResult.Success(
                             endOfPaginationReached = remoteKeys!= null // if the next page  is null then we return the endOfPaginationReached
                         )
                    nextPage
                }
            }

            //Fetch Quotes from Api
            val response = quoteApi.getQuotes(currentPage)
            // its hits the api and give the response from quotes
            val endPaginationReached = response.totalPages == currentPage
            // its check our current page is equal to endPaginationReached

            val prevPage = if (currentPage == 1) null else currentPage - 1 // if current page is equal to 1 then it is null otherwise it increase the current page by - 1
            val nextPage = if (endPaginationReached) null else currentPage + 1  //  if page is reched to endPaginationReached then it is null otherwise it increase the current page by +1

            //Save these Quotes + RemoteKeys Data into Db
            quoteDatabase.withTransaction {  // In the database we store  the remote key and quote data
                // withTransaction means it complete whole otherwise not
                quoteDAO.addQuotes(response.results)

                val keys = response.results.map { quote ->
                    QuoteRemoteKeys(
                        id = quote._id,        //WE STORE THE ID OF QUOTES IN QuoteRemoteKeys
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                quoteRemoteKeysDAO.addAllRemoteKeys(keys)
            }
            // return the result of endPaginationReached
            MediatorResult.Success(endPaginationReached)
        }catch (e : Exception){
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Result>) : QuoteRemoteKeys?{

        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?._id?.let{ id->
                quoteRemoteKeysDAO.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Result>): QuoteRemoteKeys? {
        return state.pages.firstOrNull{ it.data.isNotEmpty() }?.data?.firstOrNull() // data?.firstOrNull() -> we return the data
            // its check the lastOrNull of pages if pages is not empty then it will check the data is present in pages or not in last page after it will store the data of pages of last pages
            // so that we get the prev key and next page key by using getRemoteKeyForLastItem
            ?.let {
                    quote -> // we got the quote object
                quoteRemoteKeysDAO.getRemoteKeys(id = quote._id)
            }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Result>): QuoteRemoteKeys? {
    return state.pages.lastOrNull{ it.data.isNotEmpty() }?.data?.lastOrNull()
            // its check the lastOrNull of pages if pages is not empty then it will check the data is present in pages or not in last page after it will store the data of pages of last pages
            // so that we get the prev key and next page key by using getRemoteKeyForLastItem
        ?.let {
            quote -> // we got the quote object
            quoteRemoteKeysDAO.getRemoteKeys(id = quote._id)
        }
    }

}
