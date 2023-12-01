package com.example.giftgame

import android.app.Application
import com.example.giftgame.di.LocalStorage

class GiftApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        LocalStorage.initial(this)
    }
}