package com.example.petparadise.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    var id: String = "",
    var name: String = "",
    var image: String = "",
    var price: String = "",
    var features: String = "",
    var ingredients: String = "",
    var size: String = "",
    var category: String = ""
): Parcelable
