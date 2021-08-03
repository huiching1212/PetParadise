package com.example.petparadise.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petparadise.R
import kotlinx.android.synthetic.main.activity_address.*

class AddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_address_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbar_address_activity.setNavigationOnClickListener { onBackPressed() }
    }
}