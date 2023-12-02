package com.example.giftgame.ui.game

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import androidx.fragment.app.Fragment
import com.example.giftgame.MainActivity
import com.example.giftgame.R
import com.example.giftgame.common.gift.Direction
import com.example.giftgame.common.gift.PositionGiftCallBack
import com.example.giftgame.common.gift.ZeroGravityAnimation
import com.example.giftgame.data.Column
import com.example.giftgame.data.Item
import com.example.giftgame.databinding.FragmentGamePlayBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


class GamePlayFragment : Fragment(), SensorEventListener {
    private lateinit var binding: FragmentGamePlayBinding
    private var mPlayer: MediaPlayer? = null
    private var currentXOfUser = 0
    private var currentYOfUser = 0
    private var isStartGame = false
    private var point = 0
    private lateinit var mSensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private var speed = 30f
    private var countDown = 30L
    private var mGravity: FloatArray? = null
    private var mGeomagnetic: FloatArray? = null
    private var timeMin = 4000
    private var timeMax = 12000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGamePlayBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            mHandler = Handler(it.mainLooper)
            this.mSensorManager = it.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        }
        if (!isStartGame) {
            playAnimation()
            isStartGame = true
            object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.timer.text = (millisUntilFinished / 1000).toString()
                    countDown = millisUntilFinished / 1000
                }

                override fun onFinish() {
                    activity?.let {
                        Handler(it.mainLooper).postDelayed({
                            mSensorManager.unregisterListener(this@GamePlayFragment)
                            mHandler?.removeCallbacks(userMove)
                        }, 7000)
                    }
                }
            }.start()
        }
    }

    private fun playAnimation() {
        if (!this.isDetached) {
            handleRandomGift(Column.FIRST)
            handleRandomGift(Column.SECOND)
            handleRandomGift(Column.THIRD)
            handleRandomGift(Column.FOURTH)
        }
    }

    private fun handleRandomGift(column: Column) {
        val random = Random
        var item: Item? = null
        when (random.nextInt(100)) {
            in 0..25 -> {
                item = Item.BOOM(R.drawable.ic_boom, 15)
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
                delay(5000)
            }
            if (countDown != 0L) {
                handleRandomGift(column)
            } else {
                activity?.let {
                    Handler(it.mainLooper).postDelayed({
                        (it as MainActivity).openOpenGiftFragment(point = point)
                    }, 7000)
                }
            }
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
                timeMin = timeMin,
                timeMax = timeMax,
                activity = requireActivity(),
                ottParent = binding.container,
                column = column,
                positionGiftCallBack = object : PositionGiftCallBack {
                    override fun onChangePosition(imageXPosition: Int, imageYPosition: Int) {
                        try {
                            val cornerBottomLeftOfGift =
                                imageYPosition + convertDpToPixel(48f, requireActivity())
                            val cornerTopLeftOfUser = currentXOfUser
                            val cornerTopRightOfUser =
                                currentXOfUser + convertDpToPixel(72f, requireActivity())

                            val isDetectCollideY =
                                (cornerBottomLeftOfGift > currentYOfUser) && (cornerBottomLeftOfGift < currentYOfUser + convertDpToPixel(
                                    72f,
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
                                            if (point <= 0) {
                                                point = 0
                                            }
                                        }

                                        is Item.FlashTime -> {
                                            flashTime()
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

    private fun flashTime() {
        CoroutineScope(Dispatchers.Main).launch {
            if (timeMin == 4000 && timeMax == 12000) {
                timeMin -= 2000
                timeMax -= 2000
                object : CountDownTimer(5000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {
                        timeMin = 4000
                        timeMax = 12000
                    }
                }.start()
            }
        }
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    private fun startMusic() {
        if (mPlayer != null) {
            mPlayer?.stop()
            mPlayer = null
        }
        mPlayer = MediaPlayer.create(requireActivity(), R.raw.mucsic_game)
        mPlayer?.isLooping = true
        mPlayer?.start()
    }

    private fun stopMusic() {
        if (mPlayer != null) {
            mPlayer?.pause()
        }
    }

    override fun onStart() {
        super.onStart()
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }


    override fun onDestroy() {
        super.onDestroy()
        mSensorManager.unregisterListener(this)
        mHandler?.removeCallbacks(userMove)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) mGravity = event.values
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) mGeomagnetic = event.values
        if (mGravity != null && mGeomagnetic != null) {
            val R = FloatArray(9)
            val I = FloatArray(9)
            val success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)
                val azimut = orientation[0]
                val pitch = orientation[1]
                val roll = orientation[2]
                if ((roll < 0 && azimut < 0 && pitch < 0) || (roll < 0 && azimut > 0 && pitch < 0)) {
                    isXCanMinus = true
                    isXCanPlus = false
                } else {
                    isXCanMinus = false
                    isXCanPlus = true
                }

                if (!isRunning) {
                    userMove.run()
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private var mHandler: Handler? = null
    private val mInterval = 15
    private var isXCanPlus = false
    private var isXCanMinus = false
    var isRunning = false
    private var userMove: Runnable = object : Runnable {
        override fun run() {
            isRunning = true
            try {
                onMoveUser()
            } finally {
                mHandler!!.postDelayed(this, mInterval.toLong())
            }
        }
    }

    private fun onMoveUser() {
        var userX = binding.user.x
        val userY = binding.user.y
        if (binding.user.x >= binding.rootView.width - binding.user.width + 10) {
            isXCanPlus = false
        }
        if (binding.user.x <= binding.rootView.x + 10) {
            isXCanMinus = false
        }
        if (isXCanPlus) {
            userX += speed
        }
        if (isXCanMinus) {
            userX -= speed
        }
        currentXOfUser = userX.toInt()
        currentYOfUser = userY.toInt()
        val anime = binding.user.animate()
        anime.x(userX).y(userY)
        anime.duration = 0
        anime.start()
    }
}