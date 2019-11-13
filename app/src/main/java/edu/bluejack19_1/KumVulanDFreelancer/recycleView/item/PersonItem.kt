package edu.bluejack19_1.KumVulanDFreelancer.recycleView.item

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.bluejack19_1.KumVulanDFreelancer.Firebase.FetchUser
import edu.bluejack19_1.KumVulanDFreelancer.R
import edu.bluejack19_1.KumVulanDFreelancer.System
import edu.bluejack19_1.KumVulanDFreelancer.User
import edu.bluejack19_1.KumVulanDFreelancer.firebaseDatabase
import edu.bluejack19_1.KumVulanDFreelancer.firebaseStorageReference
import kotlinx.android.synthetic.main.activity_about_disclaimer.*
import kotlinx.android.synthetic.main.fragment_account_freelancer.*
import kotlinx.android.synthetic.main.person_item_fragment.*
import java.lang.Exception


class PersonItem(val people: ChatPeople, private val context: Context)
    :Item(){
    lateinit var person : FetchUser
    override fun bind(viewHolder: ViewHolder, position: Int) {
        firebaseDatabase().collection("users").document(people.email + "")
                .get().addOnSuccessListener {
                    if (it.data != null) {
                        person = FetchUser(it.data as HashMap<String, Any>)
                        Log.d("firebase", "curr user initiated: ${it.data.toString()}")
                        System.last_activity = System.LOGIN_REGISTER_ACTIVITY
                        init(viewHolder)
                    }
                }
                .addOnFailureListener{
                    Log.d("PersonItem", "PersonItem Bind Error")
                }
    }

    private fun init(viewHolder: ViewHolder){

        viewHolder.textView_name.text = person.data["name"].toString();
        viewHolder.textView_last_message.text = people.last_message;

        try {
            firebaseStorageReference()
                    .child(person.getProfileImage())
                    .downloadUrl
                    .addOnSuccessListener{
                        uri -> Glide.with(context)
                            .load(uri)
                            .into(viewHolder.imageView_profile_picture)
                    }
                    .addOnFailureListener {
                        Log.d("firebase", it.toString())
                    }
        } catch (e: Exception) {
            Log.d("firebase", "invalid image loading intercepted")
        }
    }

    override fun getLayout() = R.layout.person_item_fragment

}