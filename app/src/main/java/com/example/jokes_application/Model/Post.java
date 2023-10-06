package com.example.jokes_application.Model;

public class Post {

    private String name,imageUrl, joke,postedDateTime,uid,postId,reacted;

//    public Post(){
//
//    }


    public Post(){

    }
    public Post(String name, String imageUrl, String joke, String postedDateTime) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.joke = joke;
        this.postedDateTime = postedDateTime;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getReacted() {
        return reacted;
    }

    public void setReacted(String reacted) {
        this.reacted = reacted;
    }

    public String getPostedDateTime() {
        return postedDateTime;
    }

    public void setPostedDateTime(String postedDateTime) {
        this.postedDateTime = postedDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public void setUid(String uid){
        this.uid=uid;
    }
    public String getUid(){
        return uid;
    }


}
