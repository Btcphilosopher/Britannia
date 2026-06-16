package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BritanniaDao {
    // ---- Passport Stamps ----
    @Query("SELECT * FROM passport_stamps ORDER BY name ASC")
    fun getAllStamps(): Flow<List<PassportStamp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStamps(stamps: List<PassportStamp>)

    @Query("UPDATE passport_stamps SET isUnlocked = :unlocked, unlockDate = :date WHERE id = :id")
    suspend fun updateStampUnlockStatus(id: String, unlocked: Boolean, date: Long?)

    // ---- Journal Entries ----
    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteJournalEntry(entry: JournalEntry)

    // ---- Saved Itineraries ----
    @Query("SELECT * FROM saved_itineraries ORDER BY createdAt DESC")
    fun getAllSavedItineraries(): Flow<List<SavedItinerary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItinerary(itinerary: SavedItinerary)

    @Delete
    suspend fun deleteItinerary(itinerary: SavedItinerary)
}
