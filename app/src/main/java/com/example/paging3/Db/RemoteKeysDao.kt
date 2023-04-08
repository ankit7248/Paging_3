package com.example.paging3.Db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paging3.models.QuoteRemoteKeys

@Dao
interface RemoteKeysDao {

    @Query("SELECT * FROM QuoteRemoteKeys WHERE id = : id") // when we pass the id then it will give the records of data
    suspend fun getRemoteKeys(id: String): QuoteRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE) // if there are same primary key so replace it's primary key.
    suspend fun addAllRemoteKeys(remoteKeys: List<QuoteRemoteKeys>)

    @Query("DELETE FROM QuoteRemoteKeys")
    suspend fun deleteAllRemoteKeys()
}