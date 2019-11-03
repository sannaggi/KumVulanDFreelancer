package edu.bluejack19_1.KumVulanDFreelancer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login_register.*

class LoginRegisterActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 9001

    private fun getGoogleSignInClient() : GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        googleLoginBtn.setOnClickListener{
            val signInIntent = getGoogleSignInClient().signInIntent;
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("firebase", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("firebase", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("firebase", "signInWithCredential:success")
                    val currentUser = firebaseAuth().currentUser
                    Log.d("firebase", currentUser.toString())

                    firebaseDatabase().collection("users")
                        .document(currentUser!!.email + "")
                        .get().addOnSuccessListener {
                            if (it.data != null) {
                                User.data = it.data as HashMap<String, Any>
                                Log.d("firebase", "curr user initiated: ${it.data.toString()}")
                                finish()
                                System.last_activity = System.LOGIN_REGISTER_ACTIVITY
                            } else {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("New User")
                                builder.setMessage("Your email hasn't been registered yet! Do you want to register?")
                                    .setCancelable(true)
                                    .setPositiveButton("YES") { dialog, which ->
                                        val intent = Intent(this, RegisterClientActivity::class.java)
                                        startActivity(intent)
                                        System.last_activity = System.LOGIN_REGISTER_ACTIVITY
                                        finish()
                                    }
                                    .setNegativeButton("NO"){dialog, which ->  }
                                builder.create().show()
                            }
                        }
                }
            }
    }

}
