DELETE FROM life_events;
INSERT INTO life_events (name)
VALUES
    ('marriage'),
    ('childbirth'),
    ('property purchase'),
    ('employment change'),
    ('temporary move abroad');

    
DELETE FROM policy_life_events;
DELETE FROM policies;

INSERT INTO policies (id, name, premium, status) VALUES
-- Single life event policies
(1, 'Marriage Protection', 25.00, 'DRAFT'),
(2, 'Childbirth Support', 35.00, 'DRAFT'),
(3, 'Property Purchase Security', 40.00, 'DRAFT'),
(4, 'Career Change Safety Net', 30.00, 'DRAFT'),
(5, 'Temporary Relocation Cover', 45.00, 'DRAFT'),

-- Compound policies
(6, 'Family Bundle', 65.00, 'DRAFT'),
(7, 'Home & Career Package', 70.00, 'DRAFT'),
(8, 'International Transition Plan', 80.00, 'DRAFT'),
(9, 'Major Life Changes Package', 95.00, 'DRAFT'),
(10, 'Complete Life Protection', 120.00, 'DRAFT');

INSERT INTO policy_life_events (policy_id, life_event_id) VALUES

-- Single-event policies
(1,1),
(2,2),
(3,3),
(4,4),
(5,5),

-- Family bundle
(6,1),
(6,2),

-- Home & Career
(7,3),
(7,4),

-- International transition
(8,4),
(8,5),

-- Major life changes
(9,1),
(9,2),
(9,3),

-- Complete coverage
(10,1),
(10,2),
(10,3),
(10,4),
(10,5);