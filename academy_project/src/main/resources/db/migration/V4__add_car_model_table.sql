CREATE TABLE IF NOT EXISTS car_model(
    id BIGSERIAL PRIMARY KEY,
    manufacturer TEXT NOT NULL CHECK (manufacturer <> ''),
    model_name TEXT NOT NULL CHECK (model_name <> ''),
    is_common BOOLEAN NOT NULL,
    UNIQUE(manufacturer, model_name)
);