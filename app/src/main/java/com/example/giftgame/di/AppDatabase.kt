package com.example.giftgame.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.giftgame.data.User
import com.example.giftgame.di.dao.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}