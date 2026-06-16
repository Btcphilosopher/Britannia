package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passport_stamps")
data class PassportStamp(
    @PrimaryKey val id: String, // e.g., "yorkshire", "cornwall"
    val name: String,
    val category: String, // e.g., "County", "Scenic Route", "National Park"
    val description: String,
    val isUnlocked: Boolean = false,
    val unlockDate: Long? = null,
    val imageUrl: String = "" // Placeholder or empty
)

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val location: String,
    val date: Long = System.currentTimeMillis(),
    val tag: String = "" // e.g., "Rail", "Cathedral", "Pub", "Countryside"
)

@Entity(tableName = "saved_itineraries")
data class SavedItinerary(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val duration: String,
    val stops: String, // Comma-separated destinations, e.g., "Canterbury, York, Durham"
    val railRouteName: String,
    val hotelName: String,
    val activities: String, // Rich description
    val createdAt: Long = System.currentTimeMillis()
)
