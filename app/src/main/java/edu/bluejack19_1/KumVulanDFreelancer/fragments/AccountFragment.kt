package edu.bluejack19_1.KumVulanDFreelancer.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import edu.bluejack19_1.KumVulanDFreelancer.R
import edu.bluejack19_1.KumVulanDFreelancer.User
import edu.bluejack19_1.KumVulanDFreelancer.firebaseStorage
import edu.bluejack19_1.KumVulanDFreelancer.firebaseStorageReference
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    private fun loadProfileImage() {
        val PROFILE_IMAGE_DIR = "/profile_images"
        firebaseStorageReference().child("${PROFILE_IMAGE_DIR}/${User.data?.get("profile_image")}").downloadUrl.addOnSuccessListener{
                uri -> Glide.with(this)
            .load(uri)
            .thumbnail(0.25f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profileImage)
        }
//      uri -> Picasso.with(this.context).load(uri).into(profileImage)
    }

    private fun loadProfileDatas() {
        val data = User.data
        txtName.text = data?.get(User.NAME).toString()
        txtJobsTaken.text = getString(R.string.profile_jobs_done, data?.get(User.JOBS_DONE).toString())
        txtRating.text = getString(R.string.profile_rating, data?.get(User.RATING).toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadProfileImage()
        loadProfileDatas()
    }
}
