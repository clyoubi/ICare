package com.smookcreative.icare.Adapters;




public class ArticleObject2 {

    long id;
    String post;
    int status;
    String createdAt;
    String Title;
    String Description;
    String postURL;
    String postImg;
    String author;



    // Constructors
    public ArticleObject2(long id, String post, int status, String createdAt, String title, String description, String postURL, String postImg, String author) {
        this.id = id;
        this.post = post;
        this.status = status;
        this.createdAt = createdAt;
        this.Title = title;
        this.Description = description;
        this.postURL = postURL;
        this.postImg = postImg;
        this.author = author;
    }

    public ArticleObject2() {

    }


    // end Constructors



    // setters and getters

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getPostImg() {
        return postImg;
    }
    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }
    public String getPost() {
        return post;
    }
    public void setPost(String post) {
        this.post = post;
    }
    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
