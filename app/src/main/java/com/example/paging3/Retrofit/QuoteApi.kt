package com.example.paging3.Retrofit

import com.example.paging3.models.QuoteList
import retrofit2.http.GET
import retrofit2.http.Query


// Access the Api of Quotelist

interface QuoteApi {
    @GET("/quotes")
    suspend fun getQuotes(@Query("page") page: Int): QuoteList
    // return type is Int for pages
}