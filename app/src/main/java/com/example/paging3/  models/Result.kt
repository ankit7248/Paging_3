package com.example.paging3.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Quote")
data class Result(
    @PrimaryKey(autoGenerate = false) // we are not generating the unique ID by autoGenerate
                                      // because unique key is in Api call
    val _id: String,
    val author: String,
    val authorSlug: String,
    val content: String,
    val dateAdded: String,
    val dateModified: String,
    val length: Int,
)