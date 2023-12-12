-- Inserting data into gift_certificates table
INSERT INTO gift_certificates (id, name, description, price, duration, create_date, last_update_date)
VALUES (1, 'Gift Certificate 1', 'Description 1', 180.75, 6, '2020-06-01 12:00:00', '2020-06-07 12:30:00');
INSERT INTO gift_certificates (id, name, description, price, duration, create_date, last_update_date)
VALUES (2, 'Gift Certificate 2', 'Description 2', 120.99, 4, '2021-02-15 08:30:00', '2021-02-19 09:45:00');
INSERT INTO gift_certificates (id, name, description, price, duration, create_date, last_update_date)
VALUES (3, 'Gift Certificate 3', 'Description 3', 99.99, 3, '2023-11-20 09:00:00', '2023-11-22 09:15:00');

-- Inserting data into tags table
INSERT INTO tags (id, name)
VALUES (1, 'Tag 1');
INSERT INTO tags (id, name)
VALUES (2, 'Tag 2');
INSERT INTO tags (id, name)
VALUES (3, 'Tag 3');
INSERT INTO tags (id, name)
VALUES (4, 'Tag 4');
INSERT INTO tags (id, name)
VALUES (5, 'Tag 5');

-- Inserting data into gift_certificate_tags table
INSERT INTO gift_certificate_tags (id, gift_certificate_id, tag_id)
VALUES (1, 1, 1);
INSERT INTO gift_certificate_tags (id, gift_certificate_id, tag_id)
VALUES (2, 1, 2);
INSERT INTO gift_certificate_tags (id, gift_certificate_id, tag_id)
VALUES (3, 2, 1);
INSERT INTO gift_certificate_tags (id, gift_certificate_id, tag_id)
VALUES (4, 2, 3);
INSERT INTO gift_certificate_tags (id, gift_certificate_id, tag_id)
VALUES (5, 3, 3);