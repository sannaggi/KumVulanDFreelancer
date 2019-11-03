package edu.bluejack19_1.KumVulanDFreelancer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.kumvulandfreelancer.Fragments.JobsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.bluejack19_1.KumVulanDFreelancer.Fragments.AccountFragmentClient
import edu.bluejack19_1.KumVulanDFreelancer.Fragments.AccountFragmentFreelancer
import edu.bluejack19_1.KumVulanDFreelancer.Fragments.HomeFragmentGuest
import edu.bluejack19_1.KumVulanDFreelancer.fragments.AccountFragmentGuest
import edu.bluejack19_1.KumVulanDFreelancer.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: Fragment
    private lateinit var jobsFragment: JobsFragment
    private lateinit var accountFragment: Fragment

    fun logout() {
        accountFragment = AccountFragmentGuest(this)
        homeFragment = HomeFragmentGuest(this)
        addFragment(accountFragment)
    }

    fun loginFromAccount() {
        accountFragment = if (User.getRole() == User.CLIENT) {
            AccountFragmentClient(this)
        } else {
            AccountFragmentFreelancer(this)
        }

        homeFragment = HomeFragment(this)
        addFragment(accountFragment)
    }

    fun loginFromHome() {
        accountFragment = if (User.getRole() == User.CLIENT) {
            AccountFragmentClient(this)
        } else {
            AccountFragmentFreelancer(this)
        }

        homeFragment = HomeFragment(this)
        addFragment(homeFragment)
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
            .replace(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId) {
            R.id.nav_home -> {
                addFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_jobs -> {
                addFragment(jobsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_account -> {
                if(!::accountFragment.isInitialized)
                    return@OnNavigationItemSelectedListener true
                addFragment(accountFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener(navListener)

        if(User.data!!.isNotEmpty()) {
            accountFragment =  if (User.getRole() == User.CLIENT) {
                AccountFragmentClient(this)
            } else {
                AccountFragmentFreelancer(this)
            }
            homeFragment = HomeFragment(this)
        } else {
            accountFragment = AccountFragmentGuest(this)
            homeFragment = HomeFragmentGuest(this)
        }

        jobsFragment = JobsFragment()

        addFragment(homeFragment)
    }
}
