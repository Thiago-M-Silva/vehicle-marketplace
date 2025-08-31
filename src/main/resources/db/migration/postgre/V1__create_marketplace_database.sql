CREATE SCHEMA IF NOT EXISTS mktPlace;

-- Enums (tabelas de apoio)
CREATE TABLE IF NOT EXISTS mktPlace.eVehicleCategory (
    id INT PRIMARY KEY,
    category VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS mktPlace.eColors (
    id INT PRIMARY KEY,
    color VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS mktPlace.eFuelType (
    id INT PRIMARY KEY,
    fuel VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS mktPlace.ePaymentMethods (
    id INT PRIMARY KEY,
    paymentMethods VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS mktPlace.ePaymentStatus (
    id INT PRIMARY KEY,
    paymentStatus VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS mktPlace.eVehicleStatus (
    id INT PRIMARY KEY,
    vehicleStatus VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS mktPlace.eUserRole (
    id INT PRIMARY KEY,
    role VARCHAR(10)
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
    state VARCHAR(20),
    userType INT NOT NULL,
    transactionId UUID,
    createdate TIMESTAMPTZ,
    updatedate TIMESTAMPTZ,
    CONSTRAINT fk_users_role FOREIGN KEY (userType) REFERENCES mktPlace.eUserRole(id)
);

-- Veículos
CREATE TABLE IF NOT EXISTS mktPlace.cars (
    id UUID PRIMARY KEY,
    name VARCHAR(20),
    brand VARCHAR(15),
    year INT,
    price MONEY,
    model VARCHAR(15),
    horsepower INT,
    transmissionType VARCHAR(20),
    description TEXT,
    carStatus INT,
    category INT,
    color INT,
    fuelType INT,
    carType VARCHAR(20),
    createDate TIMESTAMPTZ,
    updateDate TIMESTAMPTZ,
    CONSTRAINT fk_cars_status FOREIGN KEY (carStatus) REFERENCES mktPlace.eVehicleStatus(id),
    CONSTRAINT fk_cars_category FOREIGN KEY (category) REFERENCES mktPlace.eVehicleCategory(id),
    CONSTRAINT fk_cars_color FOREIGN KEY (color) REFERENCES mktPlace.eColors(id),
    CONSTRAINT fk_cars_fuel FOREIGN KEY (fuelType) REFERENCES mktPlace.eFuelType(id)
);

CREATE TABLE IF NOT EXISTS mktPlace.bikes (
    id UUID PRIMARY KEY,
    name VARCHAR(20),
    brand VARCHAR(15),
    year INT,
    price MONEY,
    model VARCHAR(15),
    horsepower INT,
    transmissionType VARCHAR(20),
    description TEXT,
    bikeStatus INT,
    category INT,
    color INT,
    fuelType INT,
    bikeType VARCHAR(25),
    createDate TIMESTAMPTZ,
    updateDate TIMESTAMPTZ,
    CONSTRAINT fk_bikes_status FOREIGN KEY (bikeStatus) REFERENCES mktPlace.eVehicleStatus(id),
    CONSTRAINT fk_bikes_category FOREIGN KEY (category) REFERENCES mktPlace.eVehicleCategory(id),
    CONSTRAINT fk_bikes_color FOREIGN KEY (color) REFERENCES mktPlace.eColors(id),
    CONSTRAINT fk_bikes_fuel FOREIGN KEY (fuelType) REFERENCES mktPlace.eFuelType(id)
);

CREATE TABLE IF NOT EXISTS mktPlace.planes (
    id UUID PRIMARY KEY,
    name VARCHAR(20),
    brand VARCHAR(15),
    year INT,
    price MONEY,
    model VARCHAR(15),
    horsepower INT,
    transmissionType VARCHAR(20),
    description TEXT,
    planeStatus INT,
    category INT,
    color INT,
    fuelType INT,
    planeType VARCHAR(20),
    createDate TIMESTAMPTZ,
    updateDate TIMESTAMPTZ,
    CONSTRAINT fk_planes_status FOREIGN KEY (planeStatus) REFERENCES mktPlace.eVehicleStatus(id),
    CONSTRAINT fk_planes_category FOREIGN KEY (category) REFERENCES mktPlace.eVehicleCategory(id),
    CONSTRAINT fk_planes_color FOREIGN KEY (color) REFERENCES mktPlace.eColors(id),
    CONSTRAINT fk_planes_fuel FOREIGN KEY (fuelType) REFERENCES mktPlace.eFuelType(id)
);

CREATE TABLE IF NOT EXISTS mktPlace.boats (
    id UUID PRIMARY KEY,
    name VARCHAR(20),
    brand VARCHAR(15),
    year INT,
    price MONEY,
    model VARCHAR(15),
    horsepower INT,
    transmissionType VARCHAR(20),
    description TEXT,
    numberOfCabins INT,
    boatStatus INT,
    category INT,
    color INT,
    fuelType INT,
    boatType VARCHAR(20),
    createDate TIMESTAMPTZ,
    updateDate TIMESTAMPTZ,
    CONSTRAINT fk_boats_status FOREIGN KEY (boatStatus) REFERENCES mktPlace.eVehicleStatus(id),
    CONSTRAINT fk_boats_category FOREIGN KEY (category) REFERENCES mktPlace.eVehicleCategory(id),
    CONSTRAINT fk_boats_color FOREIGN KEY (color) REFERENCES mktPlace.eColors(id),
    CONSTRAINT fk_boats_fuel FOREIGN KEY (fuelType) REFERENCES mktPlace.eFuelType(id)
);

-- Agora sim: payment depois de users e vehicles
CREATE TABLE IF NOT EXISTS mktPlace.payment (
    id UUID PRIMARY KEY,
    amount MONEY NOT NULL,
    transactionId INT UNIQUE NOT NULL,
    paymentMethod INT NOT NULL,
    paymentStatus INT NOT NULL,
    client UUID NOT NULL,
    seller UUID NOT NULL,
    bike UUID,
    cars UUID,
    boats UUID,
    planes UUID,
    createDate TIMESTAMPTZ,
    updateDate TIMESTAMPTZ,
    CONSTRAINT fk_payment_method FOREIGN KEY (paymentMethod) REFERENCES mktPlace.ePaymentMethods(id),
    CONSTRAINT fk_payment_status FOREIGN KEY (paymentStatus) REFERENCES mktPlace.ePaymentStatus(id),
    CONSTRAINT fk_payment_user FOREIGN KEY (client) REFERENCES mktPlace.users(id),
    CONSTRAINT fk_payment_seller FOREIGN KEY (seller) REFERENCES mktPlace.users(id),
    CONSTRAINT fk_payment_bike FOREIGN KEY (bike) REFERENCES mktPlace.bikes(id),
    CONSTRAINT fk_payment_car FOREIGN KEY (cars) REFERENCES mktPlace.cars(id),
    CONSTRAINT fk_payment_boat FOREIGN KEY (boats) REFERENCES mktPlace.boats(id),
    CONSTRAINT fk_payment_plane FOREIGN KEY (planes) REFERENCES mktPlace.planes(id)
);

-- E aqui alteramos users para incluir o relacionamento com payment (agora que já existe)
ALTER TABLE mktPlace.users
ADD CONSTRAINT fk_users_transaction FOREIGN KEY (transactionId) REFERENCES mktPlace.payment(id);

-- Documentos
CREATE TABLE IF NOT EXISTS mktPlace.vehicle_documents (
    id SERIAL PRIMARY KEY,
    filename VARCHAR(255),
    content_type VARCHAR(50),
    uploaded_at TIMESTAMPTZ,
    mongo_file_id VARCHAR(50),
    vehicle_id UUID NOT NULL,
    vehicle_type VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS mktPlace.paymentDocs (
    id SERIAL PRIMARY KEY,
    filename VARCHAR(25),
    content_type VARCHAR(25),
    uploaded_at TIMESTAMPTZ,
    payment_id UUID NOT NULL,
    mongo_file_id VARCHAR(35),
    CONSTRAINT fk_payment_doc FOREIGN KEY (payment_id) REFERENCES mktPlace.payment(id)
);
