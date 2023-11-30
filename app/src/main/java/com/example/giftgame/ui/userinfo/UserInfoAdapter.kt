package com.example.giftgame.ui.userinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.giftgame.data.User
import com.example.giftgame.databinding.ItemUserBinding

class UserInfoAdapter(val inter: IUserInfo) : Adapter<UserInfoAdapter.UserInfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            null,
            false
        )
        return UserInfoViewHolder(binding)
    }

    override fun getItemCount(): Int = inter.count()

    override fun onBindViewHolder(holder: UserInfoViewHolder, position: Int) {
        with(holder) {
            binding.user = inter.data(position)
        }
    }

    class UserInfoViewHolder(val binding: ItemUserBinding) : ViewHolder(binding.root)
    interface IUserInfo {
        fun count(): Int
        fun data(position: Int): User
    }
}