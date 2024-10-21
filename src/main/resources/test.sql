INSERT INTO app_user (name, email, password) VALUES
('Rider', 'rider@gmail.com', 'ride'),
('Driver', 'driver@gmail.com', 'drive'),
('Driver', 'driver2@gmail.com', 'drive');

INSERT INTO user_roles (user_id, roles) VALUES
(1, 'RIDER'),
(2, 'RIDER'),
(2, 'DRIVER'),
(3, 'RIDER'),
(3, 'DRIVER');


INSERT INTO rider (user_id, rating) VALUES (1, 0.0);
INSERT INTO driver (user_id, rating, available, vehicle_id, current_location) VALUES
(2, 0.0, TRUE, '3897', ST_SetSRID(ST_MakePoint(74.1213, 28.234123), 4326)),
(3, 0.0, TRUE, '3898', ST_SetSRID(ST_MakePoint(76.1213, 29.234123), 4326));

INSERT INTO wallet (user_id, balance) VALUES
(1, 500.00),
(2, 500.00);