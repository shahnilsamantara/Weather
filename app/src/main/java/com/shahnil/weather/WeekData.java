package com.shahnil.weather;


import java.io.Serializable;

public class WeekData implements Serializable {



  public  String  mdate;
   public  String mtemp;
   public  String mhumidity;
    public String mwind;
   public  String mStringWeather;



    public WeekData(String date, String temp, String humidity, String wind, String StringWeather) {

        this.mdate=date;
        this.mtemp=temp;
        this.mhumidity=humidity;
        this.mwind=wind;
        this.mStringWeather=StringWeather;
    }


    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getMtemp() {
        return mtemp;
    }

    public void setMtemp(String mtemp) {
        this.mtemp = mtemp;
    }

    public String getMhumidity() {
        return mhumidity;
    }

    public void setMhumidity(String mhumidity) {
        this.mhumidity = mhumidity;
    }

    public String getMwind() {
        return mwind;
    }

    public void setMwind(String mwind) {
        this.mwind = mwind;
    }

    public String getmStringWeather() {
        return mStringWeather;
    }

    public void setmStringWeather(String mStringWeather) {
        this.mStringWeather = mStringWeather;
    }
}
