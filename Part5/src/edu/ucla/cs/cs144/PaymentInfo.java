package edu.ucla.cs.cs144;

public class PaymentInfo {

    String itemId;
    String name;
    String buyPrice;

    public PaymentInfo(String p_itemId, String p_name, String p_buyPrice) {
        super();
        this.itemId = p_itemId;
        this.name = p_name;
        this.buyPrice = p_buyPrice;
    }
    public String getBuyPrice() {return buyPrice;}
    public String getItemId() {return itemId; }
    public String getName() {return name;}
}