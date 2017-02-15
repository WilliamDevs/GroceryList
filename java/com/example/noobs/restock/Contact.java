package com.example.noobs.restock;

/**
 * Created by Noobs on 6/20/2016.
 */
public class Contact {
    private String Name,Prof;

    public Contact(String name,String prof){
        this.setName(name);
        this.setProf(prof);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProf() {
        return Prof;
    }

    public void setProf(String prof) {
        Prof = prof;
    }
}
