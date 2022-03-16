package com.lahsuak.apps.githuboauth.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lahsuak.apps.githuboauth.R
import com.lahsuak.apps.githuboauth.model.Repository
import com.lahsuak.apps.githuboauth.databinding.ItemRepositoryBinding

class RepositoryAdapter :
    ListAdapter<Repository, RepositoryAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(private var binding: ItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repository: Repository) {
            binding.tvRepoName.text = repository.name
            binding.tvLanguage.text = repository.language
            binding.tvVisibility.text = if (repository.private) "Private" else "Public"
            Glide.with(binding.ivRepo).load(repository.owner?.avatarUrl)
                .error(R.drawable.img_github).into(binding.ivRepo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = getItem(position)
        holder.bind(repository)
    }

}