package edu.bluejack19_1.KumVulanDFreelancer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.skillsContainer
import kotlinx.android.synthetic.main.activity_edit_profile.txtAbout
import kotlinx.android.synthetic.main.activity_edit_profile.txtAcademic
import kotlinx.android.synthetic.main.activity_edit_profile.txtName
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.profile_edit_skill.view.*

class EditProfileActivity : AppCompatActivity() {

    var skillsList: ArrayList<View> = ArrayList()

    override fun onResume() {
        super.onResume()
        System.last_activity = System.EDIT_PROFILE_ACTIVITY
    }

    private fun loadSkills() {
        val skills = User.getSkills()

        skills.forEach {
            var skill: LinearLayout = View.inflate(this, R.layout.profile_edit_skill, null) as LinearLayout

            val params: FlexboxLayout.LayoutParams = FlexboxLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(5, 10, 5, 10)
            skill.layoutParams = params

            skill.text.text = it
            skill.btnRemove.setOnClickListener{
                skillsList.remove(skill)
                skillsContainer.removeView(skill)
            }

            skillsContainer.addView(skill)
            skillsList.add(skill)
        }
    }

    private fun loadProfileImage() {
        firebaseStorageReference().child(User.getProfileImage()).downloadUrl.addOnSuccessListener{
                uri -> Glide.with(this)
            .load(uri)
            .thumbnail(0.25f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgProfile)
        }
    }

    private fun loadName() {
        txtName.setText(User.getName())
    }

    private fun loadAbout() {
        txtAbout.setText(User.getAbout())
    }

    private fun loadAcademic() {
        txtAcademic.setText(User.getAcademicRecord())
    }

    private fun initializeInitialValues() {
        loadProfileImage()
        loadName()
        loadSkills()
        loadAbout()
        loadAcademic()
    }

    private fun getSkills(): List<String> {
        return skillsList.map {
            it.text.text.toString()
        }
    }

    private fun getNewProfileDatas(): HashMap<String, Any> {
        val name = txtName.text.toString()
        val profileImage = User.getProfileImageName().toString()
        val skills = getSkills()
        val about = txtAbout.text.toString()
        val academic = txtAcademic.text.toString()

        val data = HashMap<String, Any>()
        data[User.NAME] = name
        data[User.PROFILE_IMAGE] = profileImage
        data[User.SKILLS] = skills
        data[User.ABOUT] = about
        data[User.ACADEMIC] = academic
        data[User.JOBS_DONE] = User.getJobsDone()
        data[User.RATING] = User.getRating().toDouble()
        data[User.REVIEWS] = User.getReviews()

        return data
    }

    private fun refreshUserData(data: HashMap<String, Any>) {
        User.data?.set(User.NAME, data[User.NAME].toString())
        User.data?.set(User.PROFILE_IMAGE, data[User.PROFILE_IMAGE].toString())
        User.data?.set(User.SKILLS, data[User.SKILLS] as List<String>)
        User.data?.set(User.ABOUT, data[User.ABOUT].toString())
        User.data?.set(User.ACADEMIC, data[User.ACADEMIC].toString())
    }

    private fun initializeCommitButton() {
        btnCommit.setOnClickListener {
            val data = getNewProfileDatas()
            firebaseDatabase().collection("users")
                .document(firebaseAuth().currentUser!!.email + "")
                .set(data).addOnSuccessListener {
                    refreshUserData(data)
                    Toast.makeText(applicationContext, "Profile updated successfully", Toast.LENGTH_LONG).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "Profile update failed", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initializeInitialValues()
        initializeCommitButton()
    }
}
