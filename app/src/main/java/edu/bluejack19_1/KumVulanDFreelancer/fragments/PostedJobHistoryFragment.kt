package edu.bluejack19_1.KumVulanDFreelancer.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.bluejack19_1.KumVulanDFreelancer.R

/**
 * A simple [Fragment] subclass.
 */
class PostedJobHistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posted_job_history, container, false)
    }


}
