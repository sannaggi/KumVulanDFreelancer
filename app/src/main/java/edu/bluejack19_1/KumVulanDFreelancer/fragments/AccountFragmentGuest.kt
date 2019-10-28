package edu.bluejack19_1.KumVulanDFreelancer.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.bluejack19_1.KumVulanDFreelancer.LoginRegisterActivity
import edu.bluejack19_1.KumVulanDFreelancer.MainActivity
import edu.bluejack19_1.KumVulanDFreelancer.R
import edu.bluejack19_1.KumVulanDFreelancer.System
import kotlinx.android.synthetic.main.fragment_account_guest.*

class AccountFragmentGuest(parent: MainActivity): Fragment() {

    val parent = parent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_guest, container, false)
    }

    override fun onResume() {
        super.onResume()

        if(System.last_activity == System.LOGIN_REGISTER_ACTIVITY) {
            parent.loginFromAccount()
        }

        System.last_activity = System.ACCOUNT_FRAGMENT_GUEST
    }

    fun initializeLoginButton() {
        btnLoginRegister.setOnClickListener {
            val intent = Intent(this.context, LoginRegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeLoginButton()
    }

}