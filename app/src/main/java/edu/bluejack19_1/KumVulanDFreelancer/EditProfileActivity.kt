package edu.bluejack19_1.KumVulanDFreelancer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    fun loadProfileImage() {
        firebaseStorageReference().child(User.getProfileImage()).downloadUrl.addOnSuccessListener{
                uri -> Glide.with(this)
            .load(uri)
            .thumbnail(0.25f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgProfile)
        }
    }

    fun loadName() {
        txtName.setText(User.getName())
    }

    fun loadAbout() {
        txtAbout.setText(User.getAbout())
    }

    fun loadAcademic() {
        txtAcademic.setText(User.getAcademicRecord())
    }

    fun initializeInitialValues() {
        loadProfileImage()
        loadName()
        loadAbout()
        loadAcademic()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initializeInitialValues()
    }
}
