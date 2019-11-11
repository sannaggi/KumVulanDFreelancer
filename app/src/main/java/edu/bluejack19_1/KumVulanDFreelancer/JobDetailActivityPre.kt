package edu.bluejack19_1.KumVulanDFreelancer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_job_detail_pre.*
import kotlinx.android.synthetic.main.applicant.view.*

class JobDetailActivityPre : AppCompatActivity() {

    lateinit var jobID: String
    lateinit var jobData: HashMap<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail_pre)

        fetchIntentData()
        getJobData()
    }

    private fun fetchIntentData() {
        jobID = intent.extras!!.get(TakenJob.ID).toString()
    }

    private fun getJobData() {
        firebaseDatabase()
            .collection("jobs")
            .document(jobID)
            .get()
            .addOnSuccessListener {
                jobData = it.data as HashMap<String, Any>

                firebaseDatabase()
                    .collection("users")
                    .document(jobData.get(TakenJob.CLIENT).toString())
                    .get()
                    .addOnSuccessListener { it2 ->
                        loadClientData(it2.data as HashMap<String, Any>)
                        launchActivity()
                    }
            }
    }

    private fun loadClientData(clientData: HashMap<String, Any>) {
        txtClientName.text = clientData.get(User.NAME).toString()
        loadImage(User.getProfileImagePath(clientData.get(User.PROFILE_IMAGE).toString()), imgClient)
    }

    private fun launchActivity() {
        showDatas()
    }

    private fun showDatas() {
        progress_circular.visibility = View.GONE
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.NO_GRAVITY
        container.layoutParams = params
        jobDetail.visibility = View.VISIBLE

        loadDatas()
    }

    private fun loadDatas() {
        txtJobName.text = jobData.get(TakenJob.NAME).toString()
        txtDeadline.text = jobData.get(TakenJob.DEADLINE).toString()
        txtPrice.text = jobData.get(TakenJob.EST_PRICE).toString()
        txtDescription.text = jobData.get(TakenJob.DESCRIPTION).toString()

        loadApplicants()
    }

    private fun loadApplicants() {
        val applicants = jobData.get(TakenJob.APPLICANTS) as ArrayList<String>
        if (applicants.isEmpty()) {
            applicantsContainer.visibility = View.GONE
            return
        }
        applicantsList.removeAllViews()

        applicants.forEach {
            var applicant = View.inflate(this, R.layout.applicant, null) as LinearLayout

            firebaseDatabase()
                .collection("users")
                .document(it)
                .get()
                .addOnSuccessListener { it2 ->
                    val applicantData = it2.data as HashMap<String, Any>

                    applicant.txtName.text = applicantData.get(User.NAME).toString()
                    loadImage(User.getProfileImagePath(applicantData.get(User.PROFILE_IMAGE).toString()), applicant.imgProfile)
                }

            applicantsList.addView(applicant)
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
