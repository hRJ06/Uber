
INSERT INTO app_user (name, email, password) VALUES
('Rider', 'rider@gmail.com', 'ride'),
('Driver', 'driver@gmail.com', 'drive');


INSERT INTO user_roles (user_id, roles) VALUES
(1, 'RIDER'),
(2, 'RIDER'),
(2, 'DRIVER');

INSERT INTO rider (user_id, rating) VALUES (1, 0.0);

INSERT INTO driver (user_id, rating, available, vehicle_id, current_location)
VALUES
(
    2,
    0.0,
    TRUE,
    '3897',
    ST_SetSRID(ST_MakePoint(12.9720, 77.5940), 4326)
);

INSERT INTO ride (pick_up_location, drop_off_location, created_time, rider_id, driver_id, payment_method, ride_status, otp, fare, started_at, ended_at)
VALUES
(
    ST_SetSRID(ST_MakePoint(12.9716, 77.5946), 4326),
    ST_SetSRID(ST_MakePoint(13.0350, 77.5850), 4326),
    NOW(),
    1,
    1,
    'CASH',
    'ENDED',
    '123456',
    300.00,
    NOW(),
    NOW() + INTERVAL '30 minutes'
);

INSERT INTO ride (pick_up_location, drop_off_location, created_time, rider_id, driver_id, payment_method, ride_status, otp, fare, started_at)
VALUES
(
    ST_SetSRID(ST_MakePoint(12.9716, 77.5946), 4326),
    ST_SetSRID(ST_MakePoint(13.0350, 77.5850), 4326),
    NOW(),
    1,
    1,
    'CASH',
    'CONFIRMED',
    '123456',
    300.00,
    NOW()
);

INSERT INTO rating (ride_id, rider_id, driver_id)
VALUES
(
    1,
    1,
    1
);

