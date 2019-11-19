package edu.bluejack19_1.KumVulanDFreelancer.recycleView.item

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.bluejack19_1.KumVulanDFreelancer.firebase.FinishedJob
import edu.bluejack19_1.KumVulanDFreelancer.R
import kotlinx.android.synthetic.main.item_posted_job_history.*

class PostedJobItem (var job: FinishedJob, val context: Context) : Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        init(viewHolder)
    }

    override fun getLayout(): Int = R.layout.item_posted_job_history

    private fun init(viewHolder: ViewHolder){
        viewHolder.posted_job_name.text = job.name
        viewHolder.posted_job_deadline.text = job.deadline
        viewHolder.posted_job_description.text = job.description
        viewHolder.posted_job_freelancer.text = job.freelancerName
        viewHolder.posted_job_price.text = job.price
        viewHolder.posted_job_status.text = job.status

        changeStatusColor(viewHolder, job.status)
        initReviewRatingButton(viewHolder)
    }

    private fun changeStatusColor(viewHolder: ViewHolder, status : String){
        var status = status
        if(status.equals("Finished")) viewHolder.posted_job_status
                .setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        else if(status.equals("Rejected")) viewHolder.posted_job_status
                .setTextColor(ContextCompat.getColor(context, R.color.red))
        else if(status.equals("Canceled")) viewHolder.posted_job_status
                .setTextColor(Color.YELLOW)

    }

    private fun initReviewRatingButton(viewHolder: ViewHolder){
        var isRated = false
        var isReviewed = false
        if(job.isRated == true) isRated = true
        if(job.isReviewed == true) isReviewed = true
        if(!job.status.equals("Finished")){
            isRated = true
            isReviewed = true
        }

        if(isRated and isReviewed) viewHolder.posted_job_review_rating.visibility = View.GONE
        else if(isRated) viewHolder.review_rating_separator.visibility = View.GONE
        else if(isReviewed) viewHolder.review_rating_separator.visibility = View.GONE

        viewHolder.posted_job_rating_button.setOnClickListener {

        }

        viewHolder.posted_job_review_button.setOnClickListener {

        }
    }
}