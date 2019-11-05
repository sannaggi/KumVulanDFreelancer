package edu.bluejack19_1.KumVulanDFreelancer.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import edu.bluejack19_1.KumVulanDFreelancer.*
import edu.bluejack19_1.KumVulanDFreelancer.adapters.TakenJobAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.redirect_to_jobs_button.view.*

class HomeFragment(main: MainActivity): Fragment() {
    val main = main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeSpinner()
        initializeRedirectButton()
        fetchData()
    }

    private fun fetchData() {
        val jobs = ArrayList<HashMap<String, Any>>()

        if (User.getRole() == User.FREELANCER) {
            firebaseDatabase()
                .collection("jobs")
                .whereEqualTo("client", firebaseAuth().currentUser!!.email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        jobs.add(document.data as HashMap<String, Any>)
                    }
                    firebaseDatabase()
                        .collection("jobs")
                        .whereEqualTo("freelancer", firebaseAuth().currentUser!!.email)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                jobs.add(document.data as HashMap<String, Any>)
                            }
                            Log.d("firebase", jobs.toString())
                            updateJobsUI(jobs)
                        }
                }
        }
    }

    private fun updateJobsUI(jobs: ArrayList<HashMap<String, Any>>) {
        jobsContainer.removeView(progress_circular)
        if (jobs.isEmpty()) {
            emptyJobMessageContainer.visibility = View.VISIBLE
            return
        }

        onGoingJobsContainer.visibility = View.VISIBLE
        val adapter = TakenJobAdapter(this.context!!, getTakenJobsList(jobs))
        listTakenJobs.adapter = adapter

        var size = jobs.size

        var totalHeight = 0
        for(i in 0 until size) {
            val item = adapter.getView(i, null, listTakenJobs)
            item.measure(0, 0)
            totalHeight += item.measuredHeight
        }

        val params = listTakenJobs.layoutParams
        params.height = totalHeight + (listTakenJobs.dividerHeight * (listTakenJobs.count - 1))
        listTakenJobs.layoutParams = params
    }

    private fun getTakenJobsList(list: ArrayList<HashMap<String, Any>>): ArrayList<TakenJob> {
        val jobs = ArrayList<TakenJob>()
        val size = list.size

        for (i in 0 until size) {
            val name = list[i].get(TakenJob.NAME).toString()
            val client = list[i].get(TakenJob.CLIENT).toString()
            val deadline = list[i].get(TakenJob.DEADLINE).toString()
            val description = list[i].get(TakenJob.DESCRIPTION).toString()
            val est_price = list[i].get(TakenJob.EST_PRICE).toString().toInt()
            val freelancer = list[i].get(TakenJob.FREELANCER).toString()
            val status = list[i].get(TakenJob.STATUS).toString()

            jobs.add(TakenJob(name, client, deadline, description, est_price, freelancer, status))
        }

        return jobs
    }

    private fun initializeSpinner() {
        ArrayAdapter.createFromResource(
            this.context!!,
            R.array.role_array,
            android.R.layout.simple_spinner_item
        ) .also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerRole.adapter = adapter
        }

        spinnerRole.setSelection(0)
        if (User.getRole() == User.FREELANCER) {
            spinnerRole.setSelection(1)
        }
    }

    private fun initializeRedirectButton() {
        btnRedirectJobs.setOnClickListener {
            main.jumpToJobsFragment()
        }
    }

}