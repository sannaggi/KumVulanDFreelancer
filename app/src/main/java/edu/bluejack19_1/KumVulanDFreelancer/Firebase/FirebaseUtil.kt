package edu.bluejack19_1.KumVulanDFreelancer.Firebase

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import edu.bluejack19_1.KumVulanDFreelancer.firebaseAuth
import edu.bluejack19_1.KumVulanDFreelancer.firebaseDatabase
import edu.bluejack19_1.KumVulanDFreelancer.recycleView.item.ChatPeople
import edu.bluejack19_1.KumVulanDFreelancer.recycleView.item.PersonItem

object FirebaseUtil {

    private val currentUserDocRef: DocumentReference
            = firebaseDatabase().collection("users").document(firebaseAuth().currentUser?.email.toString())
    private lateinit var chatPeopleListenerRegistration : ListenerRegistration;

    fun addChatPeopleListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
        return currentUserDocRef.addSnapshotListener{
            snapshots, e ->
            if(e != null){
                Log.e("FIRESTORE", "Chat People Listener error", e)
                return@addSnapshotListener
            }
            val items = snapshots!!["chat_people"] as ArrayList<Map<String, Any>>
            var retItems = mutableListOf<Item>()
            items.forEach {
                val temp : ChatPeople = ChatPeople(
                        it["email"].toString(),
                        it["isArchive"] as Boolean,
                        it["isStarred"] as Boolean,
                        it["last_message"].toString())
                retItems.add(PersonItem(temp, context))
            }
            onListen(retItems);
        }
    }

    fun removeListener(listener: ListenerRegistration) = listener.remove()
}