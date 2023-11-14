package com.example.giftgame.game

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import androidx.fragment.app.Fragment
import com.example.giftgame.R
import com.example.giftgame.databinding.FragmentGamePlayBinding
import com.example.giftgame.gift.Direction
import com.example.giftgame.gift.PositionGiftCallBack
import com.example.giftgame.gift.ZeroGravityAnimation
import com.example.giftgame.user.DragExperimentTouchListener
import com.example.giftgame.user.PositionUserCallback


class GamePlayFragment : Fragment() {
    private lateinit var binding: FragmentGamePlayBinding
    private var mPlayer: MediaPlayer? = null
    private var currentXOfUser = 0
    private var currentYOfUser = 0
    private var isStartGame = false
    private var point = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGamePlayBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDragUser()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpDragUser() {
        binding.user.setOnTouchListener(DragExperimentTouchListener(0f, requireActivity().resources.displayMetrics.widthPixels.toFloat() - convertDpToPixel(40f, requireActivity()), object : PositionUserCallback {
            override fun onChangePosition(imageXPosition: Int, imageYPosition: Int) {
                currentXOfUser = imageXPosition
                currentYOfUser = imageYPosition
                if (!isStartGame) {
                    playAnimation()
                    isStartGame = true
                }
            }

        }))
        binding.user.animate()
            .translationXBy(100f)
            .start();
    }

    private fun playAnimation() {
        if (!this.isDetached) {
            val handler = Handler()
            startMusic()
            flyGift(R.drawable.ic_gift_box, 1)
            flyGift(R.drawable.ic_gift_box, 2)
            flyGift(R.drawable.ic_gift_box, 3)
            flyGift(R.drawable.ic_gift_box, 4)
            flyGift(R.drawable.ic_gift_box, 5)
            handler.postDelayed({
                stopMusic()
            }, 1300)
        }
    }

    private fun flyGift(resId: Int, number: Int) {
        var isCollideSuccess = false
        val animation = ZeroGravityAnimation()
        animation.setCount(1)
        animation.setScalingFactor(1f)
        animation.setOriginationDirection(Direction.TOP)
        animation.setDestinationDirection(Direction.BOTTOM)
        animation.setImage(resId)
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        }
        )
        animation.play(requireActivity(), binding.container, number, object : PositionGiftCallBack {
            override fun onChangePosition(imageXPosition: Int, imageYPosition: Int) {
                val cornerBottomLeftOfGift = imageYPosition + convertDpToPixel(48f, requireActivity())
                val cornerTopLeftOfUser = currentXOfUser
                val cornerTopRightOfUser = currentXOfUser + convertDpToPixel(48f, requireActivity())

                val isDetectCollideY = (cornerBottomLeftOfGift > currentYOfUser) && (cornerBottomLeftOfGift < currentYOfUser + convertDpToPixel(48f, requireActivity()))
                val isDetectCollideX = (cornerTopLeftOfUser > imageXPosition) && cornerTopLeftOfUser < imageXPosition + convertDpToPixel(48f, requireActivity())
                val isDetectCollideX1 = (cornerTopRightOfUser > imageXPosition) && cornerTopRightOfUser < imageXPosition + convertDpToPixel(48f, requireActivity())

                Log.d("Hung1997", "onChangePosition99 " + currentXOfUser + " " + currentYOfUser + " " + convertDpToPixel(48f, requireActivity()))
                if (isStartGame && !isCollideSuccess) {
                    if (isDetectCollideY && (isDetectCollideX || isDetectCollideX1)) {
                        point += 1
                        binding.txtPoint.text = point.toString()
                        isCollideSuccess = true
                    }
                }
            }
        })
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    private fun startMusic() {
        mPlayer = MediaPlayer.create(requireActivity(), R.raw.mucsic_game)
        mPlayer?.isLooping = true
        mPlayer?.start()
    }

    private fun stopMusic() {
        if (mPlayer != null) {
            mPlayer?.pause()
        }
    }
}