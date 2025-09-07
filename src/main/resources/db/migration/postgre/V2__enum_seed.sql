INSERT INTO mktplace.ecolors (color) VALUES
('RED'),
('GREEN'),
('BLUE'),
('YELLOW'),
('BLACK'),
('WHITE'),
('ORANGE'),
('PURPLE'),
('PINK'),
('GRAY'),
('SILVER'),
('GOLD');

INSERT INTO  mktplace.evehiclecategory (category) values
('HATCH'),
('SEDAN'),
('SUV'),
('CROSSOVER'),
('MINIVAN'),
('STATION_WAGON'),
('CUPÊ'),
('SPORTIVE'),
('PICKUP'),
('VAN'),
('TRUCK'),
('CRUISERS'),
('SPORTBIKES'),
('STANDARD'),
('ADVENTURE'),
('DIRTBIKES'),
('ELETRIC'),
('CHOPPERS'),
('TOURING'),
('VINTAGE'),
('SCOOTER'),
('ELETRIC_BIKE'),
('QUADRICYCLE'),
('MONOCYCLE'),
('ALUMINIUM'),
('BOW_RIDER'),
('CONSOLE'),
('DAYCRUISER'),
('PILOTHOUSE'),
('SPEEDBOAT'),
('JET_SKY'),
('JET'),
('AMPHIBIOUS'),
('TURBOPROP'),
('SINGLE_ENGINE'),
('TWIN_ENGINE'),
('HELICOPTER');

INSERT INTO mktplace.efueltype (fuel) VALUES
('DIESEL'),
('ELECTRIC'),
('HYBRID'),
('LPG'),
('CNG'),
('ETHANOL'),
('BIODIESEL'),
('PROPANE'),
('BUTANE'),
('FUEL_OIL'),
('JET_FUEL'),
('KEROSENE'),
('ALCOHOL'),
('GASOLINE');

INSERT INTO mktplace.epaymentmethods (paymentmethods) VALUES
('CASH'),
('CREDIT_CARD'),
('DEBIT_CARD'),
('PAYPAL'),
('BANK_TRANSFER'),
('CRYPTOCURRENCY'),
('APPLE_PAY'),
('GOOGLE_PAY'),
('AMAZON_PAY'),
('SAMSUNG_PAY');

INSERT INTO mktplace.epaymentstatus (paymentstatus) VALUES
('PENDING'),
('COMPLETED'),
('FAILED');

INSERT INTO mktplace.evehiclestatus (vehiclestatus) VALUES
('NEW'),
('USED'),
('REFORMED'),
('DAMAGED');

INSERT INTO mktplace.euserrole (userRole) VALUES
('ADMIN'),
('CLIENT'),
('TECHNICIAN');