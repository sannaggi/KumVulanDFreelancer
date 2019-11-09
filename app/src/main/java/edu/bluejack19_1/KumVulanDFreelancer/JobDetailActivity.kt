package edu.bluejack19_1.KumVulanDFreelancer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_job_detail.*
import kotlinx.android.synthetic.main.fragment_account_freelancer.*

class JobDetailActivity : AppCompatActivity() {

    lateinit var jobID: String
    lateinit var otherPartyEmail: String
    lateinit var role: String
    lateinit var data: HashMap<String, Any>
    lateinit var otherPartyData: HashMap<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        fetchIntentData()
        getJobData()
    }

    private fun fetchIntentData() {
        jobID = intent.extras!!.get(TakenJob.ID).toString()
        otherPartyEmail = intent.extras!!.get(TakenJob.OTHER_PARTY_EMAIL).toString()
        role = intent.extras!!.get(TakenJob.YOUR_ROLE).toString()
    }

    private fun getJobData() {
        firebaseDatabase()
            .collection("jobs")
            .document(jobID)
            .get()
            .addOnSuccessListener {
                data = it.data as HashMap<String, Any>

                firebaseDatabase()
                    .collection("users")
                    .document(otherPartyEmail)
                    .get()
                    .addOnSuccessListener { it2 ->
                        otherPartyData = it2.data as HashMap<String, Any>

                        showDatas()
                    }
            }
    }

    private fun showDatas() {
        progress_circular.visibility = View.GONE
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.NO_GRAVITY
        container.layoutParams = params
        jobDetail.visibility = View.VISIBLE

        assignDatas()
    }

    private fun assignDatas() {
        txtJobName.text = data.get(TakenJob.NAME).toString()
        txtPrice.text = "Rp. ${data.get(TakenJob.EST_PRICE).toString()}"
        txtDeadline.text = data.get(TakenJob.DEADLINE).toString()
        txtDescription.text = data.get(TakenJob.DESCRIPTION).toString()

        loadUserDatas()
    }

    private fun loadUserDatas() {
        when (role) {
            TakenJob.CLIENT -> {
                txtClientName.text = User.getName()
                txtFreelancerName.text = otherPartyData.get(User.NAME).toString()

                loadImage(User.getProfileImage(), imgClient)
                loadImage(User.getProfileImagePath(otherPartyData.get(User.PROFILE_IMAGE).toString()), imgFreelancer)
            }
            TakenJob.FREELANCER -> {
                txtClientName.text = otherPartyData.get(User.NAME).toString()
                txtFreelancerName.text = User.getName()

                loadImage(User.getProfileImage(), imgFreelancer)
                loadImage(User.getProfileImagePath(otherPartyData.get(User.PROFILE_IMAGE).toString()), imgClient)
            }
        }
    }

    private fun loadImage(path: String, img: CircleImageView?) {
        firebaseStorageReference()
            .child(path)
            .downloadUrl
            .addOnSuccessListener{uri ->
                if (img != null) {
                    Glide.with(this)
                        .load(uri)
                        .into(img)
                }
            }
    }
}
