package com.smookcreative.icare.Adapters;

public class DepechesObject {

    private String title, postOn, source, author;
    private short type;

    public DepechesObject(String title, String postOn, String source, String author, short type) {
        this.title = title;
        this.postOn = postOn;
        this.source = source;
        this.author= author;
        this.type = type;
    }

    public DepechesObject() {
    }

    public DepechesObject(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostOn() {
        return postOn;
    }

    public void setPostOn(String postOn) {
        this.postOn = postOn;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
}
