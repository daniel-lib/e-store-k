CREATE TABLE products
(
    id        TEXT PRIMARY KEY,
    title     TEXT,
    handle    TEXT,
    vendor    TEXT,
    image     TEXT,
    created_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE variants
(
    id             BIGINT PRIMARY KEY,
    product_id     TEXT REFERENCES products (id) ON DELETE CASCADE,
    title          TEXT,
    price          NUMERIC,
    featured_image TEXT
);