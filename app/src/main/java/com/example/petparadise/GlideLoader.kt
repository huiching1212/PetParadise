package com.example.petparadise

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}