package com.example.petparadise.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.petparadise.Constants
import com.example.petparadise.GlideLoader
import com.example.petparadise.R
import com.example.petparadise.firestore.FirestoreClass
import com.example.petparadise.models.User
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        textMain.text = "Welcome, $username."

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        editAccountFullName.isEnabled = false
        editAccountFullName.setText(mUserDetails.fullName)

        editAccountEmailAddress.isEnabled = false
        editAccountEmailAddress.setText(mUserDetails.email)

        headerPicture.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.headerPicture -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //Toast.makeText(baseContext, "You already have the storage permission.",
                        //Toast.LENGTH_SHORT).show()
                        Constants.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btnSave -> {
                    if (validateUserProfileDetails()) {
                        showProgressDialog(resources.getString(R.string.pleaseWait))

                        if (mSelectedImageFileUri != null)
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                        else {
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val mobileNumber = editAccountMobile.text.toString().trim { it <= ' ' }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        userHashMap[Constants.COMPLETE_PROFILE] = 2

        //showProgressDialog(resources.getString(R.string.pleaseWait))

        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(baseContext, "The storage permission is granted.",
            //Toast.LENGTH_SHORT).show()
            Constants.showImageChooser(this)
        } else {
            Toast.makeText(baseContext, "Oops, you just denied the permission for storage. You can allow it from settings.",
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    mSelectedImageFileUri = data.data!!

                    //profilePicture.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                    GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, headerPicture)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Image selection failed!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled.")
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(editAccountMobile.text.toString().trim { it <= ' ' }) -> {
                editAccountMobile.error = "Please enter mobile number."
                editAccountMobile.requestFocus()
                false
            }
            else -> {
                true
            }
        }
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(this, "Your profile is updated successfully.",
            Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        //hideProgressDialog()
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
}