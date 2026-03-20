DELETE FROM life_events;
DELETE FROM policies;
DELETE FROM policy_life_events;

INSERT INTO life_events (name, description)
VALUES
    ('marriage', 'Getting legally married to a partner'),
    ('childbirth', 'Birth or adoption of a child'),
    ('property purchase', 'Buying a house, apartment, or other property'),
    ('employment change', 'Starting a new job or changing employer'),
    ('temporary move abroad', 'Living abroad for a limited period of time');

    


INSERT INTO policies (id, name, premium, status) VALUES
-- Single life event policies
(1, 'Marriage Protection', 25.00, 'ACTIVE'),
(2, 'Childbirth Support', 35.00, 'ACTIVE'),
(3, 'Property Purchase Security', 40.00, 'ACTIVE'),
(4, 'Career Change Safety Net', 30.00, 'ACTIVE'),
(5, 'Temporary Relocation Cover', 45.00, 'ACTIVE'),

-- Compound policies
(6, 'Family Bundle', 65.00, 'ACTIVE'),
(7, 'Home & Career Package', 70.00, 'ACTIVE'),
(8, 'International Transition Plan', 80.00, 'ACTIVE'),
(9, 'Major Life Changes Package', 95.00, 'ACTIVE'),
(10, 'Complete Life Protection', 120.00, 'ACTIVE');

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

-- No delete on policy table
CREATE OR REPLACE FUNCTION prevent_delete()
RETURNS trigger AS $$
BEGIN
    RAISE EXCEPTION 'Deletion is not allowed on table %', TG_TABLE_NAME;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER no_delete_trigger
BEFORE DELETE ON policies
FOR EACH ROW
EXECUTE FUNCTION prevent_delete();