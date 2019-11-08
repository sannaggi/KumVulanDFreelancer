package edu.bluejack19_1.KumVulanDFreelancer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import edu.bluejack19_1.KumVulanDFreelancer.MainActivity
import edu.bluejack19_1.KumVulanDFreelancer.R
import edu.bluejack19_1.KumVulanDFreelancer.User
import edu.bluejack19_1.KumVulanDFreelancer.firebaseUser
import kotlinx.android.synthetic.main.fragment_home.*

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


}