ALTER TABLE mktplace.boats 
ALTER COLUMN price TYPE numeric(19,2);

ALTER TABLE mktplace.cars
ALTER COLUMN price TYPE numeric(19,2);

ALTER TABLE mktplace.bikes
ALTER COLUMN price TYPE numeric(19,2);

ALTER TABLE mktplace.planes
ALTER COLUMN price TYPE numeric(19,2);

ALTER TABLE mktPlace.users
ALTER COLUMN createDate TYPE TIMESTAMPTZ,
ALTER COLUMN updateDate TYPE TIMESTAMPTZ;