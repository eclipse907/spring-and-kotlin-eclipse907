CREATE TABLE IF NOT EXISTS car(
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    date_added DATE NOT NULL,
    manufacturer_name TEXT NOT NULL CHECK (manufacturer_name <> ''),
    model_name TEXT NOT NULL CHECK (model_name <> ''),
    production_year SMALLINT NOT NULL,
    serial_number BIGINT NOT NULL
);