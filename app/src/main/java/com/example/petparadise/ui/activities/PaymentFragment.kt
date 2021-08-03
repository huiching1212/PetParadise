package com.example.petparadise.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petparadise.R
import com.example.petparadise.adapter.PaymentCardAdapter
import com.example.petparadise.firestore.FirestoreClass
import com.example.petparadise.models.PaymentCard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.dialog_payment.*

class PaymentFragment: BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentCardList: ArrayList<PaymentCard>
    private lateinit var adapter: PaymentCardAdapter
    private lateinit var FireStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_payment, container, false)

        recyclerView = view.findViewById(R.id.recycler_payment)

        paymentCardList = arrayListOf()

        adapter = PaymentCardAdapter(requireContext(), paymentCardList)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = adapter

        EventChangeListener()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addCard.setOnClickListener {
            startActivity(Intent(requireContext(), AddPaymentCardActivity::class.java))
        }
    }

    private fun EventChangeListener() {
        FireStore = FirebaseFirestore.getInstance()
        FireStore.collection("Payment Card").document(FirestoreClass().getCurrentUserID()).collection("Payment Card")
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            paymentCardList.add(dc.document.toObject(PaymentCard::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }
}