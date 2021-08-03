package com.example.petparadise.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.petparadise.GlideLoader
import com.example.petparadise.R
import com.example.petparadise.firestore.FirestoreClass
import com.example.petparadise.models.Cart
import com.example.petparadise.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_card_view.*
import kotlinx.android.synthetic.main.layout_product_details.*

class CardViewActivity : BaseActivity() {

    private lateinit var mProductDetails: Product
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_view)

        val layoutInflater: LayoutInflater = LayoutInflater.from(applicationContext)
        val view: View = layoutInflater.inflate(
            R.layout.layout_product_details,
            root_layout,
            false
        )

        root_layout.addView(view, 0)
        setupActionBar()

        getProductDetails(this)

        val productKey: Product? = intent.getParcelableExtra("ProductKey")
        //val product = Product(productKey!!.id, productKey.image, productKey.price)
        val totalPrice = productKey!!.price.toDouble()
        val cart = Cart(
            productKey.id, productKey.name, productKey.image, productKey.price.toDouble(), quantity = 1.0, totalPrice
        )
        addToCart.setOnClickListener {
            FirestoreClass().addItemToCart(this, cart)
        }
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

    private fun getProductDetails(activity: Activity) {
        val productKey: Product? = intent.getParcelableExtra("ProductKey")
        if (productKey != null) {
            mFireStore.collection("Product")
                .document(productKey.id)
                .get()
                .addOnSuccessListener { document ->
                    Log.i(activity.javaClass.simpleName, document.toString())

                    val product = document.toObject(Product::class.java)

                    when (activity) {
                        is CardViewActivity -> {
                            if (product != null) {
                                activity.productDetailsSuccess(product)
                            }
                        }
                    }
                }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbar_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun productDetailsSuccess(product: Product) {
        mProductDetails = product

        //hideProgressDialog()

        GlideLoader(this).loadUserPicture(product.image, detailsImage)
        detailsName.text = product.name
        detailsPrice.text = java.lang.StringBuilder("RM ").append(product.price)
        features.text = product.features
        ingredients.text = product.ingredients
        size.text = product.size
    }

    fun addToCartSuccess() {
        Toast.makeText(baseContext, "Added to cart.",
            Toast.LENGTH_SHORT).show()
    }
}