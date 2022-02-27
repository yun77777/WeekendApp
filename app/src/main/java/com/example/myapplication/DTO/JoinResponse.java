package com.example.myapplication.DTO;

import com.google.gson.annotations.SerializedName;

public class JoinResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public class Data{
        @SerializedName("email")
        private int email;
//        @SerializedName("userId")
//        private int userId;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}