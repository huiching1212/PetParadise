package com.example.petparadise.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petparadise.R
import com.example.petparadise.adapter.CartAdapter
import com.example.petparadise.firestore.FirestoreClass
import com.example.petparadise.listener.onQtyListener
import com.example.petparadise.models.Cart
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity(), onQtyListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartArrayList: ArrayList<Cart>
    private lateinit var adapter: CartAdapter
    private lateinit var FireStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setupActionBar()

        recyclerView = findViewById(R.id.recycler_cart)
        recycler_cart.layoutManager = LinearLayoutManager(this)

        cartArrayList = arrayListOf()

        adapter = CartAdapter(this, this, cartArrayList)
        recycler_cart.adapter = adapter

        recyclerView.adapter = adapter

        EventChangeListener()
        getCartTotalPrice()

        placeOrder.setOnClickListener {
            FireStore = FirebaseFirestore.getInstance()
            FireStore.collection("Cart").document(FirestoreClass().getCurrentUserID())
                .collection("Product")
                .get()
                .addOnSuccessListener {
                    if(it.isEmpty){
                        Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }else{
                        val intent = Intent(this, ConfirmOrderActivity::class.java)
                        startActivity(intent)
                    }
                }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_cart_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbar_cart_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun EventChangeListener() {
        FireStore = FirebaseFirestore.getInstance()
        FireStore.collection("Cart").document(FirestoreClass().getCurrentUserID()).collection("Product")
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            cartArrayList.add(dc.document.toObject(Cart::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }

    override fun onClickAddQty(cart: Cart) {
        val quantity: Double = cart.quantity!!.toDouble()
        val perPrice: Double = cart.price!!.toDouble()
        val addedQuantity = quantity + 1
        val totalPrice = perPrice * addedQuantity

        //add quantity
        FireStore.collection("Cart").document(FirestoreClass().getCurrentUserID())
            .collection("Product").document(cart.id.toString())
            .update("quantity", addedQuantity)
        //recalculate total price
        FireStore.collection("Cart").document(FirestoreClass().getCurrentUserID())
            .collection("Product").document(cart.id.toString())
            .update("totalPrice", totalPrice)
            .addOnSuccessListener {
                Log.d("This", "update to cart successfully")

            }.addOnFailureListener {
                Log.d("This", it.message.toString())
            }
        //recreate()
        val refresh = Intent(this, CartActivity::class.java)
        finish()
        overridePendingTransition(0, 0)
        startActivity(refresh)
        overridePendingTransition(0, 0)
    }

    override fun onClickMinusQty(cart: Cart) {
        val quantity: Double = cart.quantity!!.toDouble()
        val perPrice: Double = cart.price!!.toDouble()
        val minusQuantity = quantity - 1
        val totalPrice = perPrice * minusQuantity

        //add quantity
        FireStore.collection("Cart").document(FirestoreClass().getCurrentUserID())
            .collection("Product").document(cart.id.toString())
            .update("quantity", minusQuantity)
        //recalculate total price
        FireStore.collection("Cart").document(FirestoreClass().getCurrentUserID())
            .collection("Product").document(cart.id.toString())
            .update("totalPrice", totalPrice)
            .addOnSuccessListener {
                Log.d("This", "update to cart successfully")

            }.addOnFailureListener {
                Log.d("This", it.message.toString())
            }

        if (minusQuantity == 0.0) {
            FireStore.collection("Cart").document(FirestoreClass().getCurrentUserID())
                .collection("Product").document(cart.id.toString())
                .delete()
        }
        val refresh = Intent(this, CartActivity::class.java)
        finish()
        overridePendingTransition(0, 0)
        startActivity(refresh)
        overridePendingTransition(0, 0)
    }

    override fun onClickDelete(cart: Cart) {
        FireStore.collection("Cart").document(FirestoreClass().getCurrentUserID())
            .collection("Product").document(cart.id.toString())
            .delete()
        val refresh = Intent(this, CartActivity::class.java)
        finish()
        overridePendingTransition(0, 0)
        startActivity(refresh)
        overridePendingTransition(0, 0)
    }

    private fun getCartTotalPrice() {
        var totalPrice: Double = 0.0
        FireStore.collection("Cart").document(FirestoreClass().getCurrentUserID())
            .collection("Product")
            .get()
            .addOnSuccessListener {
                for (i in it) {
                    Log.d("price", "${i.getDouble("totalPrice")}")
                    totalPrice += i.getDouble("totalPrice")!!
                }
                val roundedPrice = String.format("%.2f", totalPrice)
                val grandTotal: TextView = findViewById(R.id.grandTotal)
                grandTotal.text = java.lang.StringBuilder("RM ").append(roundedPrice)

                Log.d("This", "get all price successfully")
            }.addOnFailureListener {
                Log.d("This", "get all price unsuccessfully")
            }
    }
}