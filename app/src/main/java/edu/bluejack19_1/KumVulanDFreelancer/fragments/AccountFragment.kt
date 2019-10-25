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
import kotlin.collections.ArrayList

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

        firebaseStorageReference().child("${User.PROFILE_IMAGE_DIR}/${data?.get(User.PROFILE_IMAGE)}").downloadUrl.addOnSuccessListener{
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

    private fun getReviewArrayList(list: ArrayList<Map<String, Any>>): ArrayList<Review> {
        var reviews: ArrayList<Review> = ArrayList()

        for (i in 0..4) {
            val name = list[i].get(User.NAME).toString()
            val profile_image = list[i].get(User.PROFILE_IMAGE).toString()
            val rating = list[i].get(User.RATING).toString().toBigDecimal()
            Log.d("firebase", "asd")
            val review = list[i].get(User.REVIEW).toString()
            reviews.add(Review(name, profile_image, rating, review))
        }
        return reviews
    }

    private fun loadReview(data: Map<String, Any>?) {
        val reviews = data?.get(User.REVIEWS) as ArrayList<Map<String, Any>>
        Log.d("firebase", reviews.toString())
        val adapter = ReviewAdapter(this.context!!, getReviewArrayList(reviews))
        listReview.adapter = adapter

        var totalHeight = 0
        for(i in 0..4) {
            val item = adapter.getView(i, null, listReview)
            item.measure(0, 0)
            totalHeight += item.measuredHeight
        }

        val params = listReview.layoutParams
        params.height = totalHeight + (listReview.dividerHeight * (listReview.count - 1))
        listReview.layoutParams = params
        Log.d("firebase", totalHeight.toString())
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
