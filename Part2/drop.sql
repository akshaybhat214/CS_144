
#Only drops tables if they exist. Deletion ordered based on Foreign Key constraints.
DROP TABLE IF EXISTS UserLocation, LatLong, Bids, Categories, Items, Users;