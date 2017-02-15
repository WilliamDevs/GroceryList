package com.project.noobs.restock.model;

/**
 * Created by Noobs on 10/19/2016.
 */
public class Weather {

    public Place place;
    public CurrentCondition currentCondition= new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();
    public Snow snow = new Snow();
    public Clouds clouds = new Clouds();


}
