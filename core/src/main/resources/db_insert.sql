-- Inserting sample gift certificates
INSERT INTO gift_certificates (name, description, price, duration, created_date, last_updated_date)
VALUES ('Certificate 1', 'Description 1', 59.99, 3.0, '2023-12-01 10:00:00', '2023-12-01 10:00:00'),
       ('Certificate 2', 'Description 2', 39.99, 7.0, '2023-12-02 11:00:00', '2023-12-02 11:00:00'),
       ('Certificate 3', 'Description 3', 19.99, 15.0, '2023-12-03 12:00:00', '2023-12-03 12:00:00');

-- Inserting sample tags
INSERT INTO tags (name)
VALUES ('Tag 1'),
       ('Tag 2'),
       ('Tag 3');

-- Associating tags with gift certificates
INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 3);
