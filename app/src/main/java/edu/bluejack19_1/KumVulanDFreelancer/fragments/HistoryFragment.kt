package edu.bluejack19_1.KumVulanDFreelancer.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import edu.bluejack19_1.KumVulanDFreelancer.R
import edu.bluejack19_1.KumVulanDFreelancer.System

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onResume() {
        super.onResume()

        System.last_activity = System.HISTORY_FRAGMENT
    }

}
