
CREATE TABLE ItemLatLong(
	ItemId INT NOT NULL,
	Coord GEOMETRY NOT NULL,
	PRIMARY KEY (ItemId)) 	
	ENGINE=MyISAM;

	INSERT INTO ItemLatLong (ItemId, Coord)
	(SELECT ItemId, POINT(Latitude,Longitude)
	 FROM Items
	 WHERE Latitude IS NOT NULL AND Longitude IS NOT NULL);

	CREATE SPATIAL INDEX sp_index ON ItemLatLong(Coord);
