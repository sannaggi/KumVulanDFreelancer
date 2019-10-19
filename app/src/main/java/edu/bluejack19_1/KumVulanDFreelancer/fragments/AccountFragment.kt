package edu.bluejack19_1.KumVulanDFreelancer.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import edu.bluejack19_1.KumVulanDFreelancer.R
import edu.bluejack19_1.KumVulanDFreelancer.User
import edu.bluejack19_1.KumVulanDFreelancer.firebaseStorage
import edu.bluejack19_1.KumVulanDFreelancer.firebaseStorageReference
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {
    val PROFILE_IMAGE_DIR = "/profile_images"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        txtName.text = User.data?.get("name").toString()

        firebaseStorageReference().child("${PROFILE_IMAGE_DIR}/${User.data?.get("profile_image")}").downloadUrl.addOnSuccessListener{
            uri -> Glide.with(this)
            .load(uri)
            .into(profileImage)
        }
//        Log.d("firebase", imageUrl)
//        Picasso.with(this.context).load(imageUrl).into(profileImage)
    }
}