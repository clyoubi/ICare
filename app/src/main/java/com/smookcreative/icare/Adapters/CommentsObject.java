package com.smookcreative.icare.Adapters;

public class CommentsObject {

    private String profile_image, comment, post_date, name, repond_a, id, author_id, author_email;
    private boolean reply = false;

    public CommentsObject(String id, String profile_image, String comment, String post_date, String name, String repond_a) {
        this.profile_image = profile_image;
        this.comment = comment;
        this.post_date = post_date;
        this.name = name;
        this.repond_a=repond_a;
    }


    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }

    public String getAuthor_id() {
        return author_id;
    }


    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepond_a() {
        return repond_a;
    }

    public void setRepond_a(String repond_a) {
        this.repond_a = repond_a;
    }

    public CommentsObject() {
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
