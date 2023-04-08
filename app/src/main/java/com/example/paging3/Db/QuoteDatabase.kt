package com.example.paging3.Db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.paging3.models.QuoteRemoteKeys

@Database(entities = [Result::class, QuoteRemoteKeys::class], version = 1)

abstract class QuoteDatabase: RoomDatabase() {
    // we create the database class
    abstract fun quoteDao() : QuoteDAO
    abstract fun remoteKeysDao() : RemoteKeysDao
}