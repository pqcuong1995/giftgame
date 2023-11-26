package com.example.giftgame.game

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import androidx.fragment.app.Fragment
import com.example.giftgame.R
import com.example.giftgame.data.Column
import com.example.giftgame.data.Item
import com.example.giftgame.databinding.FragmentGamePlayBinding
import com.example.giftgame.gift.Direction
import com.example.giftgame.gift.PositionGiftCallBack
import com.example.giftgame.gift.ZeroGravityAnimation
import com.example.giftgame.user.DragExperimentTouchListener
import com.example.giftgame.user.PositionUserCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


class GamePlayFragment : Fragment() {
    private lateinit var binding: FragmentGamePlayBinding
    private var mPlayer: MediaPlayer? = null
    private var currentXOfUser = 0
    private var currentYOfUser = 0
    private var isStartGame = false
    private var point = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding.user.setOnTouchListener(
            DragExperimentTouchListener(
                0f,
                requireActivity().resources.displayMetrics.widthPixels.toFloat() - convertDpToPixel(
                    40f,
                    requireActivity()
                ),
                object : PositionUserCallback {
                    override fun onChangePosition(imageXPosition: Int, imageYPosition: Int) {
                        currentXOfUser = imageXPosition
                        currentYOfUser = imageYPosition
                        if (!isStartGame) {
                            playAnimation()
                            isStartGame = true
                        }
                    }

                })
        )
    }

    private fun playAnimation() {
        if (!this.isDetached) {
            val handler = Handler()
            startMusic()
            handleRandomGift(Column.FIRST)
            handleRandomGift(Column.SECOND)
            handleRandomGift(Column.THIRD)
            handleRandomGift(Column.FOURTH)
            handler.postDelayed({
                stopMusic()
            }, 1300)
        }
    }

    private fun handleRandomGift(column: Column) {
        val random = Random
        var item: Item? = null
        when (random.nextInt(100)) {
            in 0..25 -> {
                item = Item.BOOM(R.drawable.ic_boom, 5)
            }

            in 26..40 -> {
                item = Item.Gift(R.drawable.ic_gift_0_point, 0)
            }

            in 41..60 -> {
                item = Item.Gift(R.drawable.ic_gift_5_point, 5)
            }

            in 61..70 -> {
                item = Item.Gift(R.drawable.ic_gift_10_point, 10)
            }

            in 71..80 -> {
                item = Item.Gift(R.drawable.ic_gift_15_point, 15)
            }

            in 81..90 -> {
                item = Item.Gift(R.drawable.ic_gift_30_point, 30)
            }

            in 91..100 -> {
                item = Item.FlashTime(R.drawable.ic_flash_time)
            }
        }
        item?.let {
            flyGift(it, column)
        }
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                delay(4000)
            }
            handleRandomGift(column)
        }
    }

    private fun flyGift(item: Item, column: Column) {
        try {
            var isCollideSuccess = false
            val animation = ZeroGravityAnimation()
            animation.setCount(1)
            animation.setScalingFactor(1f)
            animation.setOriginationDirection(Direction.TOP)
            animation.setDestinationDirection(Direction.BOTTOM)
            animation.setImage(item.resource)
            animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
            }
            )
            animation.play(
                requireActivity(),
                binding.container,
                column,
                object : PositionGiftCallBack {
                    override fun onChangePosition(imageXPosition: Int, imageYPosition: Int) {
                        try {
                            val cornerBottomLeftOfGift =
                                imageYPosition + convertDpToPixel(48f, requireActivity())
                            val cornerTopLeftOfUser = currentXOfUser
                            val cornerTopRightOfUser =
                                currentXOfUser + convertDpToPixel(48f, requireActivity())

                            val isDetectCollideY =
                                (cornerBottomLeftOfGift > currentYOfUser) && (cornerBottomLeftOfGift < currentYOfUser + convertDpToPixel(
                                    48f,
                                    requireActivity()
                                ))
                            val isDetectCollideX =
                                (cornerTopLeftOfUser > imageXPosition) && cornerTopLeftOfUser < imageXPosition + convertDpToPixel(
                                    48f,
                                    requireActivity()
                                )
                            val isDetectCollideX1 =
                                (cornerTopRightOfUser > imageXPosition) && cornerTopRightOfUser < imageXPosition + convertDpToPixel(
                                    48f,
                                    requireActivity()
                                )

                            if (isStartGame && !isCollideSuccess) {
                                if (isDetectCollideY && (isDetectCollideX || isDetectCollideX1)) {
                                    animation.hideAnimation()
                                    when (item) {
                                        is Item.Gift -> {
                                            point += item.point
                                        }

                                        is Item.BOOM -> {
                                            point -= item.point
                                        }

                                        is Item.FlashTime -> {

                                        }

                                        is Item.MultiGift -> {

                                        }
                                    }
                                    binding.txtPoint.text = point.toString()
                                    isCollideSuccess = true
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
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