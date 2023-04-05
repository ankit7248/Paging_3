package com.example.paging3.Repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.paging3.Paging.QuotePagingSource
import com.example.paging3.Retrofit.QuoteApi
import javax.inject.Inject

class QuoteRepository @Inject constructor(val quoteApi: QuoteApi) {
    fun getQuotes() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100) , // In One page we give 20 records and Max size is 100 records
        pagingSourceFactory = {QuotePagingSource(quoteApi)}
    ).liveData
}