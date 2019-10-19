package edu.bluejack19_1.KumVulanDFreelancer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.kumvulandfreelancer.Fragments.JobsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.bluejack19_1.KumVulanDFreelancer.Fragments.AccountFragment
import edu.bluejack19_1.KumVulanDFreelancer.Fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var jobsFragment: JobsFragment
    private lateinit var accountFragment: AccountFragment

    override fun onStart() {
        super.onStart()

        val currentUser = firebaseAuth().currentUser

        if(currentUser != null) {
            firebaseDatabase().collection("users")
                .document(currentUser.email + "")
                .get().addOnSuccessListener {
                    User.data = it.data
                    Log.d("firebase", "curr user initiated: ${it.data.toString()}")
                }
        } else {
            Log.d("firebase", "current user null")
        }
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
                if(!::jobsFragment.isInitialized) {
                    jobsFragment = JobsFragment()
                }
                addFragment(jobsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_account -> {
                if(!::accountFragment.isInitialized) {
                    accountFragment = AccountFragment()
                }
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

        homeFragment = HomeFragment()
        addFragment(homeFragment)


    }
}
