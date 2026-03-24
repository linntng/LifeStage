DELETE FROM life_events;
DELETE FROM policies;
DELETE FROM policy_life_events;

INSERT INTO users (id, role, username) VALUES
('4d6164c5-4dae-4af3-8d24-ef11b88f7429', 'USER', 'userlinn'),
('ecd0b988-070d-48e5-8381-346530b0e9fa', 'ADMIN', 'adminoskar'),
('836c725f-bdc2-48f9-a304-f97460c2a626', 'CASE_HANDLER', 'casemartin'),
('22a19cc2-45a3-4d5d-8b5a-fd0d03ac8978', 'POLICY_MANAGER', 'polmantin'),
('c3a92ff4-b718-4ef9-b11f-b151fda617da', 'POLICY_MANAGER', 'polmankar');

INSERT INTO life_events (name, description)
VALUES
    ('marriage', 'Getting legally married to a partner'),
    ('childbirth', 'Birth or adoption of a child'),
    ('property purchase', 'Buying a house, apartment, or other property'),
    ('employment change', 'Starting a new job or changing employer'),
    ('temporary move abroad', 'Living abroad for a limited period of time');

    


INSERT INTO policies (name, premium, status, in_review) VALUES
-- Single life event policies
('Marriage Protection', 290.00, 'ACTIVE', false),
('Childbirth Support', 450.00, 'ACTIVE', false),
('Property Purchase Security', 2000.00, 'ACTIVE', false),
('Career Change Safety Net', 3000.00, 'ACTIVE', false),
('Temporary Relocation Cover', 450.00, 'ACTIVE', false),

-- Compound policies
('Family Bundle', 570.00, 'ACTIVE', false),
('Home & Career Package', 3900.00, 'ACTIVE', false),
('International Transition Plan', 3200.00, 'ACTIVE', false),
('Major Life Changes Package', 2390.00, 'ACTIVE', false),
('Complete Life Protection', 5600.00, 'ACTIVE', false);

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

   '22a19cc2-45a3-4d5d-8b5a-fd0d03ac8978',   

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


