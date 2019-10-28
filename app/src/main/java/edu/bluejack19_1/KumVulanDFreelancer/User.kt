package edu.bluejack19_1.KumVulanDFreelancer

import java.math.BigDecimal

class User {

    companion object {
        var data: Map<String, Any>? = emptyMap()

        val JOBS_DONE = "jobs_done"
        val PROFILE_IMAGE = "profile_image"
        val RATING = "rating"
        val NAME = "name"
        val ACADEMIC = "academic"
        val REVIEWS = "reviews"
        val REVIEW = "review"
        val SKILLS = "skills"
        val ABOUT = "about"
        val PROFILE_IMAGE_DIR = "/profile_images"

        fun getProfileImage(): String {
            return "${PROFILE_IMAGE_DIR}/${data?.get(PROFILE_IMAGE)}"
        }

        fun getName(): String {
            return data?.get(NAME).toString()
        }

        fun getJobsDone(): Int {
            return data?.get(JOBS_DONE).toString().toInt()
        }

        fun getRating(): BigDecimal {
            return data?.get(RATING).toString().toBigDecimal()
        }

        fun getSkills(): List<String> {
            return data?.get(SKILLS) as List<String>
        }

        fun getAbout(): String {
            return data?.get(ABOUT).toString()
        }

        fun getAcademicRecord(): String {
            return data?.get(ACADEMIC).toString()
        }

        fun getReviews(): ArrayList<Map<String, Any>> {
            return data?.get(REVIEWS) as ArrayList<Map<String, Any>>
        }

    }
}