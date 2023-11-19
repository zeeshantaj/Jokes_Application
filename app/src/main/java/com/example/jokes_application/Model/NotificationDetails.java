package com.example.jokes_application.Model;

public class NotificationDetails {
    private String imageUrl,name,message;

    public NotificationDetails(String imageUrl, String name, String message) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
