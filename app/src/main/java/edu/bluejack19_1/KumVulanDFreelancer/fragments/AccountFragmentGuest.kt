package edu.bluejack19_1.KumVulanDFreelancer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.bluejack19_1.KumVulanDFreelancer.MainActivity
import edu.bluejack19_1.KumVulanDFreelancer.R

class AccountFragmentGuest(parent: MainActivity): Fragment() {

    val parent = parent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_guest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}