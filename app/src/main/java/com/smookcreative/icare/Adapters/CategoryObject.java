package com.smookcreative.icare.Adapters;

public class CategoryObject {

    private String ID, catName, articles_count;
    private boolean liked;


    public CategoryObject(String ID, String catName, String articles_count) {
        this.ID = ID;
        this.catName = catName;
        this.articles_count = articles_count;
        this.liked=liked;
    }


    public CategoryObject(String ID, String catName, String articles_count, boolean liked) {
        this.ID = ID;
        this.catName = catName;
        this.articles_count = articles_count;
        this.liked=liked;
    }



    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public CategoryObject() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getArticles_count() {
        return articles_count;
    }

    public void setArticles_count(String articles_count) {
        this.articles_count = articles_count;
    }


}
