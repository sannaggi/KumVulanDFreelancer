package edu.bluejack19_1.KumVulanDFreelancer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldValue
import edu.bluejack19_1.KumVulanDFreelancer.firebase.ChatPeople
import edu.bluejack19_1.KumVulanDFreelancer.firebase.FetchUser
import kotlinx.android.synthetic.main.activity_account_client.*
import java.lang.Exception

class AccountActivityClient : AppCompatActivity() {

    lateinit var userID: String
    lateinit var person : FetchUser
    lateinit var chat_id : String
    lateinit var chatPeople : ChatPeople

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_client)

        fetchIntentData()
        loadProfileDatas()
        initializeChatButton()
    }

    private fun initializeChatButton(){
        chat_button.setOnClickListener{
            getIdAndChat()

            ChatPageActivity.chat_id = chat_id
            Log.d("AccountActivityClient", "${chat_id}")
            ChatPageActivity.person = person
            ChatPageActivity.userDocRef = firebaseDatabase().collection("users").document(firebaseAuth().currentUser?.email + "")
            ChatPageActivity.otherDocRef = firebaseDatabase().collection("users").document(userID)
            ChatPageActivity.chatPeople = chatPeople

            var intent = Intent(this, ChatPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun generateNewIdAndChat(){
        var forUpdate = HashMap<String, Any>()
        forUpdate.put("chat_people", FieldValue.arrayUnion())
        firebaseDatabase().collection("chat_messages").add(forUpdate).addOnSuccessListener {
            chat_id = it.id
            Log.d("AccountActivityClient", "${it.id}")
        }

        chatPeople = ChatPeople(
                chat_id,
                userID,
                false,
                false,
                ""
        )
    }

    private fun getIdAndChat(){
        var chatPeopleList = User.getChatPeople()
        if(chatPeopleList.isNullOrEmpty()){
            generateNewIdAndChat()
            return
        }

        var isFound = false

        chatPeopleList!!.forEach {
            if(it["email"].toString() == userID){
                isFound = true
                chat_id = it["id"].toString()
                chatPeople = ChatPeople(
                        chat_id,
                        userID,
                        it["archive"] as Boolean,
                        it["starred"] as Boolean,
                        it["last_message"].toString()
                )
            }
        }

        if(!isFound) generateNewIdAndChat()
    }

    private fun fetchIntentData() {
        userID = intent.extras!!.get("ID").toString()
    }

    private fun loadName(name: String) {
        txtName.text = name
    }

    private fun loadProfileImage(imagePath: String) {
        try {
            firebaseStorageReference()
                .child(imagePath)
                .downloadUrl
                .addOnSuccessListener{
                        uri -> Glide.with(this)
                    .load(uri)
                    .into(profileImage)
                }
                .addOnFailureListener {
                    Log.d("firebase", it.toString())
                }
        } catch (e: Exception) {
            Log.d("firebase", "invalid image loading intercepted")
        }
    }

    private fun loadAbout(about: String) {
        txtAbout.text = about
    }

    private fun loadProfileDatas() {
        firebaseDatabase()
            .collection("users")
            .document(userID)
            .get()
            .addOnSuccessListener {
                disableLoading()
                showDatas(it.data as HashMap<String, Any>)
            }
    }

    private fun disableLoading() {
        progress_circular.visibility = View.GONE
        container.visibility = View.VISIBLE

        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        bigContainer.layoutParams = params
    }

    private fun showDatas(data: HashMap<String, Any>) {
        person = FetchUser(data)
        loadProfileImage(User.getProfileImagePath(data.get(User.PROFILE_IMAGE).toString()))
        loadName(data.get(User.NAME).toString())
        loadAbout(data.get(User.ABOUT).toString())
    }
}
