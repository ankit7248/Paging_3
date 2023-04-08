package com.example.paging3.Db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.paging3.models.Result

@Dao                                
interface QuoteDAO {

    @Query("SELECT * FROM quote") // Access te data from quotes and return the data in Pages form
    fun getQuotes(): PagingSource<Int,Result>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // if there are same primary key so replace it's primary key.
    suspend fun addQuotes(quotes : List<Result>)

    @Query("DELETE FROM quote")
    suspend fun deleteQuotes()
}