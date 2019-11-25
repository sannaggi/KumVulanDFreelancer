package edu.bluejack19_1.KumVulanDFreelancer.recycleView.item

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.bluejack19_1.KumVulanDFreelancer.R
import edu.bluejack19_1.KumVulanDFreelancer.Review
import edu.bluejack19_1.KumVulanDFreelancer.User
import edu.bluejack19_1.KumVulanDFreelancer.firebaseStorageReference
import kotlinx.android.synthetic.main.review.*
import java.lang.Exception

class ReviewItem (var review: Review, val context: Context) : Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.txtName.text = review.name
        viewHolder.txtRating.text = review.rating.toString() + "★"
        viewHolder.txtReview.text = review.review
        Log.d("ReviewItem", review.review)

        loadProfileImage(viewHolder)
    }

    private fun loadProfileImage(viewHolder: ViewHolder){
        try {
            firebaseStorageReference()
                    .child(User.getProfileImagePath(review.profile_image))
                    .downloadUrl
                    .addOnSuccessListener{
                        uri -> Glide.with(context)
                            .load(uri)
                            .into(viewHolder.imgProfile)
                    }
                    .addOnFailureListener {
                        Log.d("firebase", it.toString())
                    }
        } catch (e: Exception) {
            Log.d("firebase", "invalid image loading intercepted")
        }
    }

    override fun getLayout(): Int = R.layout.review

}