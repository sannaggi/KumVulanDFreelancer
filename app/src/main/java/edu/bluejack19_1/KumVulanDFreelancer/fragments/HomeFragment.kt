package edu.bluejack19_1.KumVulanDFreelancer.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import edu.bluejack19_1.KumVulanDFreelancer.*
import edu.bluejack19_1.KumVulanDFreelancer.adapters.TakenJobAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.redirect_to_jobs_button.view.*

class HomeFragment(main: MainActivity): Fragment(), OnItemSelectedListener {
    val main = main

    companion object {
        var role: String = TakenJob.CLIENT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeSpinner()
        initializeRedirectButton()
    }

    private fun fetchData() {
        Log.d("firebase", role)

        emptyJobMessageContainer.visibility = View.GONE
        progress_circular.visibility = View.VISIBLE
        onGoingJobsContainer.visibility = View.GONE
        btnNewJob.visibility = View.GONE

        val jobs = ArrayList<HashMap<String, Any>>()
        firebaseDatabase()
            .collection("jobs")
            .whereEqualTo(role, firebaseAuth().currentUser!!.email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val data = document.data as HashMap<String, Any>
                    Log.d("test", "$data $role ${firebaseAuth().currentUser!!.email}")
                    data.set("id", document.id)
                    jobs.add(data)
                }
                Log.d("firebase", jobs.toString())
                updateJobsUI(jobs, role)
            }
    }

    private fun updateJobsUI(jobs: ArrayList<HashMap<String, Any>>, role: String) {
        if (jobsContainer == null) return

        progress_circular.visibility = View.GONE
        if (jobs.isEmpty()) {
            if (role == TakenJob.FREELANCER)
                emptyJobMessageContainer.visibility = View.VISIBLE
            else
                btnNewJob.visibility = View.VISIBLE

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
            val id = list[i].get(TakenJob.ID).toString()

            jobs.add(TakenJob(name, client, deadline, description, est_price, freelancer, status, id))
        }

        return jobs
    }

    private fun initializeSpinner() {
        if (User.getRole() == User.CLIENT) {
            spinnerRole.visibility = View.GONE
            return
        }

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

        spinnerRole.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        role = if (position == 0) TakenJob.CLIENT else TakenJob.FREELANCER
        fetchData()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun initializeRedirectButton() {
        btnRedirectJobs.setOnClickListener {
            main.jumpToJobsFragment()
        }
    }

}