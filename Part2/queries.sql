SELECT COUNT(*) FROM Users;

SELECT COUNT(*) FROM Items, UserLocation WHERE SellerId = UserId AND BINARY Location='New York';

SELECT COUNT(*) FROM (SELECT ItemId FROM Categories GROUP BY ItemId HAVING COUNT(Category) = 4) FourCat;

SELECT ItemId FROM Items WHERE Currently = (SELECT MAX(Currently) FROM Items WHERE Ends > '2001-12-20 00:00:01' AND NumOfBids > 0) AND NumOfBids > 0;

SELECT COUNT(*) FROM Users WHERE UserId in (SELECT SellerID FROM Items) AND rating > 1000;

SELECT COUNT(*) FROM (SELECT DISTINCT Items.SellerId FROM Items INNER JOIN Bids ON Items.SellerId = Bids.BidderId) SellerBidder;

SELECT COUNT(*) FROM (SELECT DISTINCT Category FROM Categories WHERE ItemId IN (SELECT DISTINCT ItemId FROM Bids WHERE Amount > 100.00)) Morethan100;

