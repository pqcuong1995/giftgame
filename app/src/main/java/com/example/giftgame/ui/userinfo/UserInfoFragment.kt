package com.example.giftgame.ui.userinfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgame.MainActivity
import com.example.giftgame.R
import com.example.giftgame.data.User
import com.example.giftgame.databinding.FragmentUserInfoBinding
import com.example.giftgame.di.LocalStorage

class UserInfoFragment : Fragment(), UserInfoAdapter.IUserInfo {
    private lateinit var binding: FragmentUserInfoBinding
    private var users: List<User> = listOf()
    private lateinit var adapter: UserInfoAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRestart.setOnClickListener {
            activity?.let {
                it.finish()
                val openMainActivity = Intent(it, MainActivity::class.java)
                openMainActivity.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(openMainActivity)
            }
        }
        users = LocalStorage.getInstance().listUser()
        setupRecycleView()
    }

    private fun setupRecycleView() {
        adapter = UserInfoAdapter(this)
        with(binding.rcContent) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@UserInfoFragment.adapter
        }
    }

    override fun count(): Int = users.size

    override fun data(position: Int): User = users[position]
}