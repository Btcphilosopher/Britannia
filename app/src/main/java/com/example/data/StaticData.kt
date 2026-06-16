package com.example.data

data class Destination(
    val id: String,
    val name: String,
    val region: String,
    val county: String,
    val description: String,
    val history: String,
    val architecture: String,
    val transport: String,
    val attractions: List<String>,
    val hotels: List<String>,
    val alignment: String = "Heritage meets Future"
)

data class RailJourney(
    val id: String,
    val name: String,
    val route: String,
    val description: String,
    val spots: List<String>,
    val sugItinerary: String,
    val trainType: String // e.g., "Heritage Steam", "High-Speed", "Luxury Pullman"
)

data class HeritageEra(
    val id: String,
    val name: String,
    val period: String,
    val description: String,
    val sites: List<String>,
    val details: String
)

data class GrandTour(
    val id: String,
    val name: String,
    val stops: List<String>,
    val duration: String,
    val theme: String,
    val description: String,
    val dayByDay: List<String>
)

data class HospitalityVenue(
    val name: String,
    val type: String, // e.g., "Country House Hotel", "Historic Coaching Inn", "Teahouse", "Gastropub"
    val region: String,
    val description: String,
    val specialty: String,
    val rating: String = "★★★★★"
)

data class EventItem(
    val name: String,
    val location: String,
    val dateInfo: String,
    val description: String,
    val category: String // e.g., "Festival", "Exhibition", "Sporting", "Theatre"
)

data class ArchitecturalStyle(
    val name: String,
    val period: String,
    val keyFeatures: List<String>,
    val description: String,
    val exemplaryBuildings: List<String>
)

data class CulturalAsset(
    val title: String,
    val category: String, // "Literature", "Science & Engineering", "Music", "Philosophy"
    val associatedBritons: String,
    val description: String,
    val localMonuments: String
)

object BritanniaStaticData {

    val DESTINATIONS = listOf(
        Destination(
            id = "london",
            name = "London",
            region = "London & South East",
            county = "Greater London",
            description = "The historic, imperial, yet unapologetically modern capital where ancient Roman foundations sit below steel-and-glass skyscrapers.",
            history = "Founded as Londinium by the Romans in 43 AD, survived plague, fire, Blitz, and emerged as the dynamic beating pulse of Great Britain.",
            architecture = "Layered styles spanning medieval Tower of London, Sir Christopher Wren's baroque St. Paul’s Cathedral, Victorian Gothic Westminster Palace, and Anglo-Futurist icons like the Shard.",
            transport = "Accessible via the London Underground, overground rail, iconic red double-deckers, and river Thames clip clipper boats.",
            attractions = listOf("British Museum", "St Paul's Cathedral", "Tower of London", "The South Bank Arts Trail", "Westminster Abbey"),
            hotels = listOf("The Savoy (Luxury Standard)", "Brown's Hotel (Historic Inn)", "The Goring (Royal Splendour)")
        ),
        Destination(
            id = "york",
            name = "York",
            region = "Yorkshire & Humber",
            county = "North Yorkshire",
            description = "A walled city preserved in stone, containing two thousand years of history under the shadow of its magnificent central Minster.",
            history = "Once named Eboracum (Roman capital) and Jorvik (Viking stronghold), York has been a critical northern power centroid for centuries.",
            architecture = "Medieval limestone bar walls, timber-framed Overhanging Shambles (14th-century merchant street), Gothic cathedral masterpiece, and classical Georgian townhouses.",
            transport = "Centrally integrated on the LNER East Coast Main Line, under two hours from London.",
            attractions = listOf("York Minster", "The Shambles", "National Railway Museum", "Jorvik Viking Centre", "Medieval Bar Walls"),
            hotels = listOf("The Grand, York (Edwardian Palace)", "Middletons (Historic Guild Rest)", "Dean Court Hotel")
        ),
        Destination(
            id = "bath",
            name = "Bath",
            region = "South West",
            county = "Somerset",
            description = "A stunning Georgian sanctuary engineered out of local honey-gold stone around thermal hot springs.",
            history = "Established around Britain's only natural mineral pools by the Romans as Aquae Sulis in 60 AD, later reimagined in the 18th century as a high society spa sanitarium.",
            architecture = "Exquisite Palladian rows, John Wood's high Royal Crescent, the Circular Circus, and the ornate Pulteney Bridge spanning the River Avon.",
            transport = "Direct high-speed services from London Paddington on the Great Western Railway.",
            attractions = listOf("Roman Baths", "Bath Abbey", "The Royal Crescent", "Pulteney Bridge", "Thermae Bath Spa"),
            hotels = listOf("The Royal Crescent Hotel", "The Gainsborough Bath Spa", "The Bath Priory")
        ),
        Destination(
            id = "edinburgh",
            name = "Edinburgh",
            region = "Scotland",
            county = "Midlothian",
            description = "An dramatic, craggy, volcanic cityscape crowned by a royal castle, framing a majestic combination of medieval old town and neoclassical new town.",
            history = "The ancient capital of Scotland, home to the Enlightenment and decades of royal defense.",
            architecture = "Dark tenements of the medieval Royal Mile, contrasting with clean symmetry of the Georgian New Town and modern Scottish Parliament.",
            transport = "Edinburgh Waverley railway station sits right in the gorge between the Old and New Towns.",
            attractions = listOf("Edinburgh Castle", "The Royal Mile", "Holyroodhouse Palace", "Arthur's Seat Peak", "National Museum of Scotland"),
            hotels = listOf("The Balmoral (Railway Grand Dame)", "Prestonfield House", "The Witchery by the Castle")
        ),
        Destination(
            id = "canterbury",
            name = "Canterbury",
            region = "South East",
            county = "Kent",
            description = "A sacred walled cathedral town in the Garden of England, immortalized as a ancient pilgrimage goal.",
            history = "The seat of the Archbishop of Canterbury since the arrival of St Augustine in 597 AD. Infamous for the 1170 martyrdom of Thomas Becket.",
            architecture = "Sensational Early English Gothic and Romanesque choir vaults inside Canterbury Cathedral, Tudor timber frameworks lining narrow lanes.",
            transport = "Southeastern high-speed railway connecting to London St. Pancras in under 55 minutes.",
            attractions = listOf("Canterbury Cathedral", "St Augustine's Abbey", "River Stour Boat Tours", "The Westgate Towers"),
            hotels = listOf("The Canterbury Cathedral Lodge", "The Falstaff Inn", "The Abode Canterbury")
        ),
        Destination(
            id = "oxford",
            name = "Oxford",
            region = "South East",
            county = "Oxfordshire",
            description = "The City of Dreaming Spires, cradling the oldest university in the English-speaking world in harmonious architectural unity.",
            history = "Host to active academic tutorial debate since the 11th century; a royal capital during the English Civil War.",
            architecture = "Highly concentrated Saxon towers, Gothic quadrangles, the baroque circular Radcliffe Camera dome, and the Neoclassical Ashmolean Museum.",
            transport = "Frequent commuter expresses from London Marylebone and London Paddington.",
            attractions = listOf("Bodleian Library", "Radcliffe Camera", "Christ Church Meadow", "Ashmolean Museum", "Deer Park at Magdalen"),
            hotels = listOf("The Randolph Hotel", "The Old Bank Hotel", "Malmaison Oxford Castle")
        ),
        Destination(
            id = "cambridge",
            name = "Cambridge",
            region = "East of England",
            county = "Cambridgeshire",
            description = "A fenland academic university hub defined by idyllic river lawns, ornate stone chapels, and cutting-edge silicon research labs.",
            history = "Founded in 1209 by scholars fleeing hostility in Oxford. It remains the cradle of major scientific revolutions from Newton to DNA.",
            architecture = "The late Gothic perfection of King’s College Chapel fan vaults, classical mathematical sandstone bridges, and high modern laboratory pavilions.",
            transport = "Great Northern fast trains from London King’s Cross in 48 minutes.",
            attractions = listOf("King's College Chapel", "Punting on the River Cam", "The Fitzwilliam Museum", "Trinity College Great Court"),
            hotels = listOf("The University Arms (Town Landmark)", "The Varsity Hotel & Spa", "The Fellows House")
        )
    )

    val RAIL_JOURNEYS = listOf(
        RailJourney(
            id = "london_edinburgh",
            name = "The Flying Scotsman Route",
            route = "London King's Cross to Edinburgh Waverley",
            description = "Race up the backbone of Britain on the LNER East Coast Main Line, sweeping past York Minster and catching dramatic cliffside views of the North Sea near Berwick-upon-Tweed.",
            spots = listOf("London", "York Cathedral Profile", "Durham Castle & Cathedral on Ridge", "Newcastle Tyne Bridges", "Holy Island Coastline", "Edinburgh"),
            sugItinerary = "4 hours of high-speed comfort. Sit on the right-hand side for jaw-dropping coastal views of Northumberland.",
            trainType = "LNER Azuma High-Speed Rail"
        ),
        RailJourney(
            id = "settle_carlisle",
            name = "The Settle & Carlisle Line",
            route = "Settle to Carlisle (Cumbria / Yorkshire Dales)",
            description = "The absolute apex of Victorian infrastructure. This engineering wonder scales high Pennine fells, crossing iconic viaducts that blend beautifully into national park scenery.",
            spots = listOf("Settle", "Ribblehead Viaduct (24 brick arches)", "Dent Station (England's Highest)", "Garsdale Summit", "Eden Valley fells", "Carlisle"),
            sugItinerary = "Perfect for a lazy morning heritage excursion. Combined with a vintage steam engine run on select seasonal Saturdays.",
            trainType = "Heritage Steam & Northern Diesel"
        ),
        RailJourney(
            id = "cornish_riviera",
            name = "The Cornish Riviera Express",
            route = "London Paddington to Penzance",
            description = "The legendary Great Western line engineered by Isambard Kingdom Brunel. Skirt the sea wall at Dawlish where red sandstone cliffs meet ocean spraying waves.",
            spots = listOf("London", "Reading Berkshire", "Exeter Red Fells", "Dawlish Sea Wall", "Royal Albert Bridge", "St Ives branch", "Penzance"),
            sugItinerary = "A magnificent day journey. Book a first-class Pullman dining seat for silver service roast beef served as England glides past.",
            trainType = "GWR Intercity & Night Riviera Sleeper"
        ),
        RailJourney(
            id = "north_wales",
            name = "The North Wales Coast Line",
            route = "Chester to Holyhead",
            description = "Speed past imposing Edwardian castles and long sandy beaches of Flintshire, crossing the Menai Strait over Thomas Telford’s suspension shadow.",
            spots = listOf("Chester Castle Walls", "Flint Sands", "Conwy Castle (Rail runs through towers)", "Bangor University", "Britannia Bridge", "Holyhead Port"),
            sugItinerary = "Combine this with the Welsh Highland Steam Railway starting in Caernarfon for deep mountain exploration.",
            trainType = "Avanti West Coast & Transport for Wales"
        ),
        RailJourney(
            id = "highland_main",
            name = "The Highland Main Line",
            route = "Perth to Inverness",
            description = "Traverse wild Scottish countrysides, scaling desolate high mountain passes, running through thick pine groves of the Cairngorms and sweeping heather meadows.",
            spots = listOf("Perth Spires", "Dunkeld Hermitage", "Pitlochry Highland Resort", "Druimuachdar Summit (460m)", "Aviemore Ski Peak", "Inverness"),
            sugItinerary = "A breathtaking mountain traverse. Watch for wild red stags and misty castle ruins from your panoramic windows.",
            trainType = "ScotRail Inter7City Train"
        )
    )

    val HERITAGE_ERAS = listOf(
        HeritageEra(
            id = "roman",
            name = "Roman Britannia",
            period = "43 AD – 410 AD",
            description = "Engineering the wild north. Straight paved military highways, high protective wall frontiers, hot spa complexes, and amphitheaters that brought Mediterranean stone civilization to misty Celtic forests.",
            sites = listOf("Hadrian's Wall", "Roman Baths (Bath)", "Eboracum Remains (York)", "Verulamium Archeology (St Albans)"),
            details = "Focus on the straight lines, high defensive military forts, and advanced mosaic baths which established Britain's oldest urban foundations."
        ),
        HeritageEra(
            id = "gothic",
            name = "Gothic & Cathedral Britain",
            period = "11th – 16th Century",
            description = "Reaching for the heavens in golden limestone. Architectural structures displaying pointed arches, flying buttresses, stained glass narratives, and vast vaulted roof naves representing medieval engineering.",
            sites = listOf("York Minster Guild", "Canterbury Shrine", "Durham Monastic Rock", "Salisbury Cathedral Spire (Highest)"),
            details = "Centering on medieval spiritual scale, monastic gardens, sacred library archives, and unmatched stained-glass craft."
        ),
        HeritageEra(
            id = "maritime",
            name = "Maritime Britain",
            period = "16th – 20th Century",
            description = "The island nation's relationship with wild ocean currents. Oak warships, dry docks, maritime trade ports, Greenwich astronomical navigation, and coastal chalk defenses.",
            sites = listOf("Historic Dockyard Portsmouth", "Greenwich Royal Observatory", "Plymouth Hoe", "Liverpool Royal Albert Dock"),
            details = "Explores shipyards, sea shanties, celestial navigation clocks, and heroic lighthouses guarding hazardous capes."
        ),
        HeritageEra(
            id = "industrial",
            name = "Industrial Revolution",
            period = "1760 – 1914",
            description = "The birthplace of modern machines. Cast-iron bridges, canal aqua-ducts, towering brick cotton mills, coal mine shafts, steam locomotives, and grand railway arches that reshaped the global landscape.",
            sites = listOf("Ironbridge Gorge (Shropshire)", "Manchester Castlefield", "Crucible Steel-works (Sheffield)", "SS Great Britain (Bristol)"),
            details = "Celebrating heavy engineering confidence, canal towpaths, Victorian workshop ironmongery, and community brass bands."
        ),
        HeritageEra(
            id = "victorian",
            name = "Victorian & Edwardian High Golden Age",
            period = "1837 – 1910",
            description = "Confidence in red imperial brick and brass. Ornate municipal town halls, botanical glass houses, cast-iron seaside piers, steam railways, and Gothic Revival university towers representing the peak of national wealth.",
            sites = listOf("St Pancras Chambers", "Cardiff Castle Interiors", "Llandudno Victorian Pier", "Kew Botanical Glasshouse"),
            details = "Focused on ornate tiling, dark mahogany panelling, steam engines, and seaside promenades."
        )
    )

    val GRAND_TOURS = listOf(
        GrandTour(
            id = "cathedral_tour",
            name = "The Great Cathedral Tour",
            stops = listOf("Canterbury", "London", "York", "Durham"),
            duration = "5 Days",
            theme = "Spiritual Architecture",
            description = "A grand pilgrimage tracing the evolution of English sacred art, from the Romanesque choir arcades of Kent to the towering dramatic cloisters of the Anglo-Norman North.",
            dayByDay = listOf(
                "Day 1: Explore Canterbury Cathedral, the martyrdom site of Becket, and the ancient ruins of St. Augustine.",
                "Day 2: Take the high-speed rail to London. Marvel at St. Paul's Cathedral's massive dome and visit Westminster Abbey.",
                "Day 3: Journey north to York. Wander through York Minster, scaling its central tower for dales views.",
                "Day 4: Take local express to Durham, perched on a rocky cliff over the River Wear. Visit the shrine of St Cuthbert.",
                "Day 5: Participate in a Choral Evensong service in Durham's massive Norman nave."
            )
        ),
        GrandTour(
            id = "industrial_tour",
            name = "The Industrial Giants Tour",
            stops = listOf("Ironbridge", "Manchester", "Sheffield", "Liverpool"),
            duration = "6 Days",
            theme = "Heavy Engineering and Forge Heritage",
            description = "Witness the birthplace of modern civilization. Scale the world's first iron bridge, explore crimson brick textile mills in Manchester, and walk the historic river docks at the Mersey.",
            dayByDay = listOf(
                "Day 1: Visit Ironbridge Gorge in Shropshire, walking the world's first cast-iron monument built in 1779.",
                "Day 2: Travel to Manchester, the world's first industrial metropolis. Explore Castlefield canals and Science Museum.",
                "Day 3: Travel to Sheffield, City of Steel. Tour historic water-powered cutler wheels and modern artisan forge shops.",
                "Day 4: Take the rail to Liverpool, the great imperial seaport. Walk through the Grade-I listed Royal Albert Dock.",
                "Day 5: Visit the maritime museums and cruise the iconic Mersey River under the Royal Liver Building's shadow.",
                "Day 6: Conclude with a walking tour of the historic Liverpool overhead railway trail."
            )
        ),
        GrandTour(
            id = "coastal_tour",
            name = "The Coastal Croft & Cliffs Tour",
            stops = listOf("Cornwall", "Devon", "Northumberland", "Norfolk"),
            duration = "7 Days",
            theme = "Oceanic Borders & Castles",
            description = "Sweeping coastal exploration. From the sub-tropical floral paths of the Cornish peninsula to the wind-scoured sand dune fortresses of the far Northumbrian sea border.",
            dayByDay = listOf(
                "Day 1: Walk the rugged headlands of Cornwall, visiting the half-submerged island fortress of St. Michael's Mount.",
                "Day 2: Travel across Devon. Discover maritime Plymouth Hoe and taste seafood in historic Dartmouth harbor.",
                "Day 3: Rail transfer up north. Traverse coastal dunes towards Alnwick Castle and Bamburgh Castle on Northumberland cliffside.",
                "Day 4: Take a boat from Seahouses to the Farne Islands to see seals and colonial puffins.",
                "Day 5: Stop in the Norfolk Broads, sailing historic wooden yachts down peaceful reed-fringed currents.",
                "Day 6: Discover the crumbling cliff architecture and fossil-strewn coastlines of Cromer.",
                "Day 7: Conclude with premium local oysters at a traditional fishing pub in North Norfolk."
            )
        )
    )

    val HOSPITALITY = listOf(
        HospitalityVenue(
            name = "The Lygon Arms",
            type = "Historic Coaching Inn",
            region = "Cotswolds, Broadway",
            description = "A honey-colored limestone coaching house welcoming travelers since the 1300s. Boasting massive stone fireplaces where Oliver Cromwell slept before the Battle of Worcester.",
            specialty = "Dry-aged Cotswold Hereford Beef, local draft perry cider, and afternoon cream teas beside leaded windows."
        ),
        HospitalityVenue(
            name = "The Witchery by the Castle",
            type = "Historic Guild Rest",
            region = "Edinburgh, Old Town",
            description = "A gothic masterpiece of velvet, oak paneling, and heraldic ceilings nestled directly on the approach to Edinburgh Castle.",
            specialty = "Traditional Scottish Haggis with neeps and tatties, locally caught Aberdeen Angus beef, and aged single-malt Islay whisky."
        ),
        HospitalityVenue(
            name = "The Clachaig Inn",
            type = "Highland Rambler's Tavern",
            region = "Glencoe, Highlands",
            description = "A legendary haven for climbers and dynamic backpackers, cradled by jaw-dropping cliffs of Glencoe. Real peat coal logs burning warm.",
            specialty = "Caledonian Ales, Venison stew cooking in local red wine, and a choice selection of over eighty Scotch single-malts."
        ),
        HospitalityVenue(
            name = "Bettys Café Tea Rooms",
            type = "Traditional Yorkshire Salon",
            region = "Harrogate, Yorkshire",
            description = "Elegant Swiss-Yorkshire heritage room operating since 1919. Waitresses in black aprons and silver cake trolleys rolling to live piano keys.",
            specialty = "The Imperial Afternoon Tea, Fat Rascals (famous warm spiced fruit scones with cherries), and Taylors of Harrogate Yorkshire Gold tea."
        ),
        HospitalityVenue(
            name = "The Sheep Heid Inn",
            type = "Ancient Royal Pub",
            region = "Edinburgh, Duddingston",
            description = "Reputed to be Scotland’s oldest surviving pub, established in 1360. Visited by Mary Queen of Scots who played skittles in its unique old bowling alley.",
            specialty = "Traditional ale-battered North Sea Hadock with hand-cut dripping chips and local garden peas."
        )
    )

    val EVENTS = listOf(
        EventItem(
            name = "The Proms Classical Festival",
            location = "Royal Albert Hall, London",
            dateInfo = "Mid-July to Mid-September",
            description = "An eight-week summer extravaganza of daily classical music concerts, culminating in the patriotic raucous energy of the Last Night of the Proms.",
            category = "Music"
        ),
        EventItem(
            name = "The Edinburgh Festival Fringe",
            location = "Edinburgh Streets & Guild Halls",
            dateInfo = "Full month of August",
            description = "The world's largest arts and media festival, transforming Scotland's capital into a non-stop stage for physical theatre, standup, and performance art.",
            category = "Theatre"
        ),
        EventItem(
            name = "The Chelsea Flower Show",
            location = "Royal Hospital Chelsea, London",
            dateInfo = "Late May (Annual)",
            description = "The absolute Royal Horticultural Society masterpiece show. Avant-garde show gardens, rare orchids, and luxury pavilion designs attended by international royalty.",
            category = "Exhibition"
        ),
        EventItem(
            name = "The Great Dorset Steam Fair",
            location = "Blandford Forum, Dorset",
            dateInfo = "Late August Bank Holiday",
            description = "The world's largest heritage steam vehicle gathering. Hundreds of coal-fired traction engines, vintage fairgrounds, and iron horse-ploughing trials.",
            category = "Festival"
        )
    )

    val ARCHITECTURE_WALKS = listOf(
        ArchitecturalStyle(
            name = "Anglo-Norman Romanesque",
            period = "1066 – 1190 AD",
            description = "Defiant, monumental, heavy stone construction characterized by massive round drum columns, repeating chevron arches, and intimidating castle gate towers.",
            keyFeatures = listOf("Massive wall thickness", "Semi-circular barrel arches", "Intricate chevron chisel patterns", "Soaring central defense towers"),
            exemplaryBuildings = listOf("Durham Cathedral Nave", "Tower of London White Keep", "Rochester Norman Castle")
        ),
        ArchitecturalStyle(
            name = "Perpendicular English Gothic",
            period = "1350 – 1520 AD",
            description = "The peak of medieval glass and stone technology. Exclusively English style focusing on soaring vertical grid panel moldings, massive window walls, and incredibly complex stone fan vaults.",
            keyFeatures = listOf("Flamboyant lace-like fan vaults", "Vast glass window traceries", "Vertical linear stone moldings", "Slender soaring buttresses"),
            exemplaryBuildings = listOf("King's College Chapel (Cambridge)", "Henry VII Chapel (Westminster)", "Gloucester Cathedral Cloisters")
        ),
        ArchitecturalStyle(
            name = "High Palladian Georgian",
            period = "1714 – 1830 AD",
            description = "Harmonious royal Neoclassicism. Perfectly symmetrical limestone architectures inspired by ancient Greek temples, Roman baths, and the Italian drawings of Andrea Palladio.",
            keyFeatures = listOf("Mathematical symmetry", "Central triangular pediments", "Sash Windows", "Rusticated ground floors"),
            exemplaryBuildings = listOf("The Royal Crescent (Bath)", "Somerset House (London)", "Holkham Hall (Norfolk)")
        ),
        ArchitecturalStyle(
            name = "Anglo-Futurist Railway Architecture",
            period = "Late Victorian & 21st Century",
            description = "The sublime pairing of heavy cast-iron, massive arched glass roofs, and bold engineering. Bridging the mechanical peak of the Victorian steam transport with sleek high-speed networks.",
            keyFeatures = listOf("Vast single-span glass vaults", "Intricate cast-iron floral columns", "Polished copper & brass trim", "Polished dark brick walls"),
            exemplaryBuildings = listOf("St Pancras International (London)", "York Railway Station Curved Roof", "Ribblehead Pennine Masonry Viaduct")
        )
    )

    val CULTURAL_ATLAS = listOf(
        CulturalAsset(
            title = "The Literature of Landscape",
            category = "Literature",
            associatedBritons = "William Wordsworth, Emily Brontë, Beatrix Potter",
            description = "Celebrating the deep literary connection with local terrains. From the misty Westmorland mountain tops to the desolate, heather-scoured moors of Haworth.",
            localMonuments = "Wordsworth Grasmere Dove Cottage, Haworth Brontë Parsonage."
        ),
        CulturalAsset(
            title = "Cradle of Heavy Engineering",
            category = "Science & Engineering",
            associatedBritons = "Isambard Kingdom Brunel, George & Robert Stephenson",
            description = "Great minds who bound the nation in iron ribbons. Building the first commercial railways, massive iron transatlantic passenger ships, and daring tunnels.",
            localMonuments = "Clifton Suspension Bridge (Bristol), Rainhill Railway Trials (Lancashire)."
        ),
        CulturalAsset(
            title = "The Choral Heritage",
            category = "Music & Hymnody",
            associatedBritons = "Sir Edward Elgar, Ralph Vaughan Williams, Benjamin Britten",
            description = "Evocative, pastoral orchestral arrangements echoing green dales, cathedral resonances, and rolling maritime sea cliffs.",
            localMonuments = "The Three Choirs Festival (Hereford/Gloucester/Worcester), Snape Maltings Concert Hall (Suffolk)."
        )
    )
}
