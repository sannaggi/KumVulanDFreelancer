package edu.bluejack19_1.KumVulanDFreelancer.recycleView.item

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.bluejack19_1.KumVulanDFreelancer.Firebase.ChatPeople
import edu.bluejack19_1.KumVulanDFreelancer.Firebase.FetchUser
import edu.bluejack19_1.KumVulanDFreelancer.R
import edu.bluejack19_1.KumVulanDFreelancer.System
import edu.bluejack19_1.KumVulanDFreelancer.firebaseDatabase
import edu.bluejack19_1.KumVulanDFreelancer.firebaseStorageReference
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

        viewHolder.textView_name.text = person.getName();
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
