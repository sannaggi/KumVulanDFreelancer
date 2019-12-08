package edu.bluejack19_1.KumVulanDFreelancer.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.bluejack19_1.KumVulanDFreelancer.*
import edu.bluejack19_1.KumVulanDFreelancer.firebase.FirebaseUtil

import edu.bluejack19_1.KumVulanDFreelancer.adapters.FinishedJobAdapter
import edu.bluejack19_1.KumVulanDFreelancer.firebase.FinishedJob
import edu.bluejack19_1.KumVulanDFreelancer.recycleView.item.PostedJobItem
import edu.bluejack19_1.KumVulanDFreelancer.recycleView.item.TakenJobItem
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.item_posted_job_history.view.*

class HistoryFragment(parent: MainActivity) : Fragment() {
    val parent = parent
    private lateinit var historyListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var jobSection: Section
    private var finishedJobs: List<FinishedJob>? = null
    private lateinit var items: List<Item>
    var isClient: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(HomeFragment.role.equals("client"))
            historyListenerRegistration = FinishedJobAdapter.initializePostedJobListener(this.activity!!, this::updateRecyclerView)
        else{
            isClient = false
            historyListenerRegistration = FinishedJobAdapter.initializeTakenJobListener(this.activity!!, this::updateRecyclerView)
        }

        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeSortSpinner()
    }

    private fun initializeSortSpinner(){
        history_sort_category.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                if(this@HistoryFragment.finishedJobs == null) return
                var finishedJobs = this@HistoryFragment.finishedJobs
                Log.d("SPINNER", "Spinner selected = ${history_sort_category.getItemAtPosition(position)}")
                if(history_sort_category.getItemAtPosition(position).equals("Deadline")){
                    finishedJobs = finishedJobs?.sortedByDescending {
                        it.originalDeadline
                    }
                }
                else if(history_sort_category.getItemAtPosition(position).equals("Price")){
                    finishedJobs = finishedJobs?.sortedByDescending{
                        it.originalPrice
                    }
                }
                convertToItems(finishedJobs!!)
                jobSection.update(items)
            }

            override fun onNothingSelected(parent: AdapterView<*>){

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirebaseUtil.removeListener(historyListenerRegistration);
        shouldInitRecyclerView = true;
    }

    private fun convertToItems(finishedJobs: List<FinishedJob>){
        var items = mutableListOf<Item>()
        finishedJobs.forEach{
            lateinit var temp: Item
            if(isClient) temp = PostedJobItem(it, this.activity!!)
            else temp = TakenJobItem(it, this.activity!!)

            items.add(temp)
        }

        this.items = items
    }

    private fun updateRecyclerView(finishedJobs: List<FinishedJob>){
        this.finishedJobs = finishedJobs
        convertToItems(this.finishedJobs!!)

        fun init(){
            history_recycler_view.apply {
                layoutManager = LinearLayoutManager(this@HistoryFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    jobSection = Section(items)
                    add(jobSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }
        fun updateItems() = jobSection.update(items)

        if(shouldInitRecyclerView) init()
        else updateItems()
    }

    private val onItemClick = OnItemClickListener{ item, view ->
        if(item is PostedJobItem){
            view.posted_job_post_review_button.setOnClickListener{
                GivingReviewActivity.userID = item.job.freelancer
                GivingReviewActivity.jobID = item.job.id
                GivingReviewActivity.view = item.viewHolder

                var intent = Intent(this@HistoryFragment.activity!!, GivingReviewActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Toast.makeText(this.context, HomeFragment.role, Toast.LENGTH_SHORT).show()
        if(System.last_activity != System.HISTORY_FRAGMENT){
            shouldInitRecyclerView = true
            if(HomeFragment.role.equals("client"))
                historyListenerRegistration = FinishedJobAdapter.initializePostedJobListener(this.activity!!, this::updateRecyclerView)
            else{
                isClient = false
                historyListenerRegistration = FinishedJobAdapter.initializeTakenJobListener(this.activity!!, this::updateRecyclerView)
            }
        }
        System.last_activity = System.HISTORY_FRAGMENT
    }

}
