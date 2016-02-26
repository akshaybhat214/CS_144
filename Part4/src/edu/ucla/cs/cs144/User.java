package edu.ucla.cs.cs144;

public class User {
    private String userId;
    private String rating;
    private String location;
    private String country;

    public User(String idPar,  String ratPar, String locPar, String counPar){
        userId=idPar;
        rating=ratPar;
        location=locPar;
        country=counPar;
    }
    public String getUserId(){return userId;}
    public String getRating(){return rating;}
    public String getLocation(){return location;}
    public String getCountry(){return country;}

}

