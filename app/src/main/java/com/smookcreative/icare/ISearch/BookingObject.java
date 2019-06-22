package com.smookcreative.icare.ISearch;

import android.os.Parcel;
import android.os.Parcelable;
import com.smookcreative.icare.Adapters.CommentsObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Random;


public class BookingObject implements Parcelable {

    int typeListID;
    String id;
    String name;
    String website="---";
    String email="---";
    String pricing="---";

        String quater="---", location_precision="---", hours_resume;
    boolean open, i_like, biometry;
    int places, likes, comments_count, color;

    // JSON Array
    String pictures="[]", phone, specialities, comments;

    //JSON Object
    String location;
    String hours;
    String contact;


    String more_infos;

    //Other
    String country, coordinates;


    // extends personnel object JSON Object
    String work_place, grade;

    // Strings
    String work_place_ID, work_place_Name, work_place_City;
        String gradeAbreviation, specia;
    int experience_years;

    //constructor
    public BookingObject() {
        Random random = new Random();
        int color = random.nextInt(4);
        this.color = color;
    }


    public static final Creator<BookingObject> CREATOR = new Creator<BookingObject>() {
        @Override
        public BookingObject createFromParcel(Parcel in) {
            return new BookingObject(in);
        }

        @Override
        public BookingObject[] newArray(int size) {
            return new BookingObject[size];
        }
    };


    public int getTypeListID() {
        return typeListID;
    }

    public void setTypeListID(int typeListID) {
        this.typeListID = typeListID;
    }

    public String getMore_infos() {
        return more_infos;
    }

    public void setMore_infos(String more_infos) {

        try {
            JSONObject object = new JSONObject(more_infos);
            StringBuilder more = new StringBuilder("");

            if(!object.getString("bp").equals("")){
                more.append("<br>").append("<strong>BP: </strong>").append(object.get("bp"));
            }

            if(!object.getString("fax").equals("")){
                more.append("<br>").append("<strong>FAX: </strong>").append(object.get("fax"));
            }

            if(isBiometry()){
                more.append("<i>Système biométrique</i>");
            }

            this.more_infos = more.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getContact() {
        return contact.toString();
    }


    public void setContact(String contact) {
        this.contact = contact;
        try {
            JSONObject array = new JSONObject(contact);
            this.phone = array.getJSONArray("phone").toString();
            this.email = array.getString("email");
            this.website = array.getString("website");
            setMore_infos(array.getJSONObject("other").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean isI_like() {
        return i_like;
    }

    public void setI_like(boolean i_like) {
        this.i_like = i_like;
    }



    public String getPlaces() {
        return String.valueOf(places);
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public String getLikes() {
        return String.valueOf(likes);
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getComments_count() {
        return String.valueOf(comments_count);
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        if(!email.equals("")){
            email = "<strong>Email: </strong>"+email;
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(JSONArray pictures) {
        this.pictures = pictures.toString();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(JSONArray phone) {
        this.phone = phone.toString();
    }

    public String getSpecialities() {

        StringBuilder specialityList = new StringBuilder("<ul>");
        try {
            JSONArray array = new JSONArray(specialities);
            for(int i =0; i<array.length(); i++){
                String item = "<li>"+array.getString(i)+"</li>";
                specialityList.append(item);
            }
            specialityList.append("</ul>");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return specialityList.toString();
    }

    public boolean isBiometry() {
        return biometry;
    }

    public void setBiometry(boolean biometry) {
        this.biometry = biometry;
    }


    public int getColor(){
        return color;
    }

    public void setSpecialities(JSONArray specialities) {
        this.specialities = specialities.toString();
    }

    /*public ArrayList<CommentsObject> getComments() {
        ArrayList<CommentsObject> arrayList = new ArrayList<>();

        try {

            for(int i=0; i<comments.length(); i++){
                CommentsObject commentsObject = new CommentsObject();

                    commentsObject.setId(String.valueOf(comments.getJSONObject(i).getInt("comment_ID")));
                    commentsObject.setAuthor_email(comments.getJSONObject(i).getString("author_email"));
                    commentsObject.setName(comments.getJSONObject(i).getString("author_name"));
                    commentsObject.setComment(comments.getJSONObject(i).getString("comment"));
                    commentsObject.setPost_date(comments.getJSONObject(i).getString("date"));

                arrayList.add(commentsObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    public void setComments(JSONArray comments) {
        this.comments = comments;
    }*/

    public String getLocation() {
        return location;
    }

    public void setLocation(JSONObject location) {
        this.location = location.toString();
        try {
            setQuater(location.getString("quater"));
            setLocation_precision(location.getString("location"));
            setCoordinates(location.getJSONArray("coordinates"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setCoordinates(JSONArray coordinates) {
        this.coordinates = coordinates.toString();
    }

    public String getCoordinates() {
        return coordinates;
    }



    public String getQuater() {
        return quater;
    }


    public void setQuater(String quater) {
        this.quater = quater;
    }


    public String getLocation_precision() {
        return location_precision;
    }

    public void setLocation_precision(String location_precision) {
        this.location_precision = location_precision;
    }

  

    public String getHours_resume() {
        return hours_resume;
    }

    public void setHours_resume(String hours_resume) {
        this.hours_resume = hours_resume;
    }


    public String getHours() {
        String resumeHour ="---";
        try {
            JSONObject object = new JSONObject(hours);
            resumeHour = object.getString("resume");
            this.hours_resume = resumeHour;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resumeHour;
    }

    public void setHours(JSONObject hours) {
        this.hours = hours.toString();
    }


    // extends personnel object

    public String getWork_place() {
        return work_place;
    }

    public void setWork_place(JSONObject work_place) {
        this.work_place = work_place.toString();
        try {
            this.work_place_ID = work_place.getString("ID");
            this.work_place_Name = work_place.getString("name");
            this.work_place_City = work_place.getString("city");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getWork_place_ID() {
        return work_place_ID;
    }

    public String getWork_place_Name() {
        return work_place_Name;
    }

    public String getWork_place_City() {
        return work_place_City;
    }


    public String getGrade() {
        return grade;
    }

    public void setGrade(JSONObject grade) {
        this.grade = grade.toString();
        try {
            this.gradeAbreviation = grade.getString("grade");
            this.specia = grade.getJSONArray("specialities").getString(0);
            this.experience_years = grade.getInt("experience");
            setSpecialities(grade.getJSONArray("specialities"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getGradeAbreviation() {
        return gradeAbreviation;
    }

    public String getSpecialite() {
        return specia;
    }


    public int getExperience_years() {
        return experience_years;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    protected BookingObject(Parcel in){
        typeListID = in.readInt();
        id=in.readString();
        name=in.readString();
        website=in.readString();
        email=in.readString();
        pricing=in.readString();
        quater=in.readString();
        location_precision=in.readString();
        hours_resume=in.readString();
        open=in.readByte()!=0;
        i_like=in.readByte()!=0;
        places=in.readInt();
        likes=in.readInt();
        comments_count=in.readInt();
        work_place_ID=in.readString();
        work_place_Name=in.readString();
        work_place_City=in.readString();
        gradeAbreviation=in.readString();
        specia=in.readString();
        experience_years=in.readInt();

        pictures=in.readString();
        phone=in.readString();
        specialities=in.readString();
        comments=in.readString();

        location=in.readString();
        hours=in.readString();
        contact=in.readString();
        work_place=in.readString();
        grade=in.readString();
        more_infos=in.readString();
        open=in.readByte()!=0;


        /*pictures = in.readParcelable(JSONArray.class.getClassLoader());
        phone = in.readParcelable(JSONArray.class.getClassLoader());
        specialities = in.readParcelable(JSONArray.class.getClassLoader());
        comments = in.readParcelable(JSONArray.class.getClassLoader());

        location = in.readParcelable(JSONObject.class.getClassLoader());
        hours = in.readParcelable(JSONObject.class.getClassLoader());
        contact = in.readParcelable(JSONObject.class.getClassLoader());
            work_place = in.readParcelable(JSONObject.class.getClassLoader());
            grade = in.readParcelable(JSONObject.class.getClassLoader());
*/

    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(typeListID);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(website);
        dest.writeString(email);
        dest.writeString(pricing);
        dest.writeString(quater);
        dest.writeString(location_precision);
        dest.writeString(hours_resume);
        dest.writeByte((byte) (open ? 1 : 0));
        dest.writeByte((byte) (i_like ? 1 : 0));
        dest.writeInt(places);
        dest.writeInt(likes);
        dest.writeInt(comments_count);
        dest.writeString(work_place_ID);
        dest.writeString(work_place_Name);
        dest.writeString(work_place_City);
        dest.writeString(gradeAbreviation);
        dest.writeString(specia);
        dest.writeInt(experience_years);

        dest.writeString(pictures);
        dest.writeString(phone);
        dest.writeString(specialities);
        dest.writeString(comments);

        dest.writeString(location);
        dest.writeString(hours);
        dest.writeString(contact);
        dest.writeString(work_place);
        dest.writeString(grade);
        dest.writeString(more_infos);
        dest.writeByte((byte) (biometry ? 1 : 0));

        /*dest.writeParcelable((Parcelable) pictures, flags);
        dest.writeParcelable((Parcelable) phone, flags);
        dest.writeParcelable((Parcelable) specialities, flags);
        dest.writeParcelable((Parcelable) comments, flags);

        dest.writeParcelable((Parcelable) location, flags);
        dest.writeParcelable((Parcelable) hours, flags);
        dest.writeParcelable((Parcelable) contact, flags);
        dest.writeParcelable((Parcelable) work_place, flags);
        dest.writeParcelable((Parcelable) grade, flags);*/
    }



}
