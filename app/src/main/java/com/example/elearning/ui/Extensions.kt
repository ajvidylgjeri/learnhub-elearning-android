package com.example.elearning.ui

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.elearning.R

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

fun ImageView.loadImage(url: String) {
    Glide.with(this).load(url).placeholder(R.drawable.placeholder).centerCrop().into(this)
}
