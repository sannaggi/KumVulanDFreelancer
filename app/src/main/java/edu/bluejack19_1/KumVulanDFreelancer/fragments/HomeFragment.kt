package edu.bluejack19_1.KumVulanDFreelancer.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.bluejack19_1.KumVulanDFreelancer.LoginRegisterActivity
import edu.bluejack19_1.KumVulanDFreelancer.R
import kotlinx.android.synthetic.main.fragment_home_guest.*

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_guest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginRegisterBtn.setOnClickListener{
            startActivity(Intent(this.context, LoginRegisterActivity::class.java))
        }
    }
}