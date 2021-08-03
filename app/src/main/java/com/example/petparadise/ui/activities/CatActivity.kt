package com.example.petparadise.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petparadise.R
import com.example.petparadise.adapter.ProductAdapter
import com.example.petparadise.models.Product
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_product.*

class CatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<Product>
    private lateinit var adapter: ProductAdapter
    private lateinit var FireStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setupActionBar()

        recyclerView = findViewById(R.id.recycler_product)
        val gridLayoutManager = GridLayoutManager(this,2)
        recycler_product.layoutManager = gridLayoutManager

        productArrayList = arrayListOf()

        adapter = ProductAdapter(this, productArrayList)
        recycler_product.adapter = adapter

        recyclerView.adapter = adapter

        EventChangeListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopping_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_shopping -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun EventChangeListener() {
        FireStore = FirebaseFirestore.getInstance()
        FireStore.collection("Product").whereEqualTo("category", "Cat", )
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            productArrayList.add(dc.document.toObject(Product::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }
}