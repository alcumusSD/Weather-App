package com.example.openweatherproject;


public class WeatherData {
    private String date;
    private String minTemp;
    private String maxTemp;

    private int  image;
    private String desc;


    public WeatherData(String date, String minTemp, String maxTemp, int image, String desc) {
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.image = image;
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public int getImage()
    {
        return image;
    }

    public String getDesc()
    {
        return desc;
    }
}
