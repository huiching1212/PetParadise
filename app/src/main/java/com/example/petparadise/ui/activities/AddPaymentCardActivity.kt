package com.example.petparadise.ui.activities

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.braintreepayments.cardform.view.CardForm
import com.example.petparadise.R
import com.example.petparadise.firestore.FirestoreClass
import com.example.petparadise.models.PaymentCard
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_payment_card.*

class AddPaymentCardActivity : AppCompatActivity() {

    private lateinit var btnAdd: Button
    private lateinit var cardForm: CardForm
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment_card)
        setupActionBar()

        firestore = FirebaseFirestore.getInstance()

        btnAdd = findViewById(R.id.btnAdd)
        cardForm = findViewById(R.id.card_form)

        cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .setup(this)

        cardForm.cvvEditText
            .setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD)

        btnAdd.setOnClickListener {
            if (cardForm.isValid) {
                val builder = AlertDialog.Builder(this)
                builder.setPositiveButton("Confirm") { _, _ ->
                    val paymentCard = PaymentCard(
                        cardForm.cardNumber,
                        cardForm.expirationDateEditText.text.toString(),
                        cardForm.cvv,
                        null
                    )
                    firestore.collection("Payment Card")
                        .document(FirestoreClass().getCurrentUserID())
                        .collection("Payment Card").document(cardForm.cardNumber)
                        .set(paymentCard)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Added successfully", Toast.LENGTH_LONG).show()
                        }.addOnFailureListener{
                            Toast.makeText(this, "Add unsuccessfully", Toast.LENGTH_LONG).show()
                        }
                }
                builder.setNegativeButton("Cancel") { _, _ -> }

                builder.setTitle("Confirmation")
                builder.setMessage("Are you sure to add this card?")
                builder.create().show()
            } else {
                Toast.makeText(this, "Please complete the form", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_add_card_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbar_add_card_activity.setNavigationOnClickListener { onBackPressed() }
    }
}