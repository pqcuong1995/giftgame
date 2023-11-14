package com.example.giftgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.giftgame.game.GamePlayFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, GamePlayFragment())
            .commit()
    }
}