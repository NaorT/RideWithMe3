package com.example.ridewithme;

import android.net.Uri;

/**
 * Created by Naor on 30/04/2017.
 */

public class User {
    private String name;
    private String phone;
    private String email;
    private String password;
    private String id;
    private String userImage;
    private double lat;
    private double lng;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public User(String name, String phone, String email, String password, String id, double lat, double lng, String img) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.userImage = img;


    }

    public User() {

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
