package com.smookcreative.icare.Adapters;

import android.graphics.drawable.Drawable;

import com.smookcreative.icare.R;

import java.lang.reflect.Array;
import java.util.Random;

public class PlacesObject {

    private String image, name="---", location="---", quartier="---", website="---", phone="---", id;
    private int places, price, type, color;
    private String specialities="---";

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    private String hours="---";
    private String isopen="0";
    private boolean isLiked=false;

    private String recommandation="---", comments="---", places_total="---";

    public PlacesObject() {
        Random random = new Random();
        int color = random.nextInt(4);
        this.color = color;
    }


    public PlacesObject(String image, String name, String quartier, String location, String website, String phone, String id, int places, int price, String specialities, String isopen, boolean isLiked) {
        this.image = image;
        this.name = name;
        this.location = location;
        this.website = website;
        this.phone = phone;
        this.places = places;
        this.price = price;
        this.specialities = specialities;
        this.id=id;
        this.isopen=isopen;
        this.quartier=quartier;
        this.isLiked=isLiked;
    }

    public PlacesObject(String name, String location, int places, String image) {
        this.image = image;
        this.name = name;
        this.location = location;
        this.places = places;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public PlacesObject(int type, String name, String location, int places, String image) {
        this.type=type;
        this.image = image;
        this.name = name;
        this.location = location;
        this.places = places;
    }

    public PlacesObject(String name,String quartier, String location,  int places, String recommandation, String comments, String image) {
        this.type=type;
        this.image = image;
        this.name = name;
        this.location = location;
        this.places = places;
        this.recommandation=recommandation;
        this.comments=comments;
        this.quartier=quartier;
    }


    public PlacesObject(String name,String quartier, String location,  int places, String recommandation, String comments, String image, boolean isLiked) {
        this.type=type;
        this.image = image;
        this.name = name;
        this.location = location;
        this.places = places;
        this.recommandation=recommandation;
        this.comments=comments;
        this.quartier=quartier;
        this.isLiked=isLiked;
    }



    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        Random random = new Random();
        int x = random.nextInt(4);
        this.color = color;
    }

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }



    public String getRecommandation() {
        return recommandation;
    }

    public void setRecommandation(String recommandation) {
        this.recommandation = recommandation;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPlaces_total() {
        return places_total;
    }

    public void setPlaces_total(String places_total) {
        this.places_total = places_total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhone() {
        return phone;
    }

    public int getPlaces() {
        return places;
    }

    public int getPrice() {
        return price;
    }

    public String getSpecialities() {
        return specialities;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSpecialities(String specialities) {
        this.specialities = specialities;
    }
}
