package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BritanniaApp(viewModel: BritanniaViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current

    // Observe Room DB State
    val stamps by viewModel.passportStamps.collectAsStateWithLifecycle()
    val journalEntries by viewModel.journalEntries.collectAsStateWithLifecycle()
    val savedItineraries by viewModel.savedItineraries.collectAsStateWithLifecycle()

    // Screen sheets or details state
    var activeDestinationIdForDetail by remember { mutableStateOf<String?>(null) }
    var activeRailJourneyIdForDetail by remember { mutableStateOf<String?>(null) }
    var activeGrandTourIdForDetail by remember { mutableStateOf<String?>(null) }
    var showAddJournalDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("britannia_main_scaffold"),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Britannia Guide Logo",
                            tint = AntiqueBrass
                        )
                        Text(
                            text = "BRITANNIA",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 4.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            Toast.makeText(context, "Britannia Premium Member Mode Active", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("crown_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Club Status",
                            tint = AntiqueBrass
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                val navItems = listOf(
                    NavigationItem("Home", Icons.Default.Home, "home_tab"),
                    NavigationItem("Atlas", Icons.Default.Place, "atlas_tab"),
                    NavigationItem("Rail", Icons.Default.Build, "rail_tab"),
                    NavigationItem("Heritage", Icons.Default.List, "heritage_tab"),
                    NavigationItem("My UK", Icons.Default.AccountBox, "my_uk_tab")
                )

                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        label = { Text(item.label, fontSize = 11.sp) },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MutedSlate,
                            unselectedTextColor = MutedSlate
                        ),
                        modifier = Modifier.testTag(item.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "TabTransition"
            ) { targetTab ->
                when (targetTab) {
                    0 -> DashboardTab(
                        onDestinationClick = { activeDestinationIdForDetail = it },
                        onRailClick = { activeRailJourneyIdForDetail = it },
                        onTourClick = { activeGrandTourIdForDetail = it }
                    )
                    1 -> AtlasTab(
                        viewModel = viewModel,
                        onDestinationClick = { activeDestinationIdForDetail = it }
                    )
                    2 -> RailAndToursTab(
                        viewModel = viewModel,
                        savedItineraries = savedItineraries,
                        onRailClick = { activeRailJourneyIdForDetail = it },
                        onTourClick = { activeGrandTourIdForDetail = it }
                    )
                    3 -> HeritageTab(
                        onDestinationClick = { activeDestinationIdForDetail = it }
                    )
                    4 -> MyUkTab(
                        viewModel = viewModel,
                        stamps = stamps,
                        journalEntries = journalEntries,
                        savedItineraries = savedItineraries,
                        onOpenJournalForm = { showAddJournalDialog = true }
                    )
                }
            }
        }
    }

    // ---- Bottom Sheet / Modals ----
    activeDestinationIdForDetail?.let { destId ->
        val dest = BritanniaStaticData.DESTINATIONS.firstOrNull { it.id == destId }
        if (dest != null) {
            DestinationDetailSheet(
                destination = dest,
                hasStampUnlocked = stamps.firstOrNull { it.id == destId }?.isUnlocked ?: false,
                onDismiss = { activeDestinationIdForDetail = null },
                onCheckIn = {
                    viewModel.unlockPassportStamp(destId)
                    Toast.makeText(context, "🏵️ Passport Stamp Earned: ${dest.name}!", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    activeRailJourneyIdForDetail?.let { journeyId ->
        val journey = BritanniaStaticData.RAIL_JOURNEYS.firstOrNull { it.id == journeyId }
        if (journey != null) {
            RailJourneyDetailSheet(
                journey = journey,
                onDismiss = { activeRailJourneyIdForDetail = null }
            )
        }
    }

    activeGrandTourIdForDetail?.let { tourId ->
        val tour = BritanniaStaticData.GRAND_TOURS.firstOrNull { it.id == tourId }
        if (tour != null) {
            GrandTourDetailSheet(
                tour = tour,
                onDismiss = { activeGrandTourIdForDetail = null },
                onBookTour = {
                    viewModel.saveCreatedItinerary(
                        title = tour.name,
                        duration = tour.duration,
                        stops = tour.stops.joinToString(", "),
                        railRoute = "Great British Railways Network Express",
                        hotel = "Boutique Heritage Lodges",
                        activities = "Full curator guided tour. " + tour.description
                    )
                    Toast.makeText(context, "📝 Tour Saved to your Saved Itineraries!", Toast.LENGTH_LONG).show()
                    activeGrandTourIdForDetail = null
                }
            )
        }
    }

    if (showAddJournalDialog) {
        AddJournalDialog(
            onDismiss = { showAddJournalDialog = false },
            onSave = { title, content, location, tag ->
                viewModel.addJournal(title, content, location, tag)
                Toast.makeText(context, "✒️ Trip Record Saved!", Toast.LENGTH_SHORT).show()
                showAddJournalDialog = false
            }
        )
    }
}

data class NavigationItem(val label: String, val icon: ImageVector, val tag: String)

// ==========================================
// TABS IMPLEMENTATION
// ==========================================

// ------------------------------------------
// TAB 1: DASHBOARD
// ------------------------------------------
@Composable
fun DashboardTab(
    onDestinationClick: (String) -> Unit,
    onRailClick: (String) -> Unit,
    onTourClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Publication Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .testTag("hero_banner"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = RacingGreen)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, DeepNavy.copy(alpha = 0.85f))
                            )
                        )
                        .padding(16.dp)
                ) {
                    // Vintage Corner Framing Graphic using canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeW = 2.dp.toPx()
                        drawRect(
                            color = AntiqueBrass.copy(alpha = 0.4f),
                            size = size,
                            style = Stroke(width = strokeW)
                        )
                    }

                    Column(
                        modifier = Modifier.align(Alignment.BottomStart)
                    ) {
                        Text(
                            text = "SEASONAL FIELD NOTES",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AntiqueBrass,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Lavender & Limestone Spires",
                            fontFamily = FontFamily.Serif,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = ClassicCream
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A summer guide to the golden cottage trails of the Cotswolds and ancient Saxon chapels.",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 13.sp,
                            color = ClassicCream.copy(alpha = 0.8f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Section: Featured Destinations (National Geographic Style)
        item {
            Column {
                SectionHeader(title = "FEATURED CITADEL DIRECTORY")
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(BritanniaStaticData.DESTINATIONS) { destination ->
                        Card(
                            onClick = { onDestinationClick(destination.id) },
                            modifier = Modifier
                                .width(160.dp)
                                .height(220.dp)
                                .testTag("feat_dest_${destination.id}"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.7f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = destination.region.uppercase(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AntiqueBrass,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = destination.name,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = destination.description,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        maxLines = 5,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Explore link",
                                        tint = AntiqueBrass,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "READ JOURNAL",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AntiqueBrass
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Great British Railways (Suggested Runs)
        item {
            Column {
                SectionHeader(title = "NATIONAL RAILWAY ARCS")
                Spacer(modifier = Modifier.height(8.dp))
                BritanniaStaticData.RAIL_JOURNEYS.take(3).forEach { journey ->
                    Card(
                        onClick = { onRailClick(journey.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .testTag("rail_journey_row_${journey.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.6f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(NavyBlue, shape = RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Build,
                                    contentDescription = "Railway",
                                    tint = CrimsonBurgundy
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = journey.trainType.uppercase(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AntiqueBrass
                                )
                                Text(
                                    text = journey.name,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = journey.route,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "➔",
                                fontFamily = FontFamily.Monospace,
                                color = MutedSlate,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section: Curated Grand Tours
        item {
            Column {
                SectionHeader(title = "CURATED NATIONAL TOURS")
                Spacer(modifier = Modifier.height(8.dp))
                BritanniaStaticData.GRAND_TOURS.forEach { tour ->
                    HorizontalTourCard(tour = tour, onClick = { onTourClick(tour.id) })
                }
            }
        }

        // Section: Live Weekly Events
        item {
            Column {
                SectionHeader(title = "NATIONAL WEEKLY EXHIBITIONS")
                Spacer(modifier = Modifier.height(8.dp))
                BritanniaStaticData.EVENTS.forEach { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Badge(
                                    containerColor = CrimsonBurgundy,
                                    contentColor = ClassicCream
                                ) {
                                    Text(
                                        text = event.category.uppercase(),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = event.dateInfo,
                                    fontSize = 11.sp,
                                    color = AntiqueBrass,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = event.name,
                                fontFamily = FontFamily.Serif,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "📍 ${event.location}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = event.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------
// TAB 2: LIVING ATLAS & DIGITAL MAP
// ------------------------------------------
@Composable
fun AtlasTab(
    viewModel: BritanniaViewModel,
    onDestinationClick: (String) -> Unit
) {
    var selectedRegionFilter by remember { mutableStateOf("All Regions") }
    var zoomLevel by remember { mutableStateOf("Britain") } // Britain -> Region -> County -> Town -> Landmark

    val regions = listOf("All Regions", "London & South East", "Yorkshire & Humber", "South West", "Scotland", "East of England")

    // Destination search queries
    var searchTxt by remember { mutableStateOf("") }

    val filteredDestinations = BritanniaStaticData.DESTINATIONS.filter {
        (selectedRegionFilter == "All Regions" || it.region == selectedRegionFilter) &&
                (searchTxt.isBlank() || it.name.contains(searchTxt, ignoreCase = true) || it.county.contains(searchTxt, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("atlas_tab_root")
    ) {
        // High Search Panel
        OutlinedTextField(
            value = searchTxt,
            onValueChange = { searchTxt = it },
            label = { Text("Search counties, towns, or heritage locations") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchTxt.isNotEmpty()) {
                    IconButton(onClick = { searchTxt = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_bar"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AntiqueBrass,
                unfocusedBorderColor = BorderGold
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Zoom controller display
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LIVING ATLAS SCALE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AntiqueBrass
                    )
                    Text(
                        text = zoomLevel.uppercase(),
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Interactive Zoom Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val zoomSequence = listOf("Britain", "Region", "County", "Town", "Landmark")
                    
                    IconButton(
                        onClick = {
                            val currIndex = zoomSequence.indexOf(zoomLevel)
                            if (currIndex > 0) {
                                zoomLevel = zoomSequence[currIndex - 1]
                            }
                        },
                        enabled = zoomLevel != "Britain",
                        modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(4.dp))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Zoom Out", tint = MaterialTheme.colorScheme.primary)
                    }

                    IconButton(
                        onClick = {
                            val currIndex = zoomSequence.indexOf(zoomLevel)
                            if (currIndex < zoomSequence.size - 1) {
                                zoomLevel = zoomSequence[currIndex + 1]
                            }
                        },
                        enabled = zoomLevel != "Landmark",
                        modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(4.dp))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // Region Horizontal filters
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(regions) { r ->
                val isSelected = selectedRegionFilter == r
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedRegionFilter = r
                        zoomLevel = if (r == "All Regions") "Britain" else "Region"
                    },
                    label = { Text(r, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AntiqueBrass,
                        selectedLabelColor = DeepNavy
                    )
                )
            }
        }

        // Living Schematic Map of Britain (Fully custom Jetpack Compose Drawing & Interactive hotspots)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(BorderStroke(1.dp, BorderGold.copy(alpha = 0.5f)))
                .background(Color(0xFFF6F1E5))
                .testTag("interactive_map_canvas")
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Drawing high-contrast topographic-style lines mimicking regional rail paths
                val path = Path().apply {
                    moveTo(size.width * 0.5f, size.height * 0.1f) // Scotland Peak
                    lineTo(size.width * 0.45f, size.height * 0.35f) // Glasgow / Edinburgh
                    lineTo(size.width * 0.52f, size.height * 0.48f) // North England / Newcastle
                    lineTo(size.width * 0.55f, size.height * 0.62f) // Yorkshire / Midlands
                    lineTo(size.width * 0.41f, size.height * 0.72f) // Wales
                    lineTo(size.width * 0.62f, size.height * 0.78f) // London & Kent
                    lineTo(size.width * 0.32f, size.height * 0.9f) // Southwest (Cornwall)
                }

                // Draw Rail Spine Line
                drawPath(
                    path = path,
                    color = AntiqueBrass.copy(alpha = 0.7f),
                    style = Stroke(width = 3.dp.toPx())
                )
            }

            // Interactive Regional Hotspots over the schematic map!
            // Clicking them updates filters & zoom perfectly
            MapHotspotButton(
                label = "Scotland",
                alignment = Alignment.TopCenter,
                modifier = Modifier.offset(y = 20.dp),
                isSelected = selectedRegionFilter == "Scotland",
                onClick = {
                    selectedRegionFilter = "Scotland"
                    zoomLevel = "Region"
                }
            )

            MapHotspotButton(
                label = "The North",
                alignment = Alignment.Center,
                modifier = Modifier.offset(x = 10.dp, y = (-10.dp)),
                isSelected = selectedRegionFilter == "Yorkshire & Humber",
                onClick = {
                    selectedRegionFilter = "Yorkshire & Humber"
                    zoomLevel = "County"
                }
            )

            MapHotspotButton(
                label = "South East",
                alignment = Alignment.BottomEnd,
                modifier = Modifier.offset(x = (-20.dp), y = (-40.dp)),
                isSelected = selectedRegionFilter == "London & South East",
                onClick = {
                    selectedRegionFilter = "London & South East"
                    zoomLevel = "Town"
                }
            )

            MapHotspotButton(
                label = "South West",
                alignment = Alignment.BottomStart,
                modifier = Modifier.offset(x = 30.dp, y = (-20.dp)),
                isSelected = selectedRegionFilter == "South West",
                onClick = {
                    selectedRegionFilter = "South West"
                    zoomLevel = "Landmark"
                }
            )

            Text(
                text = "Interactive Schematic Map of Britannia",
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                color = AntiqueBrass.copy(alpha = 0.8f),
                modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Destination Pages/Cards List
        Text(
            text = "DISCOVERED LOCATIONS (${filteredDestinations.size})",
            fontFamily = FontFamily.Serif,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (filteredDestinations.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MutedSlate, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No matching locations found in this atlas zoom scale.", fontSize = 13.sp, color = MutedSlate, textAlign = TextAlign.Center)
                    }
                }
            } else {
                items(filteredDestinations) { dest ->
                    Card(
                        onClick = { onDestinationClick(dest.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dest_card_${dest.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dest.county.uppercase(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AntiqueBrass
                                )
                                Text(
                                    text = dest.alignment,
                                    fontSize = 9.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = RacingGreen
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = dest.name,
                                fontFamily = FontFamily.Serif,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dest.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MapHotspotButton(
    label: String,
    alignment: Alignment,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = alignment
    ) {
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) RacingGreen else CrimsonBurgundy.copy(alpha = 0.85f),
                contentColor = ClassicCream
            ),
            modifier = Modifier.height(28.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(if (isSelected) AntiqueBrass else Color.White, shape = RoundedCornerShape(50))
                )
                Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }
    }
}

// ------------------------------------------
// TAB 3: RAIL & TOURS & INTEGRATED PLANNER
// ------------------------------------------
@Composable
fun RailAndToursTab(
    viewModel: BritanniaViewModel,
    savedItineraries: List<SavedItinerary>,
    onRailClick: (String) -> Unit,
    onTourClick: (String) -> Unit
) {
    var activeSubTab by remember { mutableStateOf(0) } // 0: Live departures & Rail routes, 1: Grand Tours
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("rail_and_tours_root")
    ) {
        // Mode Selectors
        TabRow(
            selectedTabIndex = activeSubTab,
            containerColor = MaterialTheme.colorScheme.background,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeSubTab]),
                    color = AntiqueBrass
                )
            }
        ) {
            Tab(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                text = { Text("GREAT BRITISH RAILWAYS", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp) }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text("GRAND TOURS PLANNER", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (activeSubTab == 0) {
            // RAILWAYS SUB-TAB
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Sincere Live Departures Simulator
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("live_departures_board"),
                        colors = CardDefaults.cardColors(containerColor = NavyBlue),
                        border = BorderStroke(2.dp, AntiqueBrass)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.Place, contentDescription = null, tint = AntiqueBrass, modifier = Modifier.size(16.dp))
                                    Text("LIVE DEPARTURES BOARD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass, letterSpacing = 1.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Color.Red.copy(alpha = 0.2f))
                                        .border(BorderStroke(1.dp, Color.Red), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("REALTIME", fontSize = 8.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            // Railway Stations Rows
                            val departures = listOf(
                                DepartureStub("10:45", "Edinburgh Waverley", "LNER Azuma", "EXP", "On Time", "Plat 4"),
                                DepartureStub("11:00", "Penzance via Dawlish", "GWR Riviera", "PUL", "Delayed 5m", "Plat 1"),
                                DepartureStub("11:15", "Carlisle Mountain Run", "Northern Settle", "STM", "On Time", "Plat 8 B")
                            )

                            departures.forEach { s ->
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Text(s.time, color = AntiqueBrass, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                Text(s.destination, color = ClassicCream, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            }
                                            Text("${s.trainType} • ${s.type}", fontSize = 11.sp, color = MutedTextDark)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(s.status, color = if (s.status == "On Time") Color.Green else AccentGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(s.platform, fontSize = 11.sp, color = ClassicCream)
                                        }
                                    }
                                    HorizontalDivider(color = ClassicCream.copy(alpha = 0.1f))
                                }
                            }
                        }
                    }
                }

                // Heritage & Scenic Rail Lines List
                item {
                    Text(
                        text = "EXPLORE ICONIC SCENIC RAILWAYS",
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(BritanniaStaticData.RAIL_JOURNEYS) { run ->
                    Card(
                        onClick = { onRailClick(run.id) },
                        modifier = Modifier.fillMaxWidth().testTag("scenic_rail_card_${run.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(run.trainType.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Place, contentDescription = null, tint = RacingGreen, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Map details", fontSize = 9.sp, color = MutedSlate)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(run.name, fontFamily = FontFamily.Serif, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text(run.route, fontSize = 12.sp, color = MutedSlate, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(run.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        } else {
            // GRAND TOURS SUB-TAB
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = RacingGreen.copy(alpha = 0.08f)),
                        border = BorderStroke(1.dp, RacingGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Place, contentDescription = "Tours info", tint = RacingGreen, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Grand Tours of Britain are curated journeys spanning historic sites, elite hotels, dining rooms, and custom rail loops. Select any tour to view its daily guide and save to your travel agenda.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "AVAILABLE TOURS OF DISTINCTION",
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 2.sp
                    )
                }

                items(BritanniaStaticData.GRAND_TOURS) { tour ->
                    HorizontalTourCard(tour = tour, onClick = { onTourClick(tour.id) })
                }
            }
        }
    }
}

data class DepartureStub(val time: String, val destination: String, val trainType: String, val type: String, val status: String, val platform: String)

@Composable
fun HorizontalTourCard(tour: GrandTour, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .testTag("grand_tour_card_${tour.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(tour.theme.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
                Text("⏳ ${tour.duration}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CrimsonBurgundy)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(tour.name, fontFamily = FontFamily.Serif, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "STOPS: " + tour.stops.joinToString(" → "),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MutedSlate,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(tour.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), maxLines = 3, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.Place, contentDescription = null, tint = AntiqueBrass, modifier = Modifier.size(14.dp))
                Text("DISCOVER DAILY ITINERARY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
            }
        }
    }
}

// ------------------------------------------
// TAB 4: HERITAGE & ARCHITECTURE
// ------------------------------------------
@Composable
fun HeritageTab(
    onDestinationClick: (String) -> Unit
) {
    var activeSubHeritageTab by remember { mutableStateOf(0) } // 0: Eras, 1: Styles, 2: Cultural Atlas

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("heritage_tab_root"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "HERITAGE OF BRITANNIA",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Chart the chronologies, building crafts, and cultural keystones of our islands.",
                    fontSize = 12.sp,
                    color = MutedSlate
                )
            }
        }

        // Horizontal sub tabs selector
        item {
            ScrollableTabRow(
                selectedTabIndex = activeSubHeritageTab,
                containerColor = Color.Transparent,
                divider = {},
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[activeSubHeritageTab]),
                        color = AntiqueBrass
                    )
                }
            ) {
                Tab(selected = activeSubHeritageTab == 0, onClick = { activeSubHeritageTab = 0 }, text = { Text("HISTORIC ERAS", fontSize = 11.sp, fontWeight = FontWeight.Bold) }, modifier = Modifier.testTag("eras_tab"))
                Tab(selected = activeSubHeritageTab == 1, onClick = { activeSubHeritageTab = 1 }, text = { Text("ARCHITECTURE", fontSize = 11.sp, fontWeight = FontWeight.Bold) }, modifier = Modifier.testTag("architecture_tab"))
                Tab(selected = activeSubHeritageTab == 2, onClick = { activeSubHeritageTab = 2 }, text = { Text("CULTURAL ATLAS", fontSize = 11.sp, fontWeight = FontWeight.Bold) }, modifier = Modifier.testTag("culture_tab"))
            }
        }

        when (activeSubHeritageTab) {
            0 -> {
                // HISTORIC ERAS
                items(BritanniaStaticData.HERITAGE_ERAS) { era ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("era_card_${era.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = era.period,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CrimsonBurgundy
                                )
                                Icon(Icons.Default.List, contentDescription = null, tint = AntiqueBrass, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = era.name,
                                fontFamily = FontFamily.Serif,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = era.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "SITES TO VISIT: " + era.sites.joinToString(" • "),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = AccentGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            1 -> {
                // ARCHITECTURAL WALKING TOURS
                items(BritanniaStaticData.ARCHITECTURE_WALKS) { arch ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("architecture_style_card_${arch.name}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = arch.period.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = AntiqueBrass
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = arch.name,
                                fontFamily = FontFamily.Serif,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = arch.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "KEY STRUCTURAL DETAILS:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            arch.keyFeatures.forEach { feature ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = null,
                                        tint = RacingGreen,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(feature, fontSize = 11.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "EXEMPLARY MONUMENTS: " + arch.exemplaryBuildings.joinToString(", "),
                                fontSize = 10.sp,
                                fontStyle = FontStyle.Italic,
                                color = CrimsonBurgundy,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            2 -> {
                // CULTURAL ATLAS
                items(BritanniaStaticData.CULTURAL_ATLAS) { asset ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("culture_asset_card_${asset.title}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Badge(containerColor = AntiqueBrass.copy(alpha = 0.15f), contentColor = NavyBlue) {
                                Text(asset.category, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = asset.title,
                                fontFamily = FontFamily.Serif,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Britons: " + asset.associatedBritons,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = CrimsonBurgundy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = asset.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "📍 LANDMARK SHRINE: " + asset.localMonuments,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RacingGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------
// TAB 5: MY BRITANNIA (PASSPORT, FORMS, AI CONCIERGE)
// ------------------------------------------
@Composable
fun MyUkTab(
    viewModel: BritanniaViewModel,
    stamps: List<PassportStamp>,
    journalEntries: List<JournalEntry>,
    savedItineraries: List<SavedItinerary>,
    onOpenJournalForm: () -> Unit
) {
    var activeSubOption by remember { mutableStateOf(0) } // 0: Passport, 1: Journal, 2: AI Itineraries, 3: Saved/Bar Membership

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("my_uk_tab_root")
    ) {
        // Selection chips
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chips = listOf("Passport", "Travel Journal", "AI Travel Concierge", "Saves & Bar Club")
            chips.forEachIndexed { i, label ->
                val isSelected = activeSubOption == i
                FilterChip(
                    selected = isSelected,
                    onClick = { activeSubOption = i },
                    label = { Text(label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AntiqueBrass,
                        selectedLabelColor = DeepNavy
                    ),
                    modifier = Modifier.testTag("my_uk_chip_$i")
                )
            }
        }

        when (activeSubOption) {
            0 -> {
                // EXPLORER PASSPORT (Stamps grid)
                Column(modifier = Modifier.weight(1f)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DeepNavy),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        border = BorderStroke(1.dp, AntiqueBrass)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "EXPLORER PASSPORT DEEDS",
                                fontFamily = FontFamily.Serif,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AntiqueBrass,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val unlockedCount = stamps.count { it.isUnlocked }
                            Text(
                                text = "$unlockedCount / ${stamps.size} CITADELS CONQUERED",
                                fontFamily = FontFamily.Serif,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClassicCream
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Simple Progress Bar
                            LinearProgressIndicator(
                                progress = { if (stamps.isNotEmpty()) unlockedCount.toFloat() / stamps.size.toFloat() else 0f },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = AntiqueBrass,
                                trackColor = ClassicCream.copy(alpha = 0.2f)
                            )
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(stamps) { stamp ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clickable {
                                        if (stamp.isUnlocked) {
                                            viewModel.relockPassportStamp(stamp.id)
                                        } else {
                                            viewModel.unlockPassportStamp(stamp.id)
                                        }
                                    }
                                    .testTag("passport_stamp_${stamp.id}"),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (stamp.isUnlocked) Color(0xFFE6EFEA) else MaterialTheme.colorScheme.surface
                                ),
                                border = BorderStroke(
                                    1.2.dp,
                                    if (stamp.isUnlocked) RacingGreen else BorderGold.copy(alpha = 0.6f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp).fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Visual stamp indicator
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                if (stamp.isUnlocked) RacingGreen else MutedSlate.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(50)
                                            )
                                            .border(
                                                BorderStroke(2.dp, if (stamp.isUnlocked) AntiqueBrass else MutedSlate),
                                                shape = RoundedCornerShape(50)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (stamp.isUnlocked) Icons.Default.Star else Icons.Default.Lock,
                                            contentDescription = if (stamp.isUnlocked) "Unlocked" else "Locked",
                                            tint = if (stamp.isUnlocked) AntiqueBrass else MutedSlate,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = stamp.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = if (stamp.isUnlocked) RacingGreen else MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = if (stamp.isUnlocked) {
                                                val dateStr = stamp.unlockDate?.let {
                                                    SimpleDateFormat("d MMM yy", Locale.UK).format(Date(it))
                                                } ?: ""
                                                "MINTED: $dateStr"
                                            } else stamp.category,
                                            fontSize = 9.sp,
                                            color = MutedSlate,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // TRIP JOURNAL
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BRITANNIA EXPEDITION LOGS",
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Button(
                            onClick = onOpenJournalForm,
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonBurgundy, contentColor = Color.White),
                            modifier = Modifier.testTag("add_journal_button")
                        ) {
                            Text("New Log", fontSize = 11.sp)
                        }
                    }

                    if (journalEntries.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.List, contentDescription = null, tint = MutedSlate, modifier = Modifier.size(64.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Your digital journey logbook is currently pristine.", fontSize = 13.sp, color = MutedSlate, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Log architectural walks, cozy pub fireplace nights or rail peaks to build your travel history.", fontSize = 12.sp, color = MutedSlate.copy(alpha = 0.8f), textAlign = TextAlign.Center)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(journalEntries) { log ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Icon(Icons.Default.Place, contentDescription = null, tint = AntiqueBrass, modifier = Modifier.size(13.dp))
                                                Text(log.location, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
                                            }
                                            IconButton(
                                                onClick = { viewModel.deleteJournal(log) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete entry", tint = MutedSlate, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(log.title, fontFamily = FontFamily.Serif, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(log.content, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Badge(containerColor = RacingGreen.copy(alpha = 0.15f), contentColor = RacingGreen) {
                                                Text(log.tag, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                            val logDate = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.UK).format(Date(log.date))
                                            Text(logDate, fontSize = 9.sp, color = MutedSlate)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // ANGLO-FUTURIST AI TRAVEL CONCIERGE (Gemini integration!)
                var inputQuery by remember { mutableStateOf("") }
                val aiState by viewModel.aiState.collectAsStateWithLifecycle()
                val context = LocalContext.current

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ANGLO-FUTURIST CONCIERGE",
                        fontFamily = FontFamily.Serif,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Let our machine concierge outline perfect custom rail journeys or bespoke architectural walking guides.",
                        fontSize = 12.sp,
                        color = MutedSlate
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Dynamic chips suggestions for rapid trial
                    val sugChips = listOf(
                        "5-day Rail journey around Yorkshire Roman & Gothic sites",
                        "Bath and Devon coastal walking tour with hotels",
                        "Victorian engineering & Steam rail itinerary"
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        items(sugChips) { txt ->
                            ActionChipStub(text = txt) {
                                inputQuery = txt
                                viewModel.askAIConcierge(txt)
                            }
                        }
                    }

                    // Answer Panel
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .border(BorderStroke(1.dp, BorderGold.copy(alpha = 0.5f)))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        when (val st = aiState) {
                            is AiState.Idle -> {
                                Column(
                                    modifier = Modifier.fillMaxSize().padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(Icons.Default.Face, contentDescription = null, tint = AntiqueBrass, modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Awaiting your parameters...", fontSize = 12.sp, color = MutedSlate)
                                }
                            }
                            is AiState.Loading -> {
                                Column(
                                    modifier = Modifier.fillMaxSize().padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(color = AntiqueBrass)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("Consulting the Britannia Archives...", fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, fontSize = 13.sp, color = AntiqueBrass)
                                    Text("Refining train alignments and boutique hotel bookings", fontSize = 11.sp, color = MutedSlate)
                                }
                            }
                            is AiState.Success -> {
                                Column {
                                    Text(
                                        text = "BRITANNIA OFFICE RECOMMENDATION",
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RacingGreen,
                                        letterSpacing = 2.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    // Markdown visual output
                                    Text(
                                        text = st.response,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Direct Save Itinerary Button
                                    Button(
                                        onClick = {
                                            viewModel.saveCreatedItinerary(
                                                title = "AI Concierge Plan: " + inputQuery.take(28) + "...",
                                                duration = "Custom AI Tour",
                                                stops = "Dynamic stops as suggested",
                                                railRoute = "Great British Railways Network Express",
                                                hotel = "Historical coaching inns",
                                                activities = st.response
                                            )
                                            Toast.makeText(context, "📝 AI Itinerary Saved directly to your Saves!", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = RacingGreen, contentColor = Color.White),
                                        modifier = Modifier.fillMaxWidth().testTag("save_ai_itinerary_btn")
                                    ) {
                                        Text("Save Itinerary to Planner Database", fontSize = 12.sp)
                                    }
                                }
                            }
                            is AiState.Error -> {
                                Column {
                                    Icon(Icons.Default.Warning, contentDescription = "Error", tint = CrimsonBurgundy)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(st.message, fontSize = 12.sp, color = CrimsonBurgundy)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Input Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputQuery,
                            onValueChange = { inputQuery = it },
                            placeholder = { Text("Ask about custom routes or towns...") },
                            modifier = Modifier.weight(1f).testTag("ai_input_field"),
                            maxLines = 2,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = {
                                viewModel.askAIConcierge(inputQuery)
                            })
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { viewModel.askAIConcierge(inputQuery) },
                            modifier = Modifier
                                .size(48.dp)
                                .background(NavyBlue, shape = RoundedCornerShape(8.dp))
                                .testTag("ai_send_button")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Query AI", tint = AntiqueBrass)
                        }
                    }
                }
            }
            3 -> {
                // SAVES & BAR BARCODE MEMBERSHIP
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Premium Gold Club Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .testTag("club_membership_card"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, AntiqueBrass)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(DeepNavy, NavyBlue)
                                        )
                                    )
                                    .padding(16.dp)
                            ) {
                                Column(modifier = Modifier.align(Alignment.TopStart)) {
                                    Text(
                                        text = "BRITANNIA EMBASSY LOUGE",
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AntiqueBrass,
                                        letterSpacing = 2.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "MEMBERSHIP CLUB",
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ClassicCream
                                    )
                                }

                                Text(
                                    text = "PASS NO: BR-8704-UK",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = MutedSlate,
                                    modifier = Modifier.align(Alignment.BottomStart)
                                )

                                // Barcode generator mock
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = "|| ||| || ||| ||| ||",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AntiqueBrass
                                    )
                                    Text(
                                        text = "PRIORITY ROYAL STATUS",
                                        fontSize = 8.sp,
                                        color = AntiqueBrass
                                    )
                                }
                            }
                        }
                    }

                    // Saved Itineraries Header
                    item {
                        Text(
                            text = "SAVED TRAVEL ITINERARIES (${savedItineraries.size})",
                            fontFamily = FontFamily.Serif,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 2.sp
                        )
                    }

                    if (savedItineraries.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.4f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.DateRange, contentDescription = null, tint = MutedSlate)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("No planned itineraries currently saved.", fontSize = 12.sp, color = MutedSlate)
                                    Text("Utilize the Grand Tour tabs or AI Assistant generators to save your schedule.", fontSize = 11.sp, color = MutedSlate.copy(alpha = 0.8f), textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }

                    items(savedItineraries) { agenda ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                                .testTag("saved_agenda_row_${agenda.id}"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, BorderGold.copy(alpha = 0.7f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = agenda.duration.uppercase(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CrimsonBurgundy
                                        )
                                        Text(
                                            text = agenda.title,
                                            fontFamily = FontFamily.Serif,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.removeItinerary(agenda) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete agenda", tint = MutedSlate, modifier = Modifier.size(16.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                if (agenda.stops.isNotEmpty()) {
                                    Text("STOPS: " + agenda.stops, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedSlate)
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                Text("RAIL: " + agenda.railRouteName, fontSize = 11.sp, color = MutedSlate)
                                Text("HOTEL: " + agenda.hotelName, fontSize = 11.sp, color = MutedSlate)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = agenda.activities,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActionChipStub(text: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(end = 6.dp).height(38.dp),
        colors = CardDefaults.cardColors(containerColor = AntiqueBrass.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, AntiqueBrass)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight().padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Done, contentDescription = "Quick Query", tint = AntiqueBrass, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

// ==========================================
// DETAILS MODAL BOTTOM SHEETS AND DIALOGS
// ==========================================

// ---- DESTINATION PAGES PROFILE SHEET ----
@Composable
fun DestinationDetailSheet(
    destination: Destination,
    hasStampUnlocked: Boolean,
    onDismiss: () -> Unit,
    onCheckIn: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CLOSE DIRECTORY", color = CrimsonBurgundy)
            }
        },
        title = {
            Column {
                Text(destination.county.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
                Text(destination.name, fontFamily = FontFamily.Serif, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Interactive Check In Stamp Claim!
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(
                                1.5.dp,
                                if (hasStampUnlocked) RacingGreen else AntiqueBrass
                            )
                        )
                        .background(if (hasStampUnlocked) Color(0xFFE6EFEA) else ClassicCream)
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    if (hasStampUnlocked) RacingGreen else AntiqueBrass.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(50)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (hasStampUnlocked) Icons.Default.CheckCircle else Icons.Default.Add,
                                contentDescription = null,
                                tint = if (hasStampUnlocked) AntiqueBrass else AntiqueBrass
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (hasStampUnlocked) "CITADEL PASSPORT STAMP CLAIMED" else "ARE YOU EXPLORING ${destination.name.uppercase()}?",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hasStampUnlocked) RacingGreen else MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (hasStampUnlocked) "This stamp is actively unlocked and saved in your Explorer Passport deeds records." else "Check-in to simulate physical presence and mint this county seal on your passport.",
                            fontSize = 11.sp,
                            color = MutedSlate,
                            textAlign = TextAlign.Center
                        )
                        if (!hasStampUnlocked) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onCheckIn,
                                colors = ButtonDefaults.buttonColors(containerColor = RacingGreen)
                            ) {
                                Text("MINT COUNTY SEAL", fontSize = 11.sp, color = ClassicCream)
                            }
                        }
                    }
                }

                DetailSection(title = "INTRODUCTION", desc = destination.description)
                DetailSection(title = "CHRONICLED HISTORY", desc = destination.history)
                DetailSection(title = "CRAFT & ARCHITECTURE", desc = destination.architecture)
                DetailSection(title = "TRANSPORT & CONNECTIONS", desc = destination.transport)

                // Attractions List
                Column {
                    Text("MONUMENT KEYSTONES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
                    Spacer(modifier = Modifier.height(4.dp))
                    destination.attractions.forEach { attraction ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = RacingGreen, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(attraction, fontSize = 13.sp)
                        }
                    }
                }

                // Hotels list
                Column {
                    Text("ELITE SANCTUARIES & HOSTELS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
                    Spacer(modifier = Modifier.height(4.dp))
                    destination.hotels.forEach { hotelName ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = CrimsonBurgundy, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(hotelName, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    )
}

// ---- RAIL JOURNEY PROFILE SHEET ----
@Composable
fun RailJourneyDetailSheet(
    journey: RailJourney,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("DISMISS SCHEMATIC", color = CrimsonBurgundy)
            }
        },
        title = {
            Column {
                Text(journey.trainType.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
                Text(journey.name, fontFamily = FontFamily.Serif, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(journey.description, fontSize = 13.sp)

                // Visual Stop line canvas diagram!
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(NavyBlue)
                        .padding(10.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawLine(
                            color = AntiqueBrass,
                            start = Offset(0f, size.height * 0.5f),
                            end = Offset(size.width, size.height * 0.5f),
                            strokeWidth = 2.dp.toPx()
                        )

                        // Draw points
                        val totalPoints = journey.spots.size
                        if (totalPoints > 1) {
                            val step = size.width / (totalPoints - 1)
                            for (i in 0 until totalPoints) {
                                drawCircle(
                                    color = CrimsonBurgundy,
                                    radius = 6.dp.toPx(),
                                    center = Offset(i * step, size.height * 0.5f)
                                )
                                drawCircle(
                                    color = Color.White,
                                    radius = 3.dp.toPx(),
                                    center = Offset(i * step, size.height * 0.5f)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        journey.spots.forEach { s ->
                            Text(
                                text = s.take(12),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClassicCream,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                DetailSection(title = "INTEGRATED PATHWAYS", desc = journey.route)
                DetailSection(title = "PLANNER RECOMMENDATIONS", desc = journey.sugItinerary)
            }
        }
    )
}

// ---- GRAND TOUR DETAIL SHEET ----
@Composable
fun GrandTourDetailSheet(
    tour: GrandTour,
    onDismiss: () -> Unit,
    onBookTour: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onBookTour,
                colors = ButtonDefaults.buttonColors(containerColor = RacingGreen)
            ) {
                Text("BOOK & SAVE TOUR", color = ClassicCream)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("BACK", color = CrimsonBurgundy)
            }
        },
        title = {
            Column {
                Text(tour.theme.uppercase() + " • " + tour.duration, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
                Text(tour.name, fontFamily = FontFamily.Serif, fontSize = 21.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(tour.description, fontSize = 13.sp)

                Text(
                    text = "STOPS TO MAKE: " + tour.stops.joinToString(" → "),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedSlate
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text("DAY-BY-DAY CURATED RUNS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)

                tour.dayByDay.forEachIndexed { i, dayText ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(0.8.dp, BorderGold.copy(alpha = 0.5f))
                    ) {
                        Row(modifier = Modifier.padding(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(CrimsonBurgundy, shape = RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "${i + 1}", color = ClassicCream, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(dayText, fontSize = 12.sp, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    )
}

// ---- ADD JOURNAL RECORD MODAL ----
@Composable
fun AddJournalDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("Rail") }

    val tags = listOf("Rail", "Cathedral", "Atmospheric Cottage", "Pub", "Countryside", "Museum")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank() && location.isNotBlank()) {
                        onSave(title, content, location, tag)
                    }
                },
                enabled = title.isNotBlank() && content.isNotBlank() && location.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = RacingGreen),
                modifier = Modifier.testTag("save_journal_confirm")
            ) {
                Text("SAVE RECORD", color = ClassicCream)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = CrimsonBurgundy)
            }
        },
        title = {
            Text("NEW EXPEDITION LOG", fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Log Title (e.g., Afternoon at Whitby)") },
                    modifier = Modifier.fillMaxWidth().testTag("add_journal_title_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location (e.g., North Yorkshire)") },
                    modifier = Modifier.fillMaxWidth().testTag("add_journal_location_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Write your visual and historic impressions...") },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("add_journal_content_input"),
                    maxLines = 5
                )

                // Tags selection
                Text("ESTABLISH LOG CATEGORY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(tags) { t ->
                        val isSelected = tag == t
                        FilterChip(
                            selected = isSelected,
                            onClick = { tag = t },
                            label = { Text(t, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AntiqueBrass,
                                selectedLabelColor = DeepNavy
                            )
                        )
                    }
                }
            }
        }
    )
}

// ==========================================
// LOW-LEVEL VISUAL ATOMS
// ==========================================

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = FontFamily.Serif,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 2.sp
        )
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .padding(horizontal = 8.dp)
                .background(BorderGold.copy(alpha = 0.5f))
        )
    }
}

@Composable
fun DetailSection(title: String, desc: String) {
    Column {
        Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AntiqueBrass)
        Spacer(modifier = Modifier.height(2.dp))
        Text(desc, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}
