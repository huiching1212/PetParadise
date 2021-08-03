package com.example.petparadise.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentCard(
    var noOfCard: String? = null,
    var expDate: String? = null,
    var ccv: String? = null,
    var mobile: String? = null
): Parcelable