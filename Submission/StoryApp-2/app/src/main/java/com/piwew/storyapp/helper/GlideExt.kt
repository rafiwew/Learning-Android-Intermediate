package com.piwew.storyapp.helper

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(urL: String) {
    Glide.with(this.context)
        .load(urL)
        .fitCenter()
        .skipMemoryCache(true)
        .into(this)
}