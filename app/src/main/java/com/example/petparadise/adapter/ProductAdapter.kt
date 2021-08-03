package com.example.petparadise.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petparadise.R
import com.example.petparadise.models.Product
import com.example.petparadise.ui.activities.CardViewActivity

class ProductAdapter(private val context: Context, private val productList: ArrayList<Product>): RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_product_item,
        parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product: Product = productList[position]
        holder.name.text = product.name
        holder.price.text = java.lang.StringBuilder("RM ").append(product.price)
        Glide.with(context)
            .load(productList[position].image)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CardViewActivity::class.java)
            intent.putExtra("ProductKey", product)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtName)
        val price: TextView = itemView.findViewById(R.id.txtPrice)
        val image: ImageView = itemView.findViewById(R.id.imageView)
    }
}