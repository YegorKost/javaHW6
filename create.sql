CREATE TABLE car
(
  car_id SERIAL NOT NULL,
  date date,
  make CHARACTER VARYING,
  model CHARACTER VARYING,
  price NUMERIC,
  CONSTRAINT car_pkey PRIMARY KEY (car_id)
);

CREATE TABLE service_station
(
  service_station_id SERIAL NOT NULL,
  address CHARACTER VARYING,
  CONSTRAINT service_station_pkey PRIMARY KEY (service_station_id)
);

CREATE TABLE mechanic
(
  mechanic_id SERIAL NOT NULL,
  name CHARACTER VARYING,
  surname CHARACTER VARYING,
  service_station_id integer NOT NULL,
  CONSTRAINT mechanic_pkey PRIMARY KEY (mechanic_id),
  CONSTRAINT fk_service_station_id FOREIGN KEY (service_station_id)
  REFERENCES service_station (service_station_id)
);

CREATE TABLE car_service_station
(
  car_service_station_id SERIAL NOT NULL,
  car_id integer NOT NULL,
  service_station_id integer NOT NULL,
  CONSTRAINT car_service_station_pkey PRIMARY KEY (car_service_station_id),
  CONSTRAINT fk_car_id FOREIGN KEY (car_id)
  REFERENCES car (car_id),
  CONSTRAINT fk_service_station_id FOREIGN KEY (service_station_id)
  REFERENCES service_station (service_station_id)
);