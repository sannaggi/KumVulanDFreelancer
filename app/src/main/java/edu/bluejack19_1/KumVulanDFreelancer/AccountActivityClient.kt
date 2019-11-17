package edu.bluejack19_1.KumVulanDFreelancer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_account_client.*
import java.lang.Exception

class AccountActivityClient : AppCompatActivity() {

    lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_client)

        fetchIntentData()
        loadProfileDatas()
    }

    private fun fetchIntentData() {
        userID = intent.extras!!.get("ID").toString()
    }

    private fun loadName(name: String) {
        txtName.text = name
    }

    private fun loadProfileImage(imagePath: String) {
        try {
            firebaseStorageReference()
                .child(imagePath)
                .downloadUrl
                .addOnSuccessListener{
                        uri -> Glide.with(this)
                    .load(uri)
                    .into(profileImage)
                }
                .addOnFailureListener {
                    Log.d("firebase", it.toString())
                }
        } catch (e: Exception) {
            Log.d("firebase", "invalid image loading intercepted")
        }
    }

    private fun loadAbout(about: String) {
        txtAbout.text = about
    }

    private fun loadProfileDatas() {
        firebaseDatabase()
            .collection("users")
            .document(userID)
            .get()
            .addOnSuccessListener {
                disableLoading()
                showDatas(it.data as HashMap<String, Any>)
            }
    }

    private fun disableLoading() {
        progress_circular.visibility = View.GONE
        container.visibility = View.VISIBLE

        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        bigContainer.layoutParams = params
    }

    private fun showDatas(data: HashMap<String, Any>) {
        loadProfileImage(User.getProfileImagePath(data.get(User.PROFILE_IMAGE).toString()))
        loadName(data.get(User.NAME).toString())
        loadAbout(data.get(User.ABOUT).toString())
    }
}
