package com.example.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.api.GeminiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AiState {
    object Idle : AiState()
    object Loading : AiState()
    data class Success(val response: String) : AiState()
    data class Error(val message: String) : AiState()
}

class BritanniaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = BritanniaRepository(db.dao())

    // ---- Reactive State Flows ----
    val passportStamps: StateFlow<List<PassportStamp>> = repository.allStamps
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val journalEntries: StateFlow<List<JournalEntry>> = repository.allJournalEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savedItineraries: StateFlow<List<SavedItinerary>> = repository.allSavedItineraries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // UI state for search
    val searchQuery = MutableStateFlow("")
    val selectedRegionFilter = MutableStateFlow("All Regions")
    val selectedEraFilter = MutableStateFlow("All Eras")

    // AI Concierge state
    private val _aiState = MutableStateFlow<AiState>(AiState.Idle)
    val aiState: StateFlow<AiState> = _aiState

    init {
        // Seed database with predefined stamps if empty
        viewModelScope.launch {
            try {
                repository.checkAndSeedDatabase()
            } catch (e: Exception) {
                Log.e("BritanniaViewModel", "Seeding database failed", e)
            }
        }
    }

    // ---- Functions ----
    fun unlockPassportStamp(id: String) {
        viewModelScope.launch {
            repository.unlockStamp(id)
        }
    }

    fun relockPassportStamp(id: String) {
        viewModelScope.launch {
            repository.lockStamp(id)
        }
    }

    fun addJournal(title: String, content: String, location: String, tag: String) {
        viewModelScope.launch {
            val entry = JournalEntry(
                title = title,
                content = content,
                location = location,
                tag = tag
            )
            repository.addJournalEntry(entry)
        }
    }

    fun deleteJournal(entry: JournalEntry) {
        viewModelScope.launch {
            repository.deleteJournalEntry(entry)
        }
    }

    fun saveCreatedItinerary(title: String, duration: String, stops: String, railRoute: String, hotel: String, activities: String) {
        viewModelScope.launch {
            val itinerary = SavedItinerary(
                title = title,
                duration = duration,
                stops = stops,
                railRouteName = railRoute,
                hotelName = hotel,
                activities = activities
            )
            repository.saveItinerary(itinerary)
        }
    }

    fun removeItinerary(itinerary: SavedItinerary) {
        viewModelScope.launch {
            repository.deleteItinerary(itinerary)
        }
    }

    // ---- AI Travel Concierge Logic ----
    fun askAIConcierge(userPrompt: String) {
        if (userPrompt.isBlank()) return
        
        _aiState.value = AiState.Loading

        viewModelScope.launch {
            val systemDocPrompt = """
                You are the BRITANNIA Digital Travel Concierge, a highly refined, eloquent, and sophisticated AI guide designed to inspire wanderlust in Great Britain.
                Your tone matches National Geographic, luxury travel journals, and the golden age of British Rail (patriotic, confident, literary, yet forward-looking and modern — Anglo-Futurist).
                
                Guidelines:
                1. Provide deeply immersive, tailored recommendations. If the user likes architecture or railways, reference specific British eras (like Gothic, Victorian, or Anglo-Futurist) and grand rail networks (such as the LNER East Coast Main Line or Settle-Carlisle).
                2. When appropriate, write structured, bulleted itineraries, listing counties, castles, pubs, and hotels.
                3. Keep answers concise, beautiful, and inspiring. Use terms that celebrate British civilization, culture, and heavy engineering.
                4. End your reply with a summary line: "Based on your interests, [City 1] and [City 2] should be your next destinations." or "Consider a [Duration] [Region/Theme] itinerary."
                
                Format with markdown headlines for easy visual scanning on mobile screens.
            """.trimIndent()

            val result = GeminiClient.generateRecommendation(userPrompt, systemDocPrompt)
            if (result.startsWith("Error")) {
                _aiState.value = AiState.Error(result)
            } else {
                _aiState.value = AiState.Success(result)
            }
        }
    }

    // Clear AI responses
    fun resetAIConcierge() {
        _aiState.value = AiState.Idle
    }
}
