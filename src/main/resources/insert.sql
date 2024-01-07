INSERT INTO tags (name)
SELECT 'Tag ' || generate_series(1, 1000) ::text;

INSERT INTO users (username, email, password)
SELECT 'user' || generate_series(1, 1000)::text, 'user_email' || generate_series(1, 1000)::text || '@example.com', 'password' || generate_series(1, 1000) ::text;

INSERT INTO gift_certificates (name, description, price, duration, created_date, last_updated_date)
SELECT 'Gift Certificate' || generate_series(1, 10000)::text, 'Description ' || generate_series(1, 10000)::text, random() * 100.0,
       random() * 30 + 1,
       current_date - (random() * 365)::integer, current_date - (random() * 365) ::integer;

INSERT INTO gift_certificate_tag (gift_id, tag_id)
SELECT (random() * 1000 + 1)::bigint, generate_series(1, 1000) ::bigint;

insert into orders (ordered_time, user_id, gift_certificate_id, price)
select current_date - (random() * 365)::integer, generate_series(1, 1000)::bigint, 10,
       (select gc.price from gift_certificates gc where gc.id = 10);
