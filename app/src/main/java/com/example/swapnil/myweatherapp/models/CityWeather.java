package com.example.swapnil.myweatherapp.models;

import java.util.List;

/**
 * Created by ekhamees on 9/2/15.
 */
public class CityWeather {
    private String message;

    private String cnt; // Nos of days forecast Results

    private String cod;

    private List[] list;

    private City city;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public List[] getList() {
        return list;
    }

    public void setList(List[] list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
