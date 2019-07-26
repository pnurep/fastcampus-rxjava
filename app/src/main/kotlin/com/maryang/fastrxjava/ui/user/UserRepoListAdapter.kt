package com.maryang.fastrxjava.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.entity.UserRepo


class UserRepoListAdapter : RecyclerView.Adapter<UserRepoListAdapter.UserRepoListViewHolder>() {

    var data: List<UserRepo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRepoListViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_user_repos, parent, false)
        return UserRepoListViewHolder(root)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserRepoListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class UserRepoListViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        private val tvRepoName2 = root.findViewById<TextView>(R.id.tv_repo_name_2)
        private val tvDesc2 = root.findViewById<TextView>(R.id.tv_desc_2)

        fun bind(data: UserRepo) {
            tvRepoName2.text = data.name
            tvDesc2.text = data.description
        }

    }

}