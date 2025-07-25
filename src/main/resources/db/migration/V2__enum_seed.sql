INSERT INTO mktplace.ecolors (id, color) VALUES 
(1, 'RED'),
(2, 'BLUE'),
(3, 'GREEN'),
(4, 'BLACK'),
(5, 'WHITE'),
(6, 'SILVER'),
(7, 'YELLOW');

INSERT INTO mktplace.evehiclecategory (id, category) VALUES 
(1, 'SEDAN'),
(2, 'SUV'),
(3, 'TRUCK'),
(4, 'MOTORCROSS'),
(5, 'VAN'),
(6, 'COUPE'),
(7, 'SPORTS_BIKE'),
(8, 'CONVERTIBLE'),
(9, 'YATCH'),
(10, 'SPORTS_CAR'),
(11, 'ELECTRIC_CAR'),
(12, 'SPEEDBOAT'),
(13, 'LUXURY_CAR'),
(14, 'PICKUP_TRUCK'),
(15, 'QUAD_BIKE'),
(16, 'MINIVAN');

INSERT INTO mktplace.efueltype (id, fuel) VALUES
(1, 'DIESEL'),
(2, 'ELECTRIC'),
(3, 'HYBRID'),
(4, 'LPG'),
(5, 'CNG'),
(6, 'ETHANOL'),
(7, 'BIODIESEL'),
(8, 'PROPANE'),
(9, 'BUTANE'),
(10, 'FUEL_OIL'),
(11, 'JET_FUEL'),
(12, 'KEROSENE'),
(13, 'ALCOHOL'),
(14, 'GASOLINE');

INSERT INTO mktplace.epaymentmethods (id, paymentmethods) VALUES
(1, 'CASH'),
(2, 'CREDIT_CARD'),
(3, 'DEBIT_CARD'),
(4, 'PAYPAL'),
(5, 'BANK_TRANSFER'),
(6, 'CRYPTOCURRENCY'),
(7, 'APPLE_PAY'),
(8, 'GOOGLE_PAY'),
(9, 'AMAZON_PAY'),
(10, 'SAMSUNG_PAY');

INSERT INTO mktplace.epaymentstatus (id, paymentstatus) VALUES
(1, 'PENDING'),
(2, 'COMPLETED'),
(3, 'FAILED');

INSERT INTO mktplace.evehiclestatus (id, vehiclestatus) VALUES
(1, 'NEW'),
(2, 'USED'),
(3, 'REFORMED'),
(4, 'DAMAGED');

INSERT INTO mktplace.euserrole (id, role) VALUES
(1, 'ADMIN'),
(2, 'CLIENT'),
(3, 'TECHNICIAN');