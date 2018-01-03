package com.example.boti.sapiapp;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Boti on 2017.11.10..
 */

public class Advertiser {

    public String description;
    public String title;
    public ArrayList<String> photos;

    public Advertiser()
    {
        this.description="";
        this.title="";
        this.photos=new ArrayList<String>();
    }

    public Advertiser(String description, String title,ArrayList<String> photos)
    {
        this.description=description;
        this.title=title;
        this.photos = new ArrayList<String>();
        this.photos=photos;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public String getDescription() {
        return description;
    }
}
