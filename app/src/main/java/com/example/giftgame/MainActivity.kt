package com.example.giftgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.giftgame.game.GamePlayFragment
import com.example.giftgame.game.SplashFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openSplash()
    }

    private fun openSplash() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, SplashFragment())
            .commit()
    }

    fun openGamePlay() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameContainer, GamePlayFragment())
            .addToBackStack(null)
            .commit()
    }
}