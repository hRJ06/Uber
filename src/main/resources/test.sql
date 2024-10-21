INSERT INTO app_user (name, email, password) VALUES
('Rider', 'rider@gmail.com', 'ride'),
('Driver', 'driver@gmail.com', 'drive');

INSERT INTO user_roles (user_id, roles) VALUES
(1, 'RIDER'),
(2, 'RIDER'),
(2, 'DRIVER');


INSERT INTO rider (user_id, rating) VALUES (1, 0.0);
INSERT INTO driver (user_id, rating, available, vehicle_id, current_location) VALUES (2, 0.0, TRUE, '3897', ST_SetSRID(ST_MakePoint(12.9720, 77.5940), 4326));