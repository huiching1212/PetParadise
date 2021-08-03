package com.example.petparadise.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.petparadise.Constants
import com.example.petparadise.GlideLoader
import com.example.petparadise.R
import com.example.petparadise.firestore.FirestoreClass
import com.example.petparadise.models.User
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.nav_header.*

class DashboardActivity : BaseActivity() {

    private lateinit var mUserDetails: User

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        welcome.text = "Welcome, $username."

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_settings ->
                    startActivity(Intent(this, SettingsActivity::class.java))
                R.id.dog ->
                    startActivity(Intent(this, DogActivity::class.java))
                R.id.cat ->
                    startActivity(Intent(this, CatActivity::class.java))
                R.id.groomingSalon ->
                    startActivity(Intent(this, GroomingActivity::class.java))
            }
            true
        }
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.pleaseWait))
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        mUserDetails = user

        hideProgressDialog()

        GlideLoader(this@DashboardActivity).loadUserPicture(user.image, dashboardImage)
        dashboardName.text = user.fullName
        dashboardEmail.text = user.email
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}