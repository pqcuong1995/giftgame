package com.example.giftgame.ui.opengift

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.giftgame.MainActivity
import com.example.giftgame.R
import com.example.giftgame.data.User
import com.example.giftgame.databinding.DialogInputUserInfoBinding
import com.example.giftgame.databinding.FragmentOpenGiftBinding
import com.example.giftgame.di.LocalStorage


class OpenGiftFragment : Fragment() {
    private lateinit var binding: FragmentOpenGiftBinding
    private var point = 0

    companion object {
        const val POINT_KEY = "POINT"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_open_gift, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            point = it.getInt(POINT_KEY)
        }
        binding.txtPoint.text = "My point is $point"
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
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.open_box_blue_2
                                )
                            )
                        }

                        in 100..200 -> {
                            binding.giftBox.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.open_box_green_2
                                )
                            )
                        }

                        in 200..300 -> {
                            binding.giftBox.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.open_box_yellow_2
                                )
                            )
                        }
                    }
                    Handler(it.mainLooper).postDelayed({
                        binding.giftBox.clearAnimation()
                        when (point) {
                            in 0..100 -> {
                                binding.giftBox.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        requireActivity(),
                                        R.drawable.open_box_blue_3
                                    )
                                )
                            }

                            in 100..200 -> {
                                binding.giftBox.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        requireActivity(),
                                        R.drawable.open_box_green_3
                                    )
                                )
                            }

                            in 200..300 -> {
                                binding.giftBox.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        requireActivity(),
                                        R.drawable.open_box_yellow_3
                                    )
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
                            binding.buttonClose.visibility = View.GONE
                            createInputDialog()
                        }, 1000)
                    }, 1000)
                }, 1000)
            }
        }

    }

    private fun getGiftImage(point: Int): Int {
        return R.drawable.ic_voucher
    }

    private fun createInputDialog() {
        activity?.let {
            val dialogBinding: DialogInputUserInfoBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_input_user_info,
                null,
                false
            )
            val dialog = Dialog(it)
            dialog.setContentView(dialogBinding.root)
            dialog.show()
            dialogBinding.submit.setOnClickListener {
                val name = dialogBinding.input.text.toString()
                if (name.isEmpty()) {
                    Toast.makeText(context, "Please input your name!", Toast.LENGTH_SHORT).show()
                } else {
                    binding.giftBox.isClickable = false
                    val user = User(fullName = name, score = point)
                    LocalStorage.getInstance().saveUser(user)
                    (activity as MainActivity).openUserInfoFragment()
                    dialog.hide()
                }
            }
        }
    }
}