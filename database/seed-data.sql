-- ============================================================
-- MediRoute - Seed Data for Supabase (PostgreSQL)
-- Run AFTER schema.sql, in the same SQL Editor.
-- NOTE: node ids 1-5 intentionally match RoutingService's fallback sample
-- graph, so results stay consistent whether the app reads from Supabase
-- or falls back to the hardcoded sample.
-- Owner: Shehan
-- ============================================================

INSERT INTO network_node (id, name, node_type, latitude, longitude) VALUES
(1, 'City Hospital',      'HOSPITAL', 6.9271, 79.8612),
(2, 'Junction A',         'JUNCTION', 6.9310, 79.8650),
(3, 'Junction B',         'JUNCTION', 6.9350, 79.8700),
(4, 'Ambulance Depot 1',  'DEPOT',    6.9200, 79.8550),
(5, 'General Hospital',   'HOSPITAL', 6.9400, 79.8750);

INSERT INTO hospital (id, node_id, specialty, total_beds, available_beds, total_icu_beds, available_icu_beds) VALUES
(1, 1, 'CARDIAC,GENERAL',  120, 34, 12, 3),
(2, 5, 'TRAUMA,GENERAL',   200, 58, 20, 7);

INSERT INTO ambulance_depot (id, node_id, total_ambulances, available_ambulances) VALUES
(1, 4, 6, 4);

INSERT INTO road_edge (from_node_id, to_node_id, distance_km, travel_time_minutes, bidirectional) VALUES
(4, 2, 2.5, 4.0, TRUE),
(2, 1, 1.8, 3.0, TRUE),
(2, 3, 3.2, 5.0, TRUE),
(3, 5, 2.0, 3.5, TRUE),
(4, 3, 5.0, 8.0, TRUE);

INSERT INTO resource (resource_type, owner_type, owner_id, status) VALUES
('AMBULANCE', 'DEPOT',    1, 'AVAILABLE'),
('AMBULANCE', 'DEPOT',    1, 'AVAILABLE'),
('AMBULANCE', 'DEPOT',    1, 'IN_USE'),
('ICU_BED',   'HOSPITAL', 1, 'AVAILABLE'),
('ICU_BED',   'HOSPITAL', 1, 'IN_USE'),
('VENTILATOR','HOSPITAL', 2, 'AVAILABLE'),
('WARD_BED',  'HOSPITAL', 2, 'AVAILABLE');

INSERT INTO patient_incident (patient_reference, latitude, longitude, condition_type, severity_score, status) VALUES
('INC-001', 6.9285, 79.8625, 'CARDIAC', 9, 'PENDING'),
('INC-002', 6.9420, 79.8770, 'TRAUMA',  7, 'PENDING'),
('INC-003', 6.9235, 79.8580, 'GENERAL', 3, 'PENDING');

-- Reset auto-increment sequences past the explicit ids we just inserted,
-- so future INSERTs (without explicit ids) don't collide.
SELECT setval(pg_get_serial_sequence('network_node', 'id'), (SELECT MAX(id) FROM network_node));
SELECT setval(pg_get_serial_sequence('hospital', 'id'), (SELECT MAX(id) FROM hospital));
SELECT setval(pg_get_serial_sequence('ambulance_depot', 'id'), (SELECT MAX(id) FROM ambulance_depot));
