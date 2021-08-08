ALTER TABLE car
    ADD COLUMN car_model_id BIGINT;

UPDATE car
SET car_model_id = (
    SELECT id
    FROM car_model
    WHERE car_model.manufacturer = car.manufacturer_name
      AND car_model.model_name = car.model_name
);

DELETE
FROM car
WHERE car.car_model_id IS NULL;

ALTER TABLE car
    ADD CONSTRAINT fk_car_model FOREIGN KEY (car_model_id) REFERENCES car_model (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE car
    ALTER COLUMN car_model_id SET NOT NULL;

ALTER TABLE car
    DROP COLUMN IF EXISTS manufacturer_name,
    DROP COLUMN IF EXISTS model_name;