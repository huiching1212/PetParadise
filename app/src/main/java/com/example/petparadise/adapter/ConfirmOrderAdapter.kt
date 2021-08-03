package com.example.petparadise.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petparadise.R
import com.example.petparadise.models.Cart

class ConfirmOrderAdapter(private val context: Context, private val cartList: ArrayList<Cart>):
    RecyclerView.Adapter<ConfirmOrderAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_order_item,
            parent, false)

        return MyViewHolder(itemView)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageView)
        val name: TextView = itemView.findViewById(R.id.txtName)
        val price: TextView = itemView.findViewById(R.id.txtPrice)
        val quantity: TextView = itemView.findViewById(R.id.txtQuantity)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cart: Cart = cartList[position]
        val price = cart.totalPrice
        val roundedPrice = String.format("%.2f", price)
        val quantity = cart.quantity?.toInt()

        holder.name.text = cart.name
        holder.price.text = java.lang.StringBuilder("RM ").append(roundedPrice)
        holder.quantity.text = quantity.toString()
        Glide.with(context)
            .load(cartList[position].image)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
}