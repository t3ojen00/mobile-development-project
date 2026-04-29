package com.example.mobile_development_project.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.DailyMetrics
import com.example.mobile_development_project.data.models.Session
import com.example.mobile_development_project.helpers.deleteMockSessions
import com.example.mobile_development_project.helpers.mockSessions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class AnalyticsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val dailyMetrics = MutableStateFlow<Map<LocalDate, DailyMetrics>>(emptyMap())
    private var startTime: Long = 0
    val totalUsers = MutableStateFlow(0)
    private var usersListener: ListenerRegistration? = null
    fun startSession() {
        startTime = System.currentTimeMillis()
    }

    fun endSession(userId: String) {
        val duration = (System.currentTimeMillis() - startTime) / 1000

        val session = mapOf(
            "userId" to userId,
            "durationSeconds" to duration,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("sessions")
            .add(session)
    }
    fun observeUsers() {
        usersListener?.remove()
        usersListener = db.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                totalUsers.value = snapshot.size()
                Log.d("AnalyticsViewModel", "Users observed: ${totalUsers.value}")
            }
    }
    fun observeSessions() {
        db.collection("sessions")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val sessions = snapshot.documents.mapNotNull { doc ->
                    val userId = doc.getString("userId") ?: return@mapNotNull null
                    val durationSeconds = doc.getLong("durationSeconds") ?: return@mapNotNull null
                    val timestamp = doc.getLong("timestamp") ?: return@mapNotNull null
                    Session(
                        userId,
                        durationSeconds,
                        timestamp
                    )
                }
                dailyMetrics.update {
                    calculateDailyMetrics(sessions)
                }
                    Log.d("AnalyticsViewModel", "Sessions observed: ${sessions.size}")
            }
    }
    fun generateMockData() {
        mockSessions()
    }
    fun clearMockData() {
        deleteMockSessions()
    }
    fun loadData() {
        observeUsers()
        observeSessions()
    }
    private fun calculateDailyMetrics(
        sessions: List<Session>
    ): Map<LocalDate, DailyMetrics> {

        val validSessions = sessions.filter { it.timestamp > 0 }

        // group sessions by date
        val grouped = validSessions.groupBy { session ->
            Instant.ofEpochMilli(session.timestamp)
                .atZone(ZoneId.of("Europe/Helsinki"))
                .toLocalDate()
        }

        // chart displays last 7 days
        val today = LocalDate.now(ZoneId.of("Europe/Helsinki"))
        val last7Days = (0..6)
            .map { today.minusDays(it.toLong()) }
            .reversed()

        // build metrics for each day
        return last7Days.associateWith { date ->

            val daySessions = grouped[date] ?: emptyList()

            val activeUsers = daySessions
                .map { it.userId }
                .distinct()
                .size

            val totalSessions = daySessions.size

            val avgDuration = if (daySessions.isNotEmpty()) {
                daySessions.map { it.durationSeconds.toDouble() }.average()
            } else 0.0

            DailyMetrics(
                activeUsers = activeUsers,
                avgSessionDuration = avgDuration,
                totalSessions = totalSessions,
                totalUsers = totalUsers.value
            )
        }
    }
}