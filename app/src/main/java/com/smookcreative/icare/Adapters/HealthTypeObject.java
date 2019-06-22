package com.smookcreative.icare.Adapters;

public class HealthTypeObject {

    private String name, type, imageUrl, slug;
    int id, count;

    /*public HealthTypeObject(String name, String count, String type, String imageUrl) {
        this.name = name;
        this.count = count;
        this.type = type;
        this.imageUrl=imageUrl;
    }*/

    public HealthTypeObject() {
    }


    public HealthTypeObject(String name, String imageUrl, String type) {
        this.name = name;
        this.count = count;
        this.type = type;
        this.imageUrl=imageUrl;
    }

    public HealthTypeObject(String name, String imageUrl, String type, String slug) {
        this.name = name;
        this.count = count;
        this.type = type;
        this.imageUrl=imageUrl;
        this.slug=slug;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
