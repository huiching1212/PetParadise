package com.example.petparadise.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petparadise.R
import com.example.petparadise.adapter.ServiceAdapter
import com.example.petparadise.models.Service
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_grooming.*

class GroomingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceArrayList: ArrayList<Service>
    private lateinit var adapter: ServiceAdapter
    private lateinit var FireStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grooming)
        setupActionBar()

        recyclerView = findViewById(R.id.recycler_grooming)
        recycler_grooming.layoutManager = LinearLayoutManager(this)

        serviceArrayList = arrayListOf()

        adapter = ServiceAdapter(this, serviceArrayList)
        recycler_grooming.adapter = adapter

        recyclerView.adapter = adapter

        EventChangeListener()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_grooming_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbar_grooming_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun EventChangeListener() {
        FireStore = FirebaseFirestore.getInstance()
        FireStore.collection("Service").whereEqualTo("category", "Grooming Salon", )
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            serviceArrayList.add(dc.document.toObject(Service::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }
}