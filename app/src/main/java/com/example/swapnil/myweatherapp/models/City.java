package com.example.swapnil.myweatherapp.models;


public class City {

    public CityCoOrdinates coord;

    public String id;

    public String name;

    public String population;

    public String country;


    public void setCorordinates(String lat, String lon) {
        if (this.coord == null) {
            this.coord = new CityCoOrdinates();
        }
        this.coord.lat = lat;
        this.coord.lon = lon;
    }

    /**
     * Child member class
     */
    public class CityCoOrdinates {
        public String lon;
        public String lat;
    }
}
