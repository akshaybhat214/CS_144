
CREATE TABLE ItemLatLong(
	ItemId INT NOT NULL,
	Coord GEOMETRY NOT NULL,
	PRIMARY KEY (ItemId)) 	
	ENGINE=MyISAM;

	INSERT INTO ItemLatLong (ItemId, Coord)
	(SELECT ItemId, POINT(Latitiude,Longitude)
	 FROM Items
	 WHERE Latitiude IS NOT NULL AND Longitude IS NOT NULL); #Intentionally misspelled

	CREATE SPATIAL INDEX sp_index ON ItemLatLong(Coord);
