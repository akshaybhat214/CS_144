
CREATE TABLE Users(
	UserId Varchar(100) PRIMARY KEY,
	Rating INT
);

CREATE TABLE UserLocation(
	UserId Varchar(100) PRIMARY KEY,
	Location Varchar(100),
	Country Varchar(100),
	FOREIGN KEY (UserId) REFERENCES Users (UserId)		
);

CREATE TABLE Items(
	ItemId INT PRIMARY KEY,
	Name Varchar(100),
	SellerId Varchar(100),
	Currently DECIMAL(8,2),
	BuyPrice DECIMAL(8,2) DEFAULT NULL,
	FirstBid DECIMAL(8,2),
	NumOfBids INT,
	Started TIMESTAMP,
	Ends TIMESTAMP,
	Location Varchar(100),
	Country Varchar(100),
	Latitude Varchar(30) DEFAULT NULL,
	Longitude Varchar(30) DEFAULT NULL,
	Description Varchar(4000),
	FOREIGN KEY (SellerId) REFERENCES Users (UserId)	
);

CREATE TABLE Bids(
	ItemId INT,
	BidderId Varchar(100),
	Time TIMESTAMP,
	Amount DECIMAL(8,2),
	CONSTRAINT BidKey PRIMARY KEY (ItemId, BidderId, Time),
	FOREIGN KEY (ItemId) REFERENCES Items (ItemId)
);

CREATE TABLE Categories(
	ItemId INT,
	Category Varchar(100),
	CONSTRAINT CatKey PRIMARY KEY (ItemId, Category),
	FOREIGN KEY (ItemId) REFERENCES Items (ItemId)
);
