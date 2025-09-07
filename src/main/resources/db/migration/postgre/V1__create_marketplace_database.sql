CREATE SCHEMA IF NOT EXISTS mktPlace;

-- Enums (tabelas de apoio)
CREATE TABLE IF NOT EXISTS mktPlace.eVehicleCategory (
    category VARCHAR(50) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS mktPlace.eColors (
    color VARCHAR(60) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS mktPlace.eFuelType (
    fuel VARCHAR(50) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS mktPlace.ePaymentMethods (
    paymentMethods VARCHAR(50) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS mktPlace.ePaymentStatus (
    paymentStatus VARCHAR(60) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS mktPlace.eVehicleStatus (
    vehicleStatus VARCHAR(60) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS mktPlace.eUserRole (
    userRole VARCHAR(60) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS mktPlace.users (
    id UUID PRIMARY KEY,
    address VARCHAR(500),
    birthdate DATE,
    city VARCHAR(255),
    country VARCHAR(255),
    cpf VARCHAR(11),
    email VARCHAR(30),
    name VARCHAR(30),
    password VARCHAR(70),
    phonenumber VARCHAR(15),
    rg VARCHAR(15),
    state VARCHAR(50),
    userRole VARCHAR(60),
    transactionId UUID,
    createdate TIMESTAMPTZ,
    updatedate TIMESTAMPTZ,
    CONSTRAINT fk_users_role FOREIGN KEY (userRole) REFERENCES mktPlace.eUserRole(userRole)
);

-- Veículos
CREATE TABLE IF NOT EXISTS mktPlace.cars (
    id UUID PRIMARY KEY,
    name VARCHAR(50),
    brand VARCHAR(15),
    year INT,
    price MONEY,
    model VARCHAR(15),
    horsepower INT,
    transmissionType VARCHAR(50),
    description TEXT,
    vehicleStatus VARCHAR(50),
    category VARCHAR(50),
    color VARCHAR(50),
    fuelType VARCHAR(50),
    owner UUID,
    createDate TIMESTAMPTZ,
    updateDate TIMESTAMPTZ,
    CONSTRAINT fk_cars_status FOREIGN KEY (vehicleStatus) REFERENCES mktPlace.eVehicleStatus(vehicleStatus),
    CONSTRAINT fk_cars_category FOREIGN KEY (category) REFERENCES mktPlace.eVehicleCategory(category),
    CONSTRAINT fk_cars_color FOREIGN KEY (color) REFERENCES mktPlace.eColors(color),
    CONSTRAINT fk_cars_fuel FOREIGN KEY (fuelType) REFERENCES mktPlace.eFuelType(fuel),
    CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES mktPlace.users(id)
);

CREATE TABLE IF NOT EXISTS mktPlace.bikes (
    id UUID PRIMARY KEY,
    name VARCHAR(50),
    brand VARCHAR(15),
    year INT,
    price MONEY,
    model VARCHAR(15),
    horsepower INT,
    transmissionType VARCHAR(50),
    description TEXT,
    vehicleStatus VARCHAR(50),
    category VARCHAR(50),
    color VARCHAR(50),
    fuelType VARCHAR(50),
    owner UUID,
    createDate TIMESTAMPTZ,
    updateDate TIMESTAMPTZ,
    CONSTRAINT fk_cars_status FOREIGN KEY (vehicleStatus) REFERENCES mktPlace.eVehicleStatus(vehicleStatus),
    CONSTRAINT fk_cars_category FOREIGN KEY (category) REFERENCES mktPlace.eVehicleCategory(category),
    CONSTRAINT fk_cars_color FOREIGN KEY (color) REFERENCES mktPlace.eColors(color),
    CONSTRAINT fk_cars_fuel FOREIGN KEY (fuelType) REFERENCES mktPlace.eFuelType(fuel),
    CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES mktPlace.users(id)
);

CREATE TABLE IF NOT EXISTS mktPlace.planes (
    id UUID PRIMARY KEY,
    name VARCHAR(50),
    brand VARCHAR(15),
    year INT,
    price MONEY,
    model VARCHAR(15),
    horsepower INT,
    transmissionType VARCHAR(50),
    description TEXT,
    vehicleStatus VARCHAR(50),
    category VARCHAR(50),
    color VARCHAR(50),
    fuelType VARCHAR(50),
    owner UUID,
    createDate TIMESTAMPTZ,
    updateDate TIMESTAMPTZ,
    CONSTRAINT fk_cars_status FOREIGN KEY (vehicleStatus) REFERENCES mktPlace.eVehicleStatus(vehicleStatus),
    CONSTRAINT fk_cars_category FOREIGN KEY (category) REFERENCES mktPlace.eVehicleCategory(category),
    CONSTRAINT fk_cars_color FOREIGN KEY (color) REFERENCES mktPlace.eColors(color),
    CONSTRAINT fk_cars_fuel FOREIGN KEY (fuelType) REFERENCES mktPlace.eFuelType(fuel),
    CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES mktPlace.users(id)
);

CREATE TABLE IF NOT EXISTS mktPlace.boats (
    id UUID PRIMARY KEY,
    name VARCHAR(50),
    brand VARCHAR(15),
    year INT,
    price MONEY,
    model VARCHAR(15),
    horsepower INT,
    transmissionType VARCHAR(50),
    description TEXT,
    numberOfCabins INT,
    vehicleStatus VARCHAR(50),
    category VARCHAR(50),
    color VARCHAR(50),
    fuelType VARCHAR(50),
    owner UUID,
    createDate TIMESTAMPTZ,
    updateDate TIMESTAMPTZ,
    CONSTRAINT fk_cars_status FOREIGN KEY (vehicleStatus) REFERENCES mktPlace.eVehicleStatus(vehicleStatus),
    CONSTRAINT fk_cars_category FOREIGN KEY (category) REFERENCES mktPlace.eVehicleCategory(category),
    CONSTRAINT fk_cars_color FOREIGN KEY (color) REFERENCES mktPlace.eColors(color),
    CONSTRAINT fk_cars_fuel FOREIGN KEY (fuelType) REFERENCES mktPlace.eFuelType(fuel),
    CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES mktPlace.users(id)
);

CREATE TABLE IF NOT EXISTS mktPlace.payment (
    id UUID PRIMARY KEY,
    amount MONEY NOT NULL,
    transactionId INT UNIQUE NOT NULL,
    paymentMethod VARCHAR(60) NULL,
    paymentStatus VARCHAR(60) NULL,
    client UUID NOT NULL,
    seller UUID NOT NULL,
    bike UUID,
    cars UUID,
    boats UUID,
    planes UUID,
    createDate TIMESTAMPTZ,
    updateDate TIMESTAMPTZ,
    CONSTRAINT fk_payment_method FOREIGN KEY (paymentMethod) REFERENCES mktPlace.ePaymentMethods(paymentMethods),
    CONSTRAINT fk_payment_status FOREIGN KEY (paymentStatus) REFERENCES mktPlace.ePaymentStatus(paymentStatus),
    CONSTRAINT fk_payment_user FOREIGN KEY (client) REFERENCES mktPlace.users(id),
    CONSTRAINT fk_payment_seller FOREIGN KEY (seller) REFERENCES mktPlace.users(id),
    CONSTRAINT fk_payment_bike FOREIGN KEY (bike) REFERENCES mktPlace.bikes(id),
    CONSTRAINT fk_payment_car FOREIGN KEY (cars) REFERENCES mktPlace.cars(id),
    CONSTRAINT fk_payment_boat FOREIGN KEY (boats) REFERENCES mktPlace.boats(id),
    CONSTRAINT fk_payment_plane FOREIGN KEY (planes) REFERENCES mktPlace.planes(id)
);

ALTER TABLE mktplace.users
ADD COLUMN bikeId UUID,
ADD COLUMN carId UUID,
ADD COLUMN boatId UUID,
ADD COLUMN planeId UUID;

ALTER TABLE mktplace.users
ADD CONSTRAINT fk_users_bike FOREIGN KEY (bikeId)  REFERENCES mktplace.bikes(id),
ADD CONSTRAINT fk_users_car  FOREIGN KEY (carId)   REFERENCES mktplace.cars(id),
ADD CONSTRAINT fk_users_boat FOREIGN KEY (boatId)  REFERENCES mktplace.boats(id),
ADD CONSTRAINT fk_users_plane FOREIGN KEY (planeId) REFERENCES mktplace.planes(id);