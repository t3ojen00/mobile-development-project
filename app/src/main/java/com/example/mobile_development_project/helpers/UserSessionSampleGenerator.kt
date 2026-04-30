package com.example.mobile_development_project.helpers

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId

fun mockSessions() {

    val users = listOf("user1", "user2", "user3", "user4", "user5", "user6")
    val zone = ZoneId.of("Europe/Helsinki")
    val today = LocalDate.now(zone)

    // generates list of past 6 days (excluding today)
    val baseDates = (0..5).map { offset ->
        today.minusDays(offset.toLong() + 1)
    }.reversed()

    val random = kotlin.random.Random(55)

    baseDates.forEach { date ->
        val sessionsPerDay = random.nextInt(1, 15) // random number of sessions per day

        repeat(sessionsPerDay) {
            val userId = users.random()

            val seconds = random.nextInt(60, 60 * 60) // random session between 1min and 1 h

            val timestamp = date
                .atTime(
                    random.nextInt(6, 24),
                    random.nextInt(0, 59)
                )
                .atZone(zone)
                .toInstant()
                .toEpochMilli()
            // session object with random values
            val session = mapOf(
                "userId" to userId,
                "durationSeconds" to seconds,
                "timestamp" to timestamp
            )

            FirebaseFirestore.getInstance()
                .collection("sessions")
                .add(session)
        }
    }
}

fun deleteMockSessions() {
    val db = FirebaseFirestore.getInstance()
    // same mock users used to identify test data for removal
    val users = listOf("user1", "user2", "user3", "user4", "user5", "user6")

    db.collection("sessions")
        .whereIn("userId", users)
        .get()
        .addOnSuccessListener { snapshot ->

            if (snapshot.isEmpty) {
                Log.d("CLEAR", "no mock sessions found")
                return@addOnSuccessListener
            }
            // batch delete for efficiency
            val batch = db.batch()

            snapshot.documents.forEach {
                batch.delete(it.reference)
            }

            batch.commit().addOnSuccessListener {
                Log.d("CLEAR", "batch deleted")

                deleteMockSessions()
            }
        }
}