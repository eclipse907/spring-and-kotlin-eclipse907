CREATE INDEX idx_car_check_up_car_id ON car_check_up(car_id);
CREATE INDEX idx_car_check_up_time_of_check_up ON car_check_up(time_of_check_up DESC);