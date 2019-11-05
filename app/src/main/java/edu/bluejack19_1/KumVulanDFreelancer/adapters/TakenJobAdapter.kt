package edu.bluejack19_1.KumVulanDFreelancer.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import edu.bluejack19_1.KumVulanDFreelancer.*
import kotlinx.android.synthetic.main.taken_job.view.*
import java.lang.Exception

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

        row.txtJobName.text = job.name
        row.txtDeadline.text = job.deadline
        row.txtStatus.text = job.status

        row.btnFinishJob.setOnClickListener {

        }

        return row
    }
}