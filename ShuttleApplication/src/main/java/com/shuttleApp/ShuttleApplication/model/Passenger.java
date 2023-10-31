package com.shuttleApp.ShuttleApplication.model;

import java.math.BigInteger;

public class Passenger {

    private double longitude;
    private double latitude;

    private BigInteger suid;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public BigInteger getSuid(){return suid;}

    public void setSuid(BigInteger suid) { this.suid = suid;}


}

