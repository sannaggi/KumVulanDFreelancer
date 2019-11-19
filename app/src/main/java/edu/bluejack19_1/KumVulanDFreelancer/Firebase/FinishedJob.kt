package edu.bluejack19_1.KumVulanDFreelancer.firebase

import edu.bluejack19_1.KumVulanDFreelancer.TakenJob
import edu.bluejack19_1.KumVulanDFreelancer.firebaseDatabase
import java.sql.Timestamp
import java.text.SimpleDateFormat



class FinishedJob(data: HashMap<String, Any>){
    val name = string(data[TakenJob.NAME])
    val client = string(data[TakenJob.CLIENT])
    val deadline = dateFormat.format(data[TakenJob.DEADLINE])
    val description = string(data[TakenJob.DESCRIPTION])
    val price = convertPrice(data[TakenJob.EST_PRICE] as Int)
    val originalPrice = data[TakenJob.EST_PRICE] as Int
    val freelancer = string(data[TakenJob.FREELANCER])
    val originalDeadline = data[TakenJob.DEADLINE] as Timestamp
    val status = string(data[TakenJob.STATUS])
    var isReviewed = data["reviewed"] as Boolean?
    var isRated = data["rated"] as Boolean?
    val clientName = fetchName(client)
    val freelancerName = fetchName(freelancer)

    companion object {
        val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)

        private fun string(item: Any?) : String{
            return item.toString()
        }

        private fun convertPrice(price: Int) : String{
            var price = price
            var str = ""
            var count = 0
            while(price > 0){
                if(count == 3) str += "."
                str += (price % 10).toString()
                price /= 10
            }
            return str.reversed()
        }

        private fun fetchName(email: String) : String{
            var name = ""
            firebaseDatabase().collection("users")
                    .document(email + "")
                    .get().addOnSuccessListener{
                        if(it.data != null){
                            name = it.data?.get("name").toString()
                        }
            }

            return name
        }
    }
}