# MediRoute Backend

Spring Boot backend for **MediRoute** — an Intelligent Decision Support
System (IDSS) for hospital and emergency healthcare logistics, built for
the PDSA (Programming, Data Structures and Algorithms) coursework.

The system models a city's emergency response network: hospitals,
ambulance depots, road junctions, and incoming patient incidents. Five
independent algorithmic modules each solve one computational problem, and
a sixth integration layer wires all five together into one real workflow
— report an incident, and the system automatically finds the best
hospital, plans the route, assigns an ambulance, and prepares a supply
loadout.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.x (Web MVC, Data JPA) |
| Database | PostgreSQL (Supabase), accessed via the session pooler |
| Build | Maven |
| Testing | JUnit 5 |

---

## Project Structure

```
src/main/java/com/mediroute/mediroutebackend/
├── MedirouteBackendApplication.java     <- Spring Boot entry point
├── common/                              <- shared domain model, used by every module
├── routing/                             <- Task 1: Route Optimization
├── allocation/                          <- Task 2: Resource Allocation
├── network/                             <- Task 3: Network Analysis
├── decision/                            <- Task 4: Intelligent Decision (hospital ranking)
├── optimization/                        <- Task 5: Dispatch Optimization
└── incident/                            <- Integration layer — wires all 5 modules together
```

Each of the five algorithm packages follows the same internal shape:
`model` (data carried in/out), `algorithm` or `graph` (the actual
algorithm classes — plain Java, no Spring dependency), `service` (loads
data from the database and calls the algorithm classes), `controller`
(REST endpoints), and `benchmark` (a standalone `main()` class for
performance testing, not part of the running app).

---

## Package-by-Package Guide

### `common/` — Shared Domain Model

Not a "task" of its own — this is the shared vocabulary every other
package depends on. Two sub-areas:

**`common/models/`** — JPA entities and enums mapped directly to database
tables:

| Class | Represents |
|---|---|
| `NetworkNode` + `NodeType` | A point in the city graph (hospital, depot, or plain road junction) |
| `RoadEdge` | A road connecting two `NetworkNode`s, with distance and travel time |
| `Hospital` | A hospital's specialty, bed counts — linked 1-to-1 to a `NetworkNode` |
| `AmbulanceDepot` | An ambulance depot's fleet size — also linked 1-to-1 to a `NetworkNode` |
| `Resource` + `ResourceType` + `ResourceStatus` + `OwnerType` | One physical, allocatable item (an ambulance, an ICU bed, a ventilator) with a status (`AVAILABLE` / `IN_USE` / `MAINTENANCE`) |
| `PatientIncident` + `IncidentStatus` | One reported emergency, with location, condition, severity, phone number, and current status |
| `SupplyItem` + `SupplyItemStatus` | A medical supply crate or patient-transfer task waiting to be loaded onto a vehicle |
| `DispatchPlan` + `DispatchPlanItem` | A permanent record of one dispatch decision — which incident, which algorithm, which items were loaded |

**`common/models/repository/`** — one Spring Data JPA repository interface
per entity above (e.g. `HospitalRepository`, `ResourceRepository`). These
are the only classes that ever talk to the database directly; every
`service` class below calls into these, never raw SQL.

Why this package exists separately: `Graph`, `DijkstraRouter`,
`ArticulationPointFinder`, and every other algorithm class never import
anything from `common` — they work on plain in-memory data (see `routing/model/Node`
vs `common/models/NetworkNode` below). Only the `service` layer of each
module touches `common`. This keeps every algorithm class fully
independent of the database, Spring, and each other — they could be
copy-pasted into a plain Java project with zero changes.

---

### `routing/` — Task 1: Intelligent Route Optimization

**Problem:** find the fastest route between two points in the road network.

| Class | Role |
|---|---|
| `model/Node`, `model/Edge` | Lightweight in-memory graph pieces — deliberately separate from `common/models/NetworkNode`/`RoadEdge` so the algorithms below have no database dependency |
| `graph/Graph` | Adjacency-list representation of the road network |
| `graph/GraphLoaderService` | Reads `NetworkNode`/`RoadEdge` from the database (via `common`'s repositories) and builds a `Graph` |
| `graph/DijkstraRouter` | Dijkstra's algorithm — guaranteed shortest path, O((V+E) log V), using a min-heap |
| `graph/AStarRouter` | A* search — same guarantee, faster in practice via a Haversine-distance heuristic |
| `service/RoutingService` | Loads the graph, runs either algorithm, exposes a same-shape `RouteResult` for both so they can be compared directly |
| `controller/RoutingController` | `GET /api/routing/ping`, `GET /api/routing/nodes`, `POST /api/routing/shortest-path`, `POST /api/routing/compare` |
| `config/WebConfig` | CORS configuration — allows the Next.js frontend (port 3000) to call this API. This is the **only** `WebConfig` in the whole project; it applies to every module's endpoints, not just routing's |
| `benchmark/BenchmarkRunner` | Standalone class — generates random graphs of increasing size and times both algorithms, for the experimental evaluation |

---

### `allocation/` — Task 2: Intelligent Resource Allocation

**Problem:** given limited ambulances and multiple pending incidents,
decide who actually gets a resource.

| Class | Role |
|---|---|
| `model/AllocationRequest` | One incident, reshaped as a knapsack "item" (value = severity, weight = resource units needed) |
| `algorithm/GreedyAllocator` | Ranks incidents by severity-per-resource-unit using a max-heap, assigns greedily — fast, O(n log n), not always optimal |
| `algorithm/KnapsackAllocator` | 0/1 Knapsack Dynamic Programming — guaranteed optimal, O(n × capacity) |
| `service/AllocationService` | Loads pending incidents + available resource counts from `common`, runs either or both algorithms |
| `controller/AllocationController` | `GET /ping`, `GET /pending-requests`, `POST /assign`, `POST /compare` |
| `benchmark/AllocationBenchmarkRunner` | Measures both the speed AND the "quality gap" — how much value Greedy leaves on the table compared to the DP-optimal answer |

---

### `network/` — Task 3: Network Analysis

**Problem:** which roads/junctions are critical, and how connected is
each hospital?

| Class | Role |
|---|---|
| `algorithm/ArticulationPointFinder` | Iterative DFS (Tarjan's algorithm) — finds every "single point of failure" junction, O(V+E) |
| `algorithm/MSTBuilder` | Prim's algorithm — the cheapest possible set of roads that still connects every node (the network's backbone) |
| `algorithm/CentralityCalculator` | Degree centrality — ranks nodes by how many roads connect to them directly |
| `service/NetworkAnalysisService` | Loads the same `Graph` structure Task 1 built (via `GraphLoaderService`) and runs all three algorithms on it |
| `controller/NetworkAnalysisController` | `GET /ping`, `/critical-nodes`, `/mst`, `/centrality-ranking` |
| `benchmark/NetworkBenchmarkRunner` | Confirms the O(V+E) claim empirically across increasing graph sizes |

**Note:** this package directly reuses Task 1's `Graph`/`GraphLoaderService`
classes — a real example of module integration, not just five separate
demos bolted together.

---

### `decision/` — Task 4: Intelligent Decision (Hospital Ranking)

**Problem:** given a patient's condition and location, which hospital is
the best match?

| Class | Role |
|---|---|
| `util/GeoUtils` | Haversine formula — straight-line distance between two lat/long points |
| `algorithm/ScoringEngine` | Combines specialty match, distance, and bed availability into one weighted score |
| `algorithm/HospitalRecommender` | Two ways to pick the top-k highest-scoring hospitals: a bounded min-heap (O(n log k)) and a full sort (O(n log n)) baseline — both always return the *same* hospitals, this comparison is about speed only, not correctness |
| `service/RecommendationService` | Loads all hospitals, scores them against an incoming request, runs either selection method |
| `controller/RecommendationController` | `GET /ping`, `POST /recommend`, `POST /compare` |
| `benchmark/RecommendationBenchmarkRunner` | Shows the heap's advantage widening as hospital count grows while k stays fixed |

---

### `optimization/` — Task 5: Dispatch Optimization

**Problem:** given a vehicle's capacity and a set of pending supply
items, what should it carry? (The richest module — three algorithms
compared directly: exact, exact-with-pruning, and approximate.)

| Class | Role |
|---|---|
| `algorithm/KnapsackDPOptimizer` | 0/1 Knapsack DP — exact, O(n × capacity) |
| `algorithm/BacktrackingOptimizer` | Branch-and-bound — also exact, explores the decision tree directly with a fractional-relaxation pruning bound; worst case O(2ⁿ), often much faster in practice |
| `algorithm/GreedyOptimizer` | Value-density greedy — approximate, always O(n log n) |
| `service/OptimizationService` | Loads pending `SupplyItem`s, runs one or all three algorithms; **caps Backtracking to inputs of 25 items or fewer** as a deliberate safety net against its worst-case blowup |
| `controller/OptimizationController` | `GET /ping`, `POST /optimize`, `POST /compare` |
| `benchmark/OptimizationBenchmarkRunner` | Produces two separate result sets: a small-scale one including all three algorithms, and a large-scale one (DP vs Greedy only) — the split itself is the evidence for why Backtracking is capped |

---

### `incident/` — Integration Layer

**This is where the five modules stop being independent demos and become
one real system.** Not one of the five coursework tasks — this is the
"System Integration" work.

| Class | Role |
|---|---|
| `service/IncidentOrchestrationService` | The core of the whole app. See the flow diagram below |
| `service/ResetService` | Clears all reported incidents and restores every resource/supply item to its starting state — used between demo runs, triggered from the Admin Panel |
| `controller/IncidentController` | `POST /api/incident/report` (the main workflow), `GET /api/incident/dashboard-summary`, `GET /api/incident/list` (admin verification list — see security note below) |
| `controller/AdminController` | `POST /api/admin/reset-demo-data` |
| `model/*` | DTOs specific to the combined incident workflow (`IncidentReportRequest`, `IncidentResponse`, `DispatchPlanView`, `DashboardSummary`, `HospitalStatusView`, `IncidentSummaryView`) |

**`GET /api/incident/list`** returns every incident including phone
number, for the frontend Admin Panel to verify a report is genuine before
committing more resources to it.

> ⚠️ **Security note:** this endpoint is not protected at the Spring Boot
> API level — the admin login (Supabase Auth) only gates the frontend
> *page*, not this URL itself. Anyone who knows or guesses the URL could
> call it directly and see phone numbers. This is a documented, accepted
> limitation for this coursework project, consistent with the rest of the
> app having no server-side authentication. A production system would add
> Spring Security to protect `/api/admin/**` and `/api/incident/list`.

#### The actual integration flow — what happens on `POST /api/incident/report`

```
 1. PatientIncident saved to DB, status = PENDING
        │
 2. decision.RecommendationService.recommend(...)
        -> best-matching Hospital (specialty + distance + bed availability)
        │
 3. routing.RoutingService.computeRoute(...)
        -> fastest route from the ambulance depot to that hospital (A*)
        │
 4. allocation.AllocationService.runAllocation(...)
        -> does THIS incident actually get an ambulance, given every other
           pending incident competing for the same limited resources?
        │
    ┌───┴────────────────────────────────┐
    │ YES: ambulance available           │ NO: none available
    ▼                                    ▼
 4b. One Resource row flipped        DispatchPlanView returned with a
     AVAILABLE → IN_USE (real        `note` explaining why — no vehicle
     depletion, not just a           exists, so no supply plan is
     calculation)                    computed
        │
 5. optimization.OptimizationService.optimize(...)
        -> which supply items to load (DP by default)
        -> saved permanently to DispatchPlan / DispatchPlanItem
        -> each selected SupplyItem flipped PENDING → LOADED
        │
 6. network.NetworkAnalysisService.findCriticalNodes()
        -> does the chosen route pass through a known single point of
          failure? (a safety warning, not a blocking check)
        │
 7. One combined IncidentResponse returned to the frontend
```

Every arrow above is a real method call between two previously-independent
modules' `service` classes — this is literally what Group Report Chapter 8
(System Integration) should describe.

---

## Admin Panel — Architecture Note

The frontend's Admin Panel (login, reset, adding ambulances/resources/
supply items/incident locations, viewing incidents for verification) is
**deliberately split across two different paths**, not all through this
backend:

| Feature | Goes through |
|---|---|
| Login | Supabase Auth directly (no Spring Boot involvement at all) |
| Reset Demo Data | `POST /api/admin/reset-demo-data` (this backend) |
| Incident list (with phone numbers) | `GET /api/incident/list` (this backend) |
| Add ambulance / resource / supply item / incident location | Supabase's own auto-generated REST API, called directly from the frontend, bypassing this backend entirely |

**Why:** the reset and incident-list features involve business logic and
core domain data already owned by this backend's services and entities,
so routing them through here is the natural, consistent choice. Adding a
new resource or supply item row, by contrast, is a plain database insert
with no algorithm involved — routing it through Spring Boot would add a
controller/service/DTO for each just to forward a straight insert.
Supabase's Row Level Security (configured in
`database/08-schema-addition-admin-features.sql`) enforces that only a
logged-in admin can write to those tables, even though the write bypasses
this backend.

Worth stating plainly in your Group Report's critical evaluation: mixing
two data-access paths for the same overall "admin" feature is a deliberate
speed/simplicity trade-off for coursework scope, not something you'd
necessarily do in a larger production system, where you'd more likely
route everything through one consistent API layer.

---

## Running Locally

1. Create a Supabase project (or use the team's shared one).
2. Run the database scripts, in order (01 through 09), in Supabase's SQL
   Editor. Scripts 08 and 09 are new — they add phone number support,
   the admin-manageable incident locations table, and Row Level Security
   policies for the admin panel's direct-Supabase features.
3. **Enable admin login:** Supabase Dashboard → Authentication → Providers
   → confirm "Email" is enabled → Authentication → Users → Add User,
   create one admin account (email + password). This is the only account
   able to sign in to the frontend's `/admin/login`.
4. Copy `application.properties.example` → `application.properties`,
   fill in your Supabase pooler host, username, and password.
5. `mvn clean install` (or let IntelliJ do it on open).
6. Run `MedirouteBackendApplication`.
7. Confirm it's working: `GET http://localhost:8080/api/routing/ping`.

## Running the Benchmarks

Each `benchmark/` class has a standalone `main()` method — right-click →
Run in IntelliJ. Results are written as CSV files (see `benchmark-results/`
for examples already generated) — open them in Excel/Sheets to build the
charts needed for each Individual Report's Chapter 8.

## Testing

`mvn test` runs the full JUnit suite — one test class per algorithm,
covering correctness against hand-computed examples and, where two
algorithms both claim to be exact (e.g. Knapsack DP vs Backtracking), a
cross-check proving they always agree.
