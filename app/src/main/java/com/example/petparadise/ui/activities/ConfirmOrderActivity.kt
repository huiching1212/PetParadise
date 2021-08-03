package com.example.petparadise.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petparadise.R
import com.example.petparadise.adapter.ConfirmOrderAdapter
import com.example.petparadise.firestore.FirestoreClass
import com.example.petparadise.models.Cart
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_confirm_order.*

class ConfirmOrderActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartArrayList: ArrayList<Cart>
    private lateinit var adapter: ConfirmOrderAdapter
    private lateinit var FireStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)
        setupActionBar()

        recyclerView = findViewById(R.id.recycler_place_order)
        recycler_place_order.layoutManager = LinearLayoutManager(this)

        cartArrayList = arrayListOf()

        adapter = ConfirmOrderAdapter(this, cartArrayList)
        recycler_place_order.adapter = adapter

        recyclerView.adapter = adapter

        EventChangeListener()
        getCartTotalPrice()

        address.setOnClickListener {
            startActivity(Intent(this, AddressActivity::class.java))
        }

        val paymentFragment = PaymentFragment()

        paymentMethod.setOnClickListener {
            paymentFragment.show(supportFragmentManager, "PaymentDialog")
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_place_order_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbar_place_order_activity.setNavigationOnClickListener { onBackPressed() }
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