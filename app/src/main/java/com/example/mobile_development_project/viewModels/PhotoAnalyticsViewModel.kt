package com.example.mobile_development_project.viewModels

import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.DailyMetrics
import com.example.mobile_development_project.data.models.Session
import com.example.mobile_development_project.data.models.UploadedImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.example.mobile_development_project.data.models.DailyImageMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class PhotoAnalyticsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    val dailyMetrics = MutableStateFlow<Map<LocalDate, DailyMetrics>>(emptyMap())
    private var startTime: Long = 0

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

        db.collection("sessions").add(session)
    }

    val totalUsers = MutableStateFlow(0)
    private var usersListener: ListenerRegistration? = null

    fun observeUsers() {
        usersListener?.remove()
        usersListener = db.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                totalUsers.value = snapshot.size()
            }
    }

    fun observeSessions() {
        db.collection("sessions")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val sessions = snapshot.documents.mapNotNull { doc ->
                    val userId = doc.getString("userId") ?: return@mapNotNull null
                    val duration = doc.getLong("durationSeconds") ?: return@mapNotNull null
                    val timestamp = doc.getLong("timestamp") ?: return@mapNotNull null

                    Session(userId, duration, timestamp)
                }

                dailyMetrics.update {
                    calculateDailyMetrics(sessions)
                }
            }
    }

    val imageMetrics = MutableStateFlow<Map<LocalDate, DailyImageMetrics>>(emptyMap())

    fun observeImages() {
        db.collection("locations")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val images = snapshot.documents.flatMap { doc ->
                    val userId = doc.getString("ownerId") ?: return@flatMap emptyList()
                    val imageUrls = doc.get("imageUrls") as? List<String> ?: emptyList()
                    val createdAt = doc.getString("createdAt") ?: return@flatMap emptyList()

                    val timestamp = parseFinnishDateToMillis(createdAt)

                    imageUrls.map {
                        UploadedImage(
                            userId = userId,
                            timestamp = timestamp,
                            sizeBytes = 0L
                        )
                    }
                }

                imageMetrics.update {
                    calculateImageMetrics(images)
                }
            }
    }

    private fun parseFinnishDateToMillis(dateString: String): Long {
        return try {
            val formatter = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val localDateTime = java.time.LocalDateTime.parse(dateString, formatter)

            localDateTime
                .atZone(ZoneId.of("Europe/Helsinki"))
                .toInstant()
                .toEpochMilli()
        } catch (e: Exception) {
            0L
        }
    }

    fun loadData() {
        observeUsers()
        observeSessions()
        observeImages()
    }

    private fun calculateDailyMetrics(
        sessions: List<Session>
    ): Map<LocalDate, DailyMetrics> {

        val grouped = sessions.groupBy {
            Instant.ofEpochMilli(it.timestamp)
                .atZone(ZoneId.of("Europe/Helsinki"))
                .toLocalDate()
        }

        val today = LocalDate.now()
        val days = (0..6).map { today.minusDays(it.toLong()) }.reversed()

        return days.associateWith { date ->
            val daySessions = grouped[date] ?: emptyList()

            val activeUsers = daySessions.map { it.userId }.distinct().size
            val avgDuration = if (daySessions.isNotEmpty())
                daySessions.map { it.durationSeconds.toDouble() }.average()
            else 0.0

            DailyMetrics(
                activeUsers = activeUsers,
                avgSessionDuration = avgDuration,
                totalSessions = daySessions.size,
                totalUsers = totalUsers.value
            )
        }
    }

    private fun calculateImageMetrics(
        images: List<UploadedImage>
    ): Map<LocalDate, DailyImageMetrics> {

        val grouped = images.groupBy {
            Instant.ofEpochMilli(it.timestamp)
                .atZone(ZoneId.of("Europe/Helsinki"))
                .toLocalDate()
        }

        val today = LocalDate.now()
        val days = (0..6).map { today.minusDays(it.toLong()) }.reversed()

        return days.associateWith { date ->
            val dayImages = grouped[date] ?: emptyList()

            val users = dayImages.map { it.userId }.distinct().size
            val count = dayImages.size

            val avgPerUser =
                if (users > 0) count.toDouble() / users else 0.0

            val totalSizeMb =
                dayImages.sumOf { it.sizeBytes }.toDouble() / (1024 * 1024)

            DailyImageMetrics(
                uploadedImages = count,
                activeUsers = users,
                avgImagesPerUser = avgPerUser,
                totalSizeMb = totalSizeMb
            )
        }
    }
}