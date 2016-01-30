#Akshay Bhat and Aditya Padmakumar 
#01/29/2016


Users(UserId PRIMARY KEY, Rating)
	--> UserId is the primary key.
FDs: UserId -> {Rating}


UserLocation(UserId PRIMARY KEY,Location,Country)
	--> UserId is the primary key.
	--> UserId is a foreign key on Users.UserId	
FDs: UserId -> {Location, Country}


Items(ItemId, Name , SellerId , Currently, BuyPrice, FirstBid, NumOfBids, Started, Ends, Location, Country, Latitiude, Longitude, Description)
	--> ItemId is the primary key.
	--> SellerId is a foreign key on Users.UserId	
FDs: ItemId -> {Name , SellerId , Currently, BuyPrice, FirstBid, NumOfBids, Started, Ends, Location, Country, Latitiude, Longitude, Description}


Bids( ItemId, BidderId, Time, Amount)
	--> (ItemId, BidderId, Time) together, are the primary key.
	--> ItemId is a foreign key on Items.ItemId
FDs: ItemId, BidderId, Time -> {Amount}


CREATE TABLE Categories(ItemId, Category)
	--> (ItemId, Category) together, are the primary key.
	--> ItemId is a foreign key on Items.ItemId
FDs: None

-----------------------------------------------------------------------------------------
All of the relations are in Boyce-Codd Normal Form (BCNF).


