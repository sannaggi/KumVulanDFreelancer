package edu.bluejack19_1.KumVulanDFreelancer.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import edu.bluejack19_1.KumVulanDFreelancer.*
import edu.bluejack19_1.KumVulanDFreelancer.fragments.HomeFragment
import kotlinx.android.synthetic.main.taken_job.view.*

class TakenJobAdapter(private val context: Context, private val jobs: List<TakenJob>): BaseAdapter(){

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return jobs.size
    }

    override fun getItem(position: Int): Any {
        return jobs[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = inflater.inflate(R.layout.taken_job, parent, false)
        val job = getItem(position) as TakenJob

        loadValues(row, job)

        row.setOnClickListener {

        }

        row.btnFinishJob.setOnClickListener {

        }

        return row
    }

    private fun loadValues(row: View, job: TakenJob) {
        row.txtJobName.text = job.name
        row.txtDeadline.text = job.deadline
        row.txtStatus.text = job.status

        loadOtherPartyDatas(row, job)
    }

    private fun loadOtherPartyDatas(row: View, job: TakenJob) {
        val otherParty = if (HomeFragment.role == "freelancer") job.client else job.freelancer
        firebaseDatabase()
            .collection("users")
            .document(otherParty)
            .get()
            .addOnSuccessListener {
                if (row.txtOtherParty != null) {
                    val data = it.data as HashMap<String, Any>
                    row.txtOtherParty.text = data.get(User.NAME).toString()

                    loadImage(data.get(User.PROFILE_IMAGE).toString(), row)
                }
            }
    }

    private fun loadImage(imagePath: String, row: View) {
        firebaseStorageReference()
            .child(User.getProfileImagePath(imagePath))
            .downloadUrl
            .addOnSuccessListener{uri ->
                if (row.imgOtherParty != null) {
                    Glide.with(this.context)
                        .load(uri)
                        .into(row.imgOtherParty)
                }
            }
            .addOnFailureListener {
                Log.d("firebase", it.toString())
            }
    }
}