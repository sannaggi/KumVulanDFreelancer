package edu.bluejack19_1.KumVulanDFreelancer.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

class AccountFragment(parent: MainActivity) : Fragment() {

    val parent = parent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onResume() {
        super.onResume()

        System.last_activity = System.ACCOUNT_FRAGMENT
    }

    private fun loadName() {
        txtName.text = User.getName()
    }

    private fun loadJobsTaken() {
        txtJobsTaken.text = getString(R.string.profile_jobs_done, User.getJobsDone().toString())
    }

    private fun loadRating() {
        txtRating.text = getString(R.string.profile_rating, User.getRating())
    }

    private fun loadProfileImage() {

        firebaseStorageReference().child(User.getProfileImage()).downloadUrl.addOnSuccessListener{
                uri -> Glide.with(this)
            .load(uri)
            .thumbnail(0.25f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profileImage)
        }
//      uri -> Picasso.with(this.context).load(uri).into(profileImage)
    }

    private fun loadSkills() {
        val skills = User.getSkills()

        val asd: ArrayList<View> = ArrayList()

        skills.forEach {


            var skill: TextView = View.inflate(context, R.layout.profile_skill, null) as TextView
            skill.text = it

            val params: FlexboxLayout.LayoutParams = FlexboxLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(5, 10, 5, 10)
            skill.layoutParams = params

            skillsContainer.addView(skill)
        }

    }

    private fun loadAbout() {
        txtAbout.text = User.getAbout()
    }

    private fun loadAcademic() {
        txtAcademic.text = User.getAcademicRecord()
    }

    private fun getReviewArrayList(list: ArrayList<Map<String, Any>>): ArrayList<Review> {
        var reviews: ArrayList<Review> = ArrayList()

        for (i in 0..4) {
            val name = list[i].get(User.NAME).toString()
            val profile_image = list[i].get(User.PROFILE_IMAGE).toString()
            val rating = list[i].get(User.RATING).toString().toBigDecimal()
            val review = list[i].get(User.REVIEW).toString()
            reviews.add(Review(name, profile_image, rating, review))
        }
        return reviews
    }

    private fun loadReview() {
        val reviews = User.getReviews()
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
    }

    private fun loadProfileDatas() {
        val data = User.data

        loadProfileImage()
        loadName()
        loadJobsTaken()
        loadRating()
        loadSkills()
        loadAbout()
        loadAcademic()
        loadReview()
    }

    private fun initializeLogoutButton() {
        btnLogout.setOnClickListener {
            firebaseAuth().signOut()
            parent.logout()
        }
    }

    private fun initializeEditButton() {
        btnEdit.setOnClickListener {
            val intent = Intent(this.context, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadProfileDatas()
        initializeLogoutButton()
        initializeEditButton()
    }
}
