package com.example.petparadise.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.petparadise.Constants
import com.example.petparadise.models.Cart
import com.example.petparadise.models.User
import com.example.petparadise.ui.activities.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()
    private var cartData: Cart? = Cart()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun addItemToCart(activity: CardViewActivity, cartInfo: Cart): Boolean {
        var isAdded: Boolean = true
        mFireStore.collection("Cart")
            .document(getCurrentUserID()).collection("Product").document(cartInfo.id.toString())
            .get()
            .addOnSuccessListener {
                Log.d("This", "get cart successfully")
                cartData = Cart(
                    it.getString("id"),
                    it.getString("name"),
                    it.getString("image"),
                    it.getDouble("price"),
                    it.getDouble("quantity"),
                    it.getDouble("totalPrice")
                )

                if (cartData?.id != null) {
                    val quantity: Double = it.getDouble("quantity")!!.toDouble()
                    val perPrice: Double = it.getDouble("price")!!.toDouble()
                    val addedQuantity = quantity + 1
                    val totalPrice = perPrice * addedQuantity

                    mFireStore.collection("Cart").document(getCurrentUserID()).collection("Product")
                        .document(cartInfo.id.toString())
                        .update("quantity", addedQuantity)

                    mFireStore.collection("Cart").document(getCurrentUserID()).collection("Product")
                        .document(cartInfo.id.toString())
                        .update("totalPrice", totalPrice)

                        .addOnSuccessListener {
                            Log.d("This", "update to cart successfully")
                            isAdded = true
                            activity.addToCartSuccess()
                        }.addOnFailureListener {
                            Log.d("This", it.message.toString())
                            isAdded = false
                        }
                } else {
                    mFireStore.collection("Cart")
                        .document(getCurrentUserID()).collection("Product").document(cartInfo.id.toString())
                        .set(cartInfo, SetOptions.merge())
                        .addOnSuccessListener {
                            activity.addToCartSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.e(
                                activity.javaClass.simpleName,
                                "Error while adding the product to cart.",
                                e
                            )
                        }
                }
            }.addOnFailureListener {
                Log.d("This", it.message.toString())
                isAdded = false
            }

        return isAdded
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                if (user != null) {
                    editor.putString(
                        Constants.LOGGED_IN_USERNAME,
                        user.fullName
                    )
                    editor.apply()
                }

                when (activity) {
                    is LoginActivity -> {
                        if (user != null) {
                            activity.userLoggedInSuccess(user)
                        }
                    }
                    is SettingsActivity -> {
                        if (user != null) {
                            activity.userDetailsSuccess(user)
                        }
                    }
                    is DashboardActivity -> {
                        if (user != null) {
                            activity.userDetailsSuccess(user)
                        }
                    }
                }
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
                    )
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when (activity) {
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }

            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnFailureListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                }
        }
            .addOnFailureListener { exception ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
}