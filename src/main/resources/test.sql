BEGIN;

INSERT INTO app_user (name, email, password) VALUES
('Rider', 'rider@gmail.com', 'ride'),
('Rider2', 'rider2@gmail.com', 'ride'),
('Driver', 'driver@gmail.com', 'drive'),
('Driver2', 'driver2@gmail.com', 'drive');

INSERT INTO user_roles (user_id, roles) VALUES
(1, 'RIDER'),
(2, 'RIDER'),
(3, 'RIDER'),
(3, 'DRIVER'),
(4, 'RIDER'),
(4, 'DRIVER');


INSERT INTO rider (user_id, rating) VALUES (1, 0.0);
INSERT INTO rider (user_id, rating) VALUES (2, 0.0);

INSERT INTO driver (user_id, rating, available, vehicle_id, current_location)
VALUES
(
    3,
    0.0,
    TRUE,
    '3897',
    ST_SetSRID(ST_MakePoint(12.9720, 77.5940), 4326)
),
(
    4,
    0.0,
    FALSE,
    '3896',
    ST_SetSRID(ST_MakePoint(18.9720, 80.5940), 4326)
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
    '1234',
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
    'ONGOING',
    '4567',
    300.00,
    NOW()
);

INSERT INTO ride (pick_up_location, drop_off_location, created_time, rider_id, driver_id, payment_method, ride_status, otp, fare, started_at)
VALUES
(
    ST_SetSRID(ST_MakePoint(12.9716, 77.5946), 4326),
    ST_SetSRID(ST_MakePoint(13.0350, 77.5850), 4326),
    NOW(),
    2,
    2,
    'CASH',
    'CONFIRMED',
    '4567',
    300.00,
    NOW()
);

INSERT INTO ride_request (pick_up_location, drop_off_location, requested_time, rider_id, payment_method, ride_request_status, fare)
VALUES (
    ST_SetSRID(ST_MakePoint(12.9716, 77.5946), 4326),
    ST_SetSRID(ST_MakePoint(13.0350, 77.5850), 4326),
    CURRENT_TIMESTAMP,
    1 ,
    'CASH',
    'PENDING',
    25.50
);

INSERT INTO rating (ride_id, rider_id, driver_id)
VALUES
(
    1,
    1,
    1
);

INSERT INTO wallet (user_id, balance)
VALUES (1, 500.00),
       (2, 500.00),
       (3, 500.00),
       (4, 500.00);

INSERT INTO payment (payment_method, ride_id, amount, payment_status)
VALUES (
    'CASH',
    2,
    300.00,
    'PENDING'
);

COMMIT;
