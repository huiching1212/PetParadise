package com.example.petparadise.ui.activities

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petparadise.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth = FirebaseAuth.getInstance()

        btnSubmit.setOnClickListener {
            forgotPassword()
        }
    }

    private fun forgotPassword() {
        if (editForgotPasswordEmail.text.toString().isEmpty()) {
            editForgotPasswordEmail.error = "Please enter email."
            editForgotPasswordEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editForgotPasswordEmail.text.toString()).matches()) {
            editForgotPasswordEmail.error = "Please enter valid email."
            editForgotPasswordEmail.requestFocus()
            return
        }

        auth.sendPasswordResetEmail(editForgotPasswordEmail.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email sent.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "This email haven't registered.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}