package com.example.giftgame.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "full_name") val fullName: String?,
    @ColumnInfo(name = "score") val score: Int?
)