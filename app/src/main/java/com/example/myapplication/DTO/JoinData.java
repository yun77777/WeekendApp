package com.example.myapplication.DTO;

import com.google.gson.annotations.SerializedName;

public class JoinData {

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("device")
    private String device;

    public JoinData(String password, String name, String email, String device) {
        this.password=password;
        this.name=name;
        this.email=email;
        this.device=device;
    }
}