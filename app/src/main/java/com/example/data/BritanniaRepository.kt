package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BritanniaRepository(private val dao: BritanniaDao) {

    val allStamps: Flow<List<PassportStamp>> = dao.getAllStamps()
    val allJournalEntries: Flow<List<JournalEntry>> = dao.getAllJournalEntries()
    val allSavedItineraries: Flow<List<SavedItinerary>> = dao.getAllSavedItineraries()

    suspend fun checkAndSeedDatabase() = withContext(Dispatchers.IO) {
        val existing = dao.getAllStamps().first()
        if (existing.isEmpty()) {
            val initialStamps = listOf(
                PassportStamp(
                    id = "yorkshire",
                    name = "Yorkshire Whitsun",
                    category = "County",
                    description = "The Historic Broad Acres. Moors, dales, dramatic abbey ruins, and coastal smuggling villages."
                ),
                PassportStamp(
                    id = "cornwall",
                    name = "Cornish Riviera",
                    category = "Coastline",
                    description = "Rugged granite cliffs, Celtic heritage, tin engine houses, and white sand sub-tropical coves."
                ),
                PassportStamp(
                    id = "lake_district",
                    name = "Lake District Fells",
                    category = "National Park",
                    description = "Majestic lakeland fells, high mountain passes, and literary trails of Wordsworth and Beatrix Potter."
                ),
                PassportStamp(
                    id = "highlands",
                    name = "Scottish Highlands",
                    category = "Scenic Region",
                    description = "Sweeping glens, historical battlefields, mysterious deep-water lochs, and jagged Caledonian peaks."
                ),
                PassportStamp(
                    id = "northumberland",
                    name = "Northumberland Border",
                    category = "County",
                    description = "Hadrian's Wall roman ramparts, isolated beach castles, and some of Europe's darkest starlit skies."
                ),
                PassportStamp(
                    id = "canterbury",
                    name = "Canterbury Cathedral",
                    category = "Cathedral",
                    description = "The mother-church and cathedral shrine of England, standing magnificent in Kentish brick since 597 AD."
                ),
                PassportStamp(
                    id = "settle_carlisle",
                    name = "Settle & Carlisle Line",
                    category = "Rail Route",
                    description = "Eighty breathtaking miles of Victorian high masonry viaducts and grand brick tunnels."
                ),
                PassportStamp(
                    id = "bath",
                    name = "Georgian Bath Spa",
                    category = "Roman Heritage",
                    description = "Harmonious neoclassical golden limestone crescent streets resting above thermal spring-fed ancient baths."
                )
            )
            dao.insertStamps(initialStamps)
        }
    }

    suspend fun unlockStamp(id: String) = withContext(Dispatchers.IO) {
        dao.updateStampUnlockStatus(id, true, System.currentTimeMillis())
    }

    suspend fun lockStamp(id: String) = withContext(Dispatchers.IO) {
        dao.updateStampUnlockStatus(id, false, null)
    }

    suspend fun addJournalEntry(entry: JournalEntry) = withContext(Dispatchers.IO) {
        dao.insertJournalEntry(entry)
    }

    suspend fun deleteJournalEntry(entry: JournalEntry) = withContext(Dispatchers.IO) {
        dao.deleteJournalEntry(entry)
    }

    suspend fun saveItinerary(itinerary: SavedItinerary) = withContext(Dispatchers.IO) {
        dao.insertItinerary(itinerary)
    }

    suspend fun deleteItinerary(itinerary: SavedItinerary) = withContext(Dispatchers.IO) {
        dao.deleteItinerary(itinerary)
    }
}
