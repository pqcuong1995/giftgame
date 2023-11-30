package com.example.giftgame.di

import android.app.Application
import androidx.room.Room
import com.example.giftgame.data.User

class LocalStorage private constructor(application: Application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "gift_db"
    ).allowMainThreadQueries().build()
    private val userDao = db.userDao()
    companion object {
        private var instance: LocalStorage? = null
        fun initial(application: Application){
            if (instance == null) {
                instance = LocalStorage(application)
            }
        }
        fun getInstance(): LocalStorage {
            return instance as LocalStorage
        }
    }

    fun listUser(): List<User> {
        val users: List<User> = userDao.getAll()
        return users
    }

    fun saveUser(user: User) {
        userDao.insertAll(user)
    }
}