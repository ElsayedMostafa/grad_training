package com.example.madara.training.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 3/28/18.
 */

public class Rfid {
    @SerializedName("user_id")
    public int user_id;
    @SerializedName("qrcode")
    public String mId;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;



    public Rfid(String id){
        this.mId = id;
    }
    public String getId(){
        return this.mId;
    }
}
