-- ============================================================
-- MediRoute - Database Schema (DDL) for Supabase (PostgreSQL)
-- Run this in: Supabase Dashboard -> SQL Editor -> New Query -> paste -> Run
-- Owner: Shehan
-- ============================================================

DROP TABLE IF EXISTS patient_incident CASCADE;
DROP TABLE IF EXISTS resource CASCADE;
DROP TABLE IF EXISTS road_edge CASCADE;
DROP TABLE IF EXISTS ambulance_depot CASCADE;
DROP TABLE IF EXISTS hospital CASCADE;
DROP TABLE IF EXISTS network_node CASCADE;

-- ------------------------------------------------------------
-- network_node: every point in the shared graph (Task 1, Task 3)
-- ------------------------------------------------------------
CREATE TABLE network_node (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(150)        NOT NULL,
    node_type   VARCHAR(20)         NOT NULL,   -- HOSPITAL | DEPOT | JUNCTION
    latitude    DOUBLE PRECISION    NOT NULL,
    longitude   DOUBLE PRECISION    NOT NULL
);

-- ------------------------------------------------------------
-- hospital: 1-to-1 with a network_node of type HOSPITAL
-- ------------------------------------------------------------
CREATE TABLE hospital (
    id                  BIGSERIAL PRIMARY KEY,
    node_id             BIGINT          NOT NULL UNIQUE REFERENCES network_node(id),
    specialty           VARCHAR(255),               -- comma-separated tags
    total_beds          INT             NOT NULL DEFAULT 0,
    available_beds      INT             NOT NULL DEFAULT 0,
    total_icu_beds      INT             NOT NULL DEFAULT 0,
    available_icu_beds  INT             NOT NULL DEFAULT 0
);

-- ------------------------------------------------------------
-- ambulance_depot: 1-to-1 with a network_node of type DEPOT
-- ------------------------------------------------------------
CREATE TABLE ambulance_depot (
    id                      BIGSERIAL PRIMARY KEY,
    node_id                 BIGINT      NOT NULL UNIQUE REFERENCES network_node(id),
    total_ambulances        INT         NOT NULL DEFAULT 0,
    available_ambulances    INT         NOT NULL DEFAULT 0
);

-- ------------------------------------------------------------
-- road_edge: connections between nodes (Task 1, Task 3)
-- ------------------------------------------------------------
CREATE TABLE road_edge (
    id                      BIGSERIAL PRIMARY KEY,
    from_node_id            BIGINT              NOT NULL REFERENCES network_node(id),
    to_node_id              BIGINT              NOT NULL REFERENCES network_node(id),
    distance_km             DOUBLE PRECISION    NOT NULL,
    travel_time_minutes     DOUBLE PRECISION    NOT NULL,
    bidirectional           BOOLEAN             NOT NULL DEFAULT TRUE
);

-- ------------------------------------------------------------
-- resource: individual ambulances / beds / ventilators (Task 2, Task 5)
-- ------------------------------------------------------------
CREATE TABLE resource (
    id              BIGSERIAL PRIMARY KEY,
    resource_type   VARCHAR(20)     NOT NULL,  -- AMBULANCE | ICU_BED | WARD_BED | VENTILATOR
    owner_type      VARCHAR(20)     NOT NULL,  -- HOSPITAL | DEPOT
    owner_id        BIGINT          NOT NULL,  -- references hospital.id or ambulance_depot.id
    status          VARCHAR(20)     NOT NULL DEFAULT 'AVAILABLE' -- AVAILABLE | IN_USE | MAINTENANCE
);

-- ------------------------------------------------------------
-- patient_incident: incoming requests (Task 2, Task 4)
-- ------------------------------------------------------------
CREATE TABLE patient_incident (
    id                      BIGSERIAL PRIMARY KEY,
    patient_reference       VARCHAR(100),
    latitude                DOUBLE PRECISION    NOT NULL,
    longitude               DOUBLE PRECISION    NOT NULL,
    condition_type          VARCHAR(50),
    severity_score          INT                 NOT NULL,
    status                  VARCHAR(20)         NOT NULL DEFAULT 'PENDING', -- PENDING | ASSIGNED | EN_ROUTE | COMPLETED
    assigned_hospital_id    BIGINT              REFERENCES hospital(id),
    assigned_resource_id    BIGINT              REFERENCES resource(id),
    created_at              TIMESTAMP           DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- indexes
-- ------------------------------------------------------------
CREATE INDEX idx_edge_from        ON road_edge(from_node_id);
CREATE INDEX idx_edge_to          ON road_edge(to_node_id);
CREATE INDEX idx_incident_status  ON patient_incident(status);
CREATE INDEX idx_resource_owner   ON resource(owner_type, owner_id);
CREATE INDEX idx_resource_status  ON resource(status);
