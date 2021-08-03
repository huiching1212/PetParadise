package com.example.petparadise.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val profileCompleted: Int = 0): Parcelable