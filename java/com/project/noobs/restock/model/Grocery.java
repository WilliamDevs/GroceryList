package com.project.noobs.restock.model;

/**
 * Created by Noobs on 10/29/2016.
 */
public class Grocery {

    int id;
    String item,quantity;

    public Grocery(){

    }

    public Grocery(int id,String item,String quantity){
        this.id=id;
        this.item = item;
        this.quantity=quantity;
    }
    public Grocery(String item,String quantity){
        this.item = item;
        this.quantity=quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
