package edu.bluejack19_1.KumVulanDFreelancer.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.bluejack19_1.KumVulanDFreelancer.*
import edu.bluejack19_1.KumVulanDFreelancer.Firebase.FirebaseUtil
import edu.bluejack19_1.KumVulanDFreelancer.recycleView.item.PersonItem
import kotlinx.android.synthetic.main.fragment_people.*

class PeopleFragment(parent: MainActivity) : Fragment() {

    private val parent = parent

    private lateinit var userListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var peopleSection: Section

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        userListenerRegistration = FirebaseUtil.addChatPeopleListener(this.activity!!, this::updateRecyclerView)

        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirebaseUtil.removeListener(userListenerRegistration);
        shouldInitRecyclerView = true;
    }

    private fun updateRecyclerView(items: List<Item>){
        fun init(){
            recycler_view_people.apply {
                layoutManager = LinearLayoutManager(this@PeopleFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }
        fun updateItems() = peopleSection.update(items)

        if(shouldInitRecyclerView) init()
        else updateItems()
    }

    private val onItemClick = OnItemClickListener{ item, view ->
        if(item is PersonItem){
            ChatPageActivity.chat_id = item.people.chat_id
            ChatPageActivity.person = item.person
            ChatPageActivity.userDocRef = firebaseDatabase().collection("users").document(firebaseAuth().currentUser?.email + "")
            ChatPageActivity.otherDocRef = firebaseDatabase().collection("users").document(item.people.email)
            ChatPageActivity.chatPeople = item.people

            var intent = Intent(this.context, ChatPageActivity::class.java)
            startActivity(intent)
        }
    }

}
