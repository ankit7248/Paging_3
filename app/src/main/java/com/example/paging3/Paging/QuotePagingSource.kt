package com.example.paging3.Paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3.Retrofit.QuoteApi
import com.example.paging3.models.Result

class QuotePagingSource(private val quoteApi: QuoteApi) : PagingSource<Int, Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        // In this function we load the data in paging form
        // and we hit the api in this load
        return try {
            val position = params.key ?: 1 // params give the key of pages
            val response = quoteApi.getQuotes(position)
            return LoadResult.Page(
                data = response.results, // we store the data
                prevKey = if (position == 1) null
                else position - 1,  // store the previous key
                nextKey = if (position == response.totalPages) null
                else position + 1  // store the AFTER key
                // if we are in last page so we  totalPages
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        // anchorPosition store the key of current page
        // closestPageToPosition give the closest page arounding the current page
        // getfreshKey -> give the key page


        // We can use this code also
//        return state.anchorPosition?.let {
//            state.closestPageToPosition(it)?.prevKey?.plus(1)
//                ?:state.closestPageToPosition(it)?.nextKey?.minus(1)
//        }

        if (state.anchorPosition != null) {
            val anchorPage = state.closestPageToPosition(state.anchorPosition!!)
            if (anchorPage?.prevKey != null) {
                return anchorPage.prevKey!!.plus(1)
            } else if (anchorPage?.nextKey != null) {
                return anchorPage.nextKey!!.minus(1)
            }
        } else {
            return null  // if page key is null so it will go to LoadParams and start with 1 key page
        }
        return null
    }

}