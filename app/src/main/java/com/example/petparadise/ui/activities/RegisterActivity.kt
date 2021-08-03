package com.example.petparadise.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.petparadise.R
import com.example.petparadise.firestore.FirestoreClass
import com.example.petparadise.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        haveAccount.setOnClickListener {
            //onBackPressed()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        if (editRegisterFullName.text.toString().isEmpty()) {
            editRegisterFullName.error = "Please fill in full name."
            editRegisterFullName.requestFocus()
            return
        }

        if (editRegisterEmailAddress.text.toString().isEmpty()) {
            editRegisterEmailAddress.error = "Please fill in email."
            editRegisterEmailAddress.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editRegisterEmailAddress.text.toString()).matches()) {
            editRegisterEmailAddress.error = "Please fill in valid email."
            editRegisterEmailAddress.requestFocus()
            return
        }

        if (editRegisterPassword.text.toString().isEmpty()) {
            editRegisterPassword.error = "Please fill in password."
            editRegisterPassword.requestFocus()
            return
        }

        if (editRegisterConfirmPassword.text.toString().isEmpty()) {
            editRegisterConfirmPassword.error = "Please fill in confirm password."
            editRegisterConfirmPassword.requestFocus()
            return
        }

        if (editRegisterConfirmPassword.text.toString() != editRegisterPassword.text.toString()) {
            editRegisterConfirmPassword.error = "Password and confirm password does not match."
            editRegisterConfirmPassword.requestFocus()
            return
        }

        showProgressDialog(resources.getString(R.string.pleaseWait))

        auth.createUserWithEmailAndPassword(editRegisterEmailAddress.text.toString(), editRegisterPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                hideProgressDialog()
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val user = User(
                        firebaseUser.uid,
                        editRegisterFullName.text.toString(),
                        editRegisterEmailAddress.text.toString()
                    )

                    FirestoreClass().registerUser(this, user)

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(baseContext, "Registration failed. Try again later.",
                    Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun userRegistrationSuccess() {
        Toast.makeText(baseContext, "You are registered successfully",
            Toast.LENGTH_SHORT).show()
    }
}