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
import edu.bluejack19_1.KumVulanDFreelancer.R
import edu.bluejack19_1.KumVulanDFreelancer.User
import edu.bluejack19_1.KumVulanDFreelancer.firebaseStorageReference
import kotlinx.android.synthetic.main.activity_about_disclaimer.*
import kotlinx.android.synthetic.main.fragment_account_freelancer.*
import kotlinx.android.synthetic.main.person_item_fragment.*
import java.lang.Exception


class PersonItem(private val context: Context)
    :Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_name.text = User.getName();
        viewHolder.textView_last_message.text = "last message";

        try {
            firebaseStorageReference()
                    .child(User.getProfileImage())
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
