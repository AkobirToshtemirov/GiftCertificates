CREATE TABLE gift_certificates
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(255)   NOT NULL,
    description       TEXT,
    price             NUMERIC(10, 2) NOT NULL,
    duration          NUMERIC(10, 2) NOT NULL,
    created_date      TIMESTAMP      NOT NULL,
    last_updated_date TIMESTAMP      NOT NULL
);

CREATE TABLE tags
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE gift_certificate_tags
(
    id                  SERIAL PRIMARY KEY,
    gift_certificate_id BIGINT REFERENCES gift_certificates (id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id              BIGINT REFERENCES tags (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT gift_cert_tag_unique UNIQUE (gift_certificate_id, tag_id)
);
