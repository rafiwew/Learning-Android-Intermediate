package com.piwew.storyapp.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.piwew.storyapp.R
import com.piwew.storyapp.data.api.response.ListStoryItem
import com.piwew.storyapp.databinding.ItemRowStoriesBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class ListStoriesAdapter(private val context: Context) :
    ListAdapter<ListStoryItem, ListStoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val stories = getItem(position)
        holder.bind(stories, context)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(getItem(position)) }
    }

    class MyViewHolder(private val binding: ItemRowStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: ListStoryItem, context: Context) {
            binding.tvItemName.text = stories.name
            binding.tvItemDescription.text = stories.description
            val formattedTime = formatRelativeTime(stories.createdAt, context)
            binding.tvItemDate.text = formattedTime
            Glide.with(itemView.context)
                .load(stories.photoUrl)
                .fitCenter()
                .override(Target.SIZE_ORIGINAL)
                .skipMemoryCache(true)
                .into(binding.ivItemPhoto)
        }

        private fun formatRelativeTime(iso8601DateTime: String, context: Context): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val timeZoneUTC = TimeZone.getTimeZone("UTC")
            val timeZoneIndonesia = TimeZone.getTimeZone("Asia/Jakarta")

            inputFormat.timeZone = timeZoneUTC

            return try {
                val dateCreated = inputFormat.parse(iso8601DateTime)

                if (dateCreated != null) {
                    val calendar = Calendar.getInstance()
                    calendar.time = dateCreated
                    calendar.timeZone = timeZoneIndonesia
                    val dateIndonesia = calendar.time

                    val now = Date()

                    val diffInMilliseconds = now.time - dateIndonesia.time
                    val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds)

                    if (diffInMinutes < 1) {
                        return context.getString(R.string.just_now)
                    } else if (diffInMinutes < 60) {
                        return context.getString(R.string.minutes_ago, diffInMinutes)
                    } else {
                        val diffInHours = TimeUnit.MINUTES.toHours(diffInMinutes)
                        if (diffInHours < 24) {
                            return context.getString(R.string.hours_ago, diffInHours)
                        } else {
                            val outputDate = SimpleDateFormat("d MMMM yyyy, HH:mm:ss", Locale.getDefault())
                            return outputDate.format(dateIndonesia)
                        }
                    }
                } else {
                    context.getString(R.string.invalid_date)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                context.getString(R.string.invalid_date)
            }
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