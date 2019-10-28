package com.example.kumvulandfreelancer.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.bluejack19_1.KumVulanDFreelancer.R
import edu.bluejack19_1.KumVulanDFreelancer.System

class JobsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jobs, container, false)
    }

    override fun onResume() {
        super.onResume()
        System.last_activity = System.JOB_FRAGMENT
    }
}