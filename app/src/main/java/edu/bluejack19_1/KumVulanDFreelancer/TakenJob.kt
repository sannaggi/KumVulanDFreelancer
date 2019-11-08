package edu.bluejack19_1.KumVulanDFreelancer

class TakenJob(name: String, client: String, deadline: String, description: String, est_price: Int, freelancer: String, status: String) {
    val name = name
    val client = client
    val deadline = deadline
    val description = description
    val est_price = est_price
    val freelancer = freelancer
    val status = status

    companion object {
        val NAME = "name"
        val CLIENT = "client"
        val DEADLINE = "deadline"
        val DESCRIPTION = "description"
        val EST_PRICE = "est_price"
        val FREELANCER = "freelancer"
        val STATUS = "status"
    }
}