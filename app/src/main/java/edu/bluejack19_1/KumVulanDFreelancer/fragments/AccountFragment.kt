package edu.bluejack19_1.KumVulanDFreelancer.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import edu.bluejack19_1.KumVulanDFreelancer.*
import edu.bluejack19_1.KumVulanDFreelancer.adapters.ReviewAdapter
import kotlinx.android.synthetic.main.fragment_account.*
import org.w3c.dom.Text
import java.util.*

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    private fun loadName(data: Map<String, Any>?) {
        txtName.text = data?.get(User.NAME).toString()
    }

    private fun loadJobsTaken(data: Map<String, Any>?) {
        txtJobsTaken.text = getString(R.string.profile_jobs_done, data?.get(User.JOBS_DONE).toString())
    }

    private fun loadRating(data: Map<String, Any>?) {
        txtRating.text = getString(R.string.profile_rating, data?.get(User.RATING).toString())
    }

    private fun loadProfileImage(data: Map<String, Any>?) {

        firebaseStorageReference().child("${User.PROFILE_IMAGE_DIR}/${data?.get("profile_image")}").downloadUrl.addOnSuccessListener{
                uri -> Glide.with(this)
            .load(uri)
            .thumbnail(0.25f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profileImage)
        }
//      uri -> Picasso.with(this.context).load(uri).into(profileImage)
    }

    private fun loadSkills(data: Map<String, Any>?) {
        val skills = data?.get(User.SKILLS) as List<String>

        skills.forEach {
            var skill: TextView = View.inflate(context, R.layout.profile_skill, null) as TextView
            skill.text = it

            val params: FlexboxLayout.LayoutParams = FlexboxLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(5, 10, 5, 10)
            skill.layoutParams = params

            skillsContainer.addView(skill)
        }

    }

    private fun loadAbout(data: Map<String, Any>?) {
        txtAbout.text = data?.get(User.ABOUT).toString()
    }

    private fun loadAcademic(data: Map<String, Any>?) {
        txtAcademic.text = data?.get(User.ACADEMIC).toString()
    }

    private fun loadReview(data: Map<String, Any>?) {
        val reviews = data?.get(User.REVIEWS) as List<Review>
        val adapter = ReviewAdapter(this.context!!, reviews)
        listReview.adapter = adapter
    }

    private fun loadProfileDatas() {
        val data = User.data

        loadProfileImage(data)
        loadName(data)
        loadJobsTaken(data)
        loadRating(data)
        loadSkills(data)
        loadAbout(data)
        loadAcademic(data)
        loadReview(data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadProfileDatas()
    }
}
