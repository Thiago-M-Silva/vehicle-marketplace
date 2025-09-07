select * from mktplace.bikes
SELECT * from mktplace.evehiclecategory

DELETE FROM mktplace.evehiclecategory
DELETE FROM """$user"", public".flyway_schema_history

INSERT INTO  mktplace.evehiclecategory (id, category) values
(1, 'HATCH'),
(2, 'SEDAN'),
(3, 'SUV'),
(4, 'CROSSOVER'),
(5, 'MINIVAN'),
(6, 'STATION_WAGON'),
(7, 'CUPÊ'),
(8, 'SPORTIVE'),
(9, 'PICKUP'),
(10, 'VAN'),
(11, 'TRUCK'),
(12, 'CRUISERS'),
(13, 'SPORTBIKES'),
(14, 'STANDARD'),
(15, 'ADVENTURE'),
(16, 'DIRTBIKES'),
(17, 'ELETRIC'),
(18, 'CHOPPERS'),
(19, 'TOURING'),
(20, 'VINTAGE'),
(21, 'SCOOTER'),
(22, 'ELETRIC_BIKE'),
(23, 'QUADRICYCLE'),
(24, 'MONOCYCLE'),
(25, 'ALUMINIUM'),
(26, 'BOW_RIDER'),
(27, 'CONSOLE'),
(28, 'DAYCRUISER'),
(29, 'PILOTHOUSE'),
(30, 'SPEEDBOAT'),
(31, 'JET_SKY'),
(32, 'JET'),
(33, 'AMPHIBIOUS'),
(34, 'TURBOPROP'),
(35, 'SENGLE_ENGINE'),
(36, 'TWIN_ENGINE'),
(37, 'HELICOPTER');


/*
1	SEDAN
2	SUV
3	TRUCK
4	MOTORCROSS
5	VAN
6	COUPE
7	SPORTS_BIKE
8	CONVERTIBLE
9	YATCH
10	SPORTS_CAR
11	ELECTRIC_CAR
12	SPEEDBOAT
13	LUXURY_CAR
14	PICKUP_TRUCK
15	QUAD_BIKE
16	MINIVAN
*/