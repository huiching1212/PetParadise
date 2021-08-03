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
import com.example.petparadise.listener.onQtyListener
import com.example.petparadise.models.Cart
import com.google.firebase.firestore.FirebaseFirestore

class CartAdapter(private val onQtyListener: onQtyListener, private val context: Context, private val cartList: ArrayList<Cart>): RecyclerView.Adapter<CartAdapter.MyCartViewHolder>() {

    lateinit var firestore: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_cart_item,
            parent, false)

        return MyCartViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class MyCartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtName)
        val price: TextView = itemView.findViewById(R.id.txtPrice)
        val image: ImageView = itemView.findViewById(R.id.imageView)
        val quantity: TextView = itemView.findViewById(R.id.txtQuantity)
        //val remarks: EditText = itemView.findViewById(R.id.remarks)
        val addBtn: ImageView = itemView.findViewById(R.id.btnPlus)
        val minusBtn: ImageView = itemView.findViewById(R.id.btnMinus)
        val deleteBtn: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {
        firestore = FirebaseFirestore.getInstance()
        val cart: Cart = cartList[position]
        val product = cart.id
        val price = cart.totalPrice
        val roundedPrice = String.format("%.2f", price)
        val quantity = cart.quantity?.toInt()

        holder.name.text = cart.name
        holder.price.text = java.lang.StringBuilder("RM ").append(roundedPrice)
        holder.quantity.text = quantity.toString()
        /*holder.remarks.setText(cart.remarks)
        holder.remarks.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                firestore.collection("Cart").document(FirestoreClass().getCurrentUserID())
                    .collection("Product").document(cart.id.toString())
                    .update("remarks", p0.toString())
                    .addOnSuccessListener {
                        Log.d("This", "updated remarks")
                    }
            }

        })*/

        holder.addBtn.setOnClickListener {
            onQtyListener.onClickAddQty(cart)
        }
        holder.minusBtn.setOnClickListener {
            onQtyListener.onClickMinusQty(cart)
        }
        holder.deleteBtn.setOnClickListener {
            onQtyListener.onClickDelete(cart)
        }
        Glide.with(context)
            .load(cartList[position].image)
            .into(holder.image)
    }
}