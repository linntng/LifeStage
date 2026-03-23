DELETE FROM life_events;
DELETE FROM policies;
DELETE FROM policy_life_events;

INSERT INTO users (id, role, username) VALUES
('fa44cb5f-78d3-4b26-9e71-02268106af2e', 'USER', 'evillinn'),
('e2f63b33-c182-49b2-a56e-a032ef91db93', 'ADMIN', 'evilmartin'),
('e30a72fb-6928-4ed0-a6cf-897ac71b6ea4', 'CASE_HANDLER', 'eviloskar'),
('5581809f-6205-4e13-bf7d-b9d094c31953', 'POLICY_MANAGER', 'chaososkar'),
('438573b2-e2ec-401f-8896-b70b24848ddc', 'POLICY_MANAGER', 'chaosmartin');

INSERT INTO life_events (name, description)
VALUES
    ('marriage', 'Getting legally married to a partner'),
    ('childbirth', 'Birth or adoption of a child'),
    ('property purchase', 'Buying a house, apartment, or other property'),
    ('employment change', 'Starting a new job or changing employer'),
    ('temporary move abroad', 'Living abroad for a limited period of time');

    


INSERT INTO policies (name, premium, status, in_review) VALUES
-- Single life event policies
('Marriage Protection', 25.00, 'ACTIVE', false),
('Childbirth Support', 35.00, 'ACTIVE', false),
('Property Purchase Security', 40.00, 'ACTIVE', false),
('Career Change Safety Net', 30.00, 'ACTIVE', false),
('Temporary Relocation Cover', 45.00, 'ACTIVE', false),

-- Compound policies
('Family Bundle', 65.00, 'ACTIVE', false),
('Home & Career Package', 70.00, 'ACTIVE', false),
('International Transition Plan', 80.00, 'ACTIVE', false),
('Major Life Changes Package', 95.00, 'ACTIVE', false),
('Complete Life Protection', 120.00, 'ACTIVE', false);

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

INSERT INTO policy_manager_actions (

    manager_id,

    origin_policy_id,

    suggested_policy_id,

    created_at

)

SELECT

   '5581809f-6205-4e13-bf7d-b9d094c31953',   

    NULL,

    p.id,

    NOW()                

FROM policies p;

-- No delete on policy table
CREATE OR REPLACE FUNCTION prevent_delete()
RETURNS trigger AS $$
BEGIN
    RAISE EXCEPTION 'Deletion is not allowed on table %', TG_TABLE_NAME;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER no_delete_trigger
BEFORE DELETE ON policies
FOR EACH ROW
EXECUTE FUNCTION prevent_delete();


