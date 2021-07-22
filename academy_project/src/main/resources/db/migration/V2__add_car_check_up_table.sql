CREATE TABLE IF NOT EXISTS car_check_up (
    id BIGINT PRIMARY KEY,
    time_of_check_up TIMESTAMP NOT NULL,
    worker_name TEXT NOT NULL CHECK (worker_name <> ''),
    price DOUBLE PRECISION NOT NULL CHECK ( price > 0 ),
    car_id BIGINT NOT NULL,
    FOREIGN KEY (car_id) REFERENCES car(id) ON UPDATE CASCADE
);