package com.smookcreative.icare.Adapters;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleObject implements Parcelable {


    private String banner, title, postOn, source, content, link, credit_photo, src;
    private String categories="adverts", comment_count;
    private String id, videoId, rate, author_rate, author_bio, author_id, author_pic, author_email;

    public ArticleObject() {
    }

    public ArticleObject(String id, String banner, String title, String postOn, String source, String content, String link, String credit_photo, String categories, String author, String comment_count, String videoId, String author_bio, String rate, String author_rate, String author_id, String author_pic, String author_email) {
        this.id=id;
        this.banner = banner;
        this.title = title;
        this.postOn = postOn;
        this.source = source;
        this.content = content;
        this.link=link;
        this.credit_photo=credit_photo;
        this.categories =categories;
        this.src=author;
        this.comment_count=comment_count;
        this.author_bio = author_bio;
        this.rate = rate;
        this.author_rate = author_rate;
        this.author_id = author_id;
        this.author_pic = author_pic;
        this.author_email = author_email;
    }


    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_pic() {
        return author_pic;
    }

    public void setAuthor_pic(String author_pic) {
        this.author_pic = author_pic;
    }

    public String getAuthor_rate() {
        return author_rate;
    }

    public void setAuthor_rate(String author_rate) {
        this.author_rate = author_rate;
    }

    public String getAuthor_bio() {
        return author_bio;
    }

    public void setAuthor_bio(String author_bio) {
        this.author_bio = author_bio;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public ArticleObject(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getAuthor() {
        return src;
    }

    public void setAuthor(String src) {
        this.src = src;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }



    protected ArticleObject(Parcel in) {
        id=in.readString();
        banner = in.readString();
        title = in.readString();
        postOn = in.readString();
        source = in.readString();
        content = in.readString();
        link = in.readString();
        credit_photo = in.readString();
        categories = in.readString();
        src=in.readString();
        comment_count=in.readString();
        videoId=in.readString();
        author_bio=in.readString();
        rate=in.readString();
        author_rate=in.readString();
        author_id=in.readString();
        author_pic=in.readString();
        author_email=in.readString();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final Creator<ArticleObject> CREATOR = new Creator<ArticleObject>() {
        @Override
        public ArticleObject createFromParcel(Parcel in) {
            return new ArticleObject(in);
        }

        @Override
        public ArticleObject[] newArray(int size) {
            return new ArticleObject[size];
        }
    };


    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getCredit_photo() {
        return credit_photo;
    }

    public void setCredit_photo(String credit_photo) {
        this.credit_photo = credit_photo;
    }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content;}

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(banner);
        dest.writeString(title);
        dest.writeString(postOn);
        dest.writeString(source);
        dest.writeString(content);
        dest.writeString(link);
        dest.writeString(credit_photo);
        dest.writeString(categories);
        dest.writeString(src);
        dest.writeString(comment_count);
        dest.writeString(videoId);
        dest.writeString(author_bio);
        dest.writeString(rate);
        dest.writeString(author_rate);
        dest.writeString(author_id);
        dest.writeString(author_pic);
        dest.writeString(author_email);

    }
}
