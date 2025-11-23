ALTER TABLE mktplace.bikes
ADD COLUMN rentalPriceMonthly MONEY,
ADD COLUMN stripeProductId VARCHAR(75) UNIQUE,
ADD COLUMN stripePriceId VARCHAR(75) UNIQUE;

ALTER TABLE mktplace.boats
ADD COLUMN rentalPriceMonthly MONEY,
ADD COLUMN stripeProductId VARCHAR(75) UNIQUE,
ADD COLUMN stripePriceId VARCHAR(75) UNIQUE;

ALTER TABLE mktplace.cars
ADD COLUMN rentalPriceMonthly MONEY,
ADD COLUMN stripeProductId VARCHAR(75) UNIQUE,
ADD COLUMN stripePriceId VARCHAR(75) UNIQUE;

ALTER TABLE mktplace.planes
ADD COLUMN rentalPriceMonthly MONEY,
ADD COLUMN stripeProductId VARCHAR(75) UNIQUE,
ADD COLUMN stripePriceId VARCHAR(75) UNIQUE;