package com.example.jokes_application.Model;

public class UserProfileModel {


    private String name,joke,postedDateTime,imageUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public String getPostedDateTime() {
        return postedDateTime;
    }

    public void setPostedDateTime(String postedDateTime) {
        this.postedDateTime = postedDateTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserProfileModel() {
    }

}
