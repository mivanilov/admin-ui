INSERT INTO admin_ui.user (email, name, role)
VALUES ('michail.ivanilov@gmail.com', 'Michail Ivanilov', 'ADMIN');

INSERT INTO admin_ui.example_config_type (type, description)
VALUES ('T1', 'Type 1'),
       ('T2', 'Type 2'),
       ('T3', 'Type 3');

CREATE OR REPLACE PROCEDURE admin_ui.insert_example_config(IN count INTEGER) AS
$procedure$
DECLARE
    i                            INTEGER := 0;
    visibility_value             admin_ui.VISIBILITY_ENUM;
    active_value                 BOOLEAN;
    example_config_type_id_value INTEGER;
BEGIN
    WHILE i < count
        LOOP
            i := i + 1;

            IF MOD(i, 2) = 0 THEN
                visibility_value := 'PUBLIC';
                active_value := false;
                example_config_type_id_value := 1;
            ELSE
                visibility_value := 'PRIVATE';
                active_value := true;
                example_config_type_id_value := 2;
            END IF;

            INSERT INTO admin_ui.example_config (name, visibility, create_date, active, example_config_type_id)
            VALUES (CONCAT('Example Config ', i), visibility_value, (CURRENT_DATE - i * INTERVAL '1 DAY'), active_value, example_config_type_id_value);
        END LOOP;
END
$procedure$ LANGUAGE plpgsql;
CALL admin_ui.insert_example_config(200);
