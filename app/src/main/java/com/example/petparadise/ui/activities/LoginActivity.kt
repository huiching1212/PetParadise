package com.example.petparadise.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.petparadise.Constants
import com.example.petparadise.R
import com.example.petparadise.firestore.FirestoreClass
import com.example.petparadise.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        registerAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            userLogin()
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    fun userLoggedInSuccess(user: User) {

        if (user.profileCompleted == 0) {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        finish()
    }

    private fun userLogin() {
        if (editTextEmailAddress.text.toString().isEmpty()) {
            editTextEmailAddress.error = "Please enter email."
            editTextEmailAddress.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmailAddress.text.toString()).matches()) {
            editTextEmailAddress.error = "Please enter valid email."
            editTextEmailAddress.requestFocus()
            return
        }

        if (editTextPassword.text.toString().isEmpty()) {
            editTextPassword.error = "Please enter password."
            editTextPassword.requestFocus()
            return
        }

        showProgressDialog(resources.getString(R.string.pleaseWait))

        auth.signInWithEmailAndPassword(editTextEmailAddress.text.toString(), editTextPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                //hideProgressDialog()
                if (task.isSuccessful) {
                    val user:FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    hideProgressDialog()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        //val currentUser:FirebaseUser? = auth.currentUser
        //updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            //startActivity(Intent(this, MainActivity::class.java))
            //Toast.makeText(baseContext, "Login successfully.",
                //Toast.LENGTH_SHORT).show()
            FirestoreClass().getUserDetails(this)
        } else {
            Toast.makeText(baseContext, "Login failed.",
                Toast.LENGTH_SHORT).show()
        }
    }
}