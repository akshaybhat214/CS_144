package edu.ucla.cs.cs144;


public class Bid {
    private User bidder;
    private String time;
    private String amount;

    public Bid(User bidderPar, String timePar, String amtPar){
        bidder=bidderPar;
        time=timePar;
        amount=amtPar;
    }
    public User getBidder(){return bidder;}
    public String getTime(){return time;}
    public String getAmount(){return amount;}
}

 