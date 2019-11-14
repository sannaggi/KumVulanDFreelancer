package edu.bluejack19_1.KumVulanDFreelancer

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import edu.bluejack19_1.KumVulanDFreelancer.Firebase.FetchUser
import edu.bluejack19_1.KumVulanDFreelancer.recycleView.item.ChatPeople
import kotlinx.android.synthetic.main.activity_chat_page.*
import kotlinx.android.synthetic.main.person_item_fragment.*
import java.lang.Exception

class ChatPageActivity : AppCompatActivity() {

    companion object{
        lateinit var email: String
        lateinit var person: FetchUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        onFirstOpen()
    }

    private fun onFirstOpen(){
        initProfileImage()
        initProfileName()
    }

    private fun initProfileImage(){
        try {
            firebaseStorageReference()
                    .child(person.getProfileImage())
                    .downloadUrl
                    .addOnSuccessListener{
                        uri -> Glide.with(this)
                            .load(uri)
                            .into(chat_page_profile_image)
                    }
                    .addOnFailureListener {
                        Log.d("firebase", it.toString())
                    }
        } catch (e: Exception) {
            Log.d("firebase", "invalid image loading intercepted")
        }
    }

    private fun initProfileName(){
        findViewById<TextView>(R.id.chat_page_profile_name).text = person.getName()
    }
}
