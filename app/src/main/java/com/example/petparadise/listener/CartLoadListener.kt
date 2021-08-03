package com.example.petparadise.listener

import com.example.petparadise.models.Cart

interface CartLoadListener {
    fun onLoadCartSuccess(cartModelList: List<Cart>)
    fun onLoadCartFailed(message: String?)
}