DROP TABLE IF EXISTS gift_certificate_tags;
DROP TABLE IF EXISTS gift_certificates;
DROP TABLE IF EXISTS tags;


CREATE TABLE gift_certificates
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(255)   NOT NULL,
    description      TEXT,
    price            DECIMAL(10, 2) NOT NULL,
    duration         INT            NOT NULL,
    create_date      TIMESTAMP      NOT NULL,
    last_update_date TIMESTAMP      NOT NULL
);

CREATE TABLE tags
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE gift_certificate_tags
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    gift_certificate_id BIGINT,
    tag_id              BIGINT,
    CONSTRAINT gift_cert_tag_unique UNIQUE (gift_certificate_id, tag_id),
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificates (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags (id) ON UPDATE CASCADE ON DELETE CASCADE
);