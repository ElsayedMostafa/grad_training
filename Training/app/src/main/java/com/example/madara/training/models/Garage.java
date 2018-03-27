package com.example.madara.training.models;

/**
 * Created by madara on 3/12/18.
 */

public class Garage {
    private int mId;
    private String mName;
    private String mDistance;
    private String mLocation;

    public Garage(int id, String name, String distance, String location){
        mId = id;
        mName = name;
        mDistance = distance;
        mLocation = location;

    }
    public String getGarageName(){
        return mName;
    }
    public String getGarageDistance(){
        return mDistance;
    }

}
