package com.example.petparadise.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petparadise.R
import com.example.petparadise.models.Service

class ServiceAdapter(private val context: Context, private val serviceList: ArrayList<Service>): RecyclerView.Adapter<ServiceAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_service_item,
        parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ServiceAdapter.MyViewHolder, position: Int) {
        val service: Service = serviceList[position]

        holder.name.text = service.name
        holder.address.text = service.address
        holder.time.text = service.time
        holder.telephone.text = service.telNo
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.shopName)
        val address: TextView = itemView.findViewById(R.id.shopAddress)
        val time: TextView = itemView.findViewById(R.id.operatingTime)
        val telephone: TextView = itemView.findViewById(R.id.telNo)
    }
}