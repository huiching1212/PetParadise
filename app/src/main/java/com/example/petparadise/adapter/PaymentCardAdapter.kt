package com.example.petparadise.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petparadise.R
import com.example.petparadise.models.PaymentCard

class PaymentCardAdapter(private val context: Context, private val paymentCardList: ArrayList<PaymentCard>): RecyclerView.Adapter<PaymentCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_payment_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentList = paymentCardList[position]

        holder.cardNumber.text = currentList.noOfCard
    }

    override fun getItemCount(): Int {
        return paymentCardList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cardNumber: TextView = view.findViewById(R.id.cardNumber)
        val details: ImageView = view.findViewById(R.id.arrDetail)
    }
}