package com.example.giftgame.opengift

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.giftgame.MainActivity
import com.example.giftgame.R
import com.example.giftgame.databinding.FragmentOpenGiftBinding


class OpenGiftFragment : Fragment() {
    private lateinit var binding: FragmentOpenGiftBinding

    companion object {
        const val POINT_KEY = "POINT"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_open_gift, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var point = 0
        arguments?.let {
            point = it.getInt(POINT_KEY)
        }
        when (point) {
            in 0..100 -> {
                binding.giftBox.setImageDrawable(
                    ContextCompat.getDrawable(requireActivity(), R.drawable.open_box_blue_1)
                )
            }

            in 100..200 -> {
                binding.giftBox.setImageDrawable(
                    ContextCompat.getDrawable(requireActivity(), R.drawable.open_box_green_1)
                )
            }

            in 200..300 -> {
                binding.giftBox.setImageDrawable(
                    ContextCompat.getDrawable(requireActivity(), R.drawable.open_box_yellow_1)
                )
            }
        }

        binding.giftBox.setOnClickListener {
            context?.let {
                binding.giftBox.startAnimation(AnimationUtils.loadAnimation(it, R.anim.shake))
                Handler(it.mainLooper).postDelayed({
                    binding.giftBox.clearAnimation()
                    when (point) {
                        in 0..100 -> {
                            binding.giftBox.setImageDrawable(
                                ContextCompat.getDrawable(requireActivity(), R.drawable.open_box_blue_2)
                            )
                        }

                        in 100..200 -> {
                            binding.giftBox.setImageDrawable(
                                ContextCompat.getDrawable(requireActivity(), R.drawable.open_box_green_2)
                            )
                        }

                        in 200..300 -> {
                            binding.giftBox.setImageDrawable(
                                ContextCompat.getDrawable(requireActivity(), R.drawable.open_box_yellow_2)
                            )
                        }
                    }
                    Handler(it.mainLooper).postDelayed({
                        binding.giftBox.clearAnimation()
                        when (point) {
                            in 0..100 -> {
                                binding.giftBox.setImageDrawable(
                                    ContextCompat.getDrawable(requireActivity(), R.drawable.open_box_blue_3)
                                )
                            }

                            in 100..200 -> {
                                binding.giftBox.setImageDrawable(
                                    ContextCompat.getDrawable(requireActivity(), R.drawable.open_box_green_3)
                                )
                            }

                            in 200..300 -> {
                                binding.giftBox.setImageDrawable(
                                    ContextCompat.getDrawable(requireActivity(), R.drawable.open_box_yellow_3)
                                )
                            }
                        }
                        Handler(it.mainLooper).postDelayed({
                            binding.giftBox.setImageDrawable(
                                ContextCompat.getDrawable(it, getGiftImage(point = point))
                            )
                            binding.giftBox.startAnimation(
                                AnimationUtils.loadAnimation(
                                    it,
                                    R.anim.scale
                                )
                            )
                            binding.restart.visibility = View.VISIBLE
                            binding.buttonClose.visibility = View.GONE
                        }, 1000)
                    }, 1000)
                }, 1000)
            }
        }
        binding.restart.setOnClickListener {
            activity?.let {
                it.finish()
                val openMainActivity = Intent(it, MainActivity::class.java)
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(openMainActivity)
            }
        }
    }

    private fun getGiftImage(point: Int): Int {
        if (point <= 0) {
            return R.drawable.boxnull
        } else {
            return R.drawable.table
        }
    }
}