package com.piwew.storyapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.piwew.storyapp.data.api.response.ListStoryItem
import com.piwew.storyapp.databinding.ItemRowStoriesBinding

class ListStoriesAdapter :
    ListAdapter<ListStoryItem, ListStoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val stories = getItem(position)
        holder.bind(stories)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(getItem(position)) }
    }

    class MyViewHolder(private val binding: ItemRowStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: ListStoryItem) {
            binding.tvItemName.text = stories.name
            binding.tvItemDescription.text = stories.description
            Glide.with(itemView.context)
                .load(stories.photoUrl)
                .fitCenter()
                .override(Target.SIZE_ORIGINAL)
                .skipMemoryCache(true)
                .into(binding.ivItemPhoto)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return newItem == oldItem
            }
        }
    }
}