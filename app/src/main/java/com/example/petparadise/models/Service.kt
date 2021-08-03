package com.example.petparadise.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service (
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var time: String = "",
    var telNo: String = "",
    var category: String = ""
): Parcelable