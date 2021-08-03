package com.example.petparadise.listener

import com.example.petparadise.models.Cart

interface onQtyListener {
    fun onClickAddQty(cart : Cart)
    fun onClickMinusQty(cart: Cart)
    fun onClickDelete(cart: Cart)
}