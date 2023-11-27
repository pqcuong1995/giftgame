package com.example.giftgame

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.giftgame.game.GamePlayFragment
import com.example.giftgame.game.RulesFragment
import com.example.giftgame.game.SplashFragment
import com.example.giftgame.opengift.OpenGiftFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        openSplash()
    }

    private fun openSplash() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, SplashFragment(), SplashFragment::class.java.name)
            .commit()
    }

    fun openRulesGame() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameContainer, RulesFragment(), RulesFragment::class.java.name)
            .addToBackStack(null)
            .commit()
    }

    fun openGamePlay() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameContainer, GamePlayFragment(), GamePlayFragment::class.java.name)
            .addToBackStack(null)
            .commit()
    }

    fun openOpenGiftFragment(point: Int) {
        val openGiftFragment = OpenGiftFragment()
        val bundle = Bundle()
        bundle.putInt(OpenGiftFragment.POINT_KEY, point)
        openGiftFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.frameContainer, openGiftFragment, openGiftFragment::class.java.name)
            .addToBackStack(null)
            .commit()
    }
}