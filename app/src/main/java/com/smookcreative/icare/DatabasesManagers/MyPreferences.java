package com.smookcreative.icare.DatabasesManagers;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.smookcreative.icare.Adapters.ArticleObject;
import com.smookcreative.icare.Adapters.CategoryObject;
import com.smookcreative.icare.Adapters.UserInfos;
import com.smookcreative.icare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MyPreferences {

private Context context;
private SharedPreferences sharedPreferences;


    public MyPreferences(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(context.getResources().getString(R.string.login_preferences), Context.MODE_PRIVATE);
    }


    public void writeLoginStatus(boolean status){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status), status);
        editor.commit();
    }

    public boolean ActiveAudio(boolean audio){
        boolean saved;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.is_audio_conversion),audio);
        saved = editor.commit();
        return saved;
    }


    public boolean isAudioConversion(){
        boolean id;
        id = sharedPreferences.getBoolean(context.getResources().getString(R.string.is_audio_conversion), false);
        return id;
    }


    public boolean readLoginStatus(){
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status), false);
        return status;
    }

    public boolean saveUserinfos(UserInfos userInfos){
        boolean saved;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getResources().getString(R.string.user_ID),userInfos.getId());
            editor.putString(context.getResources().getString(R.string.user_fisrt_name),userInfos.getFirst_name());
            editor.putString(context.getResources().getString(R.string.user_last_name),userInfos.getLast_name());
            editor.putString(context.getResources().getString(R.string.user_display_name),userInfos.getDisplay_name());
            editor.putString(context.getResources().getString(R.string.user_login),userInfos.getLogin_name());
            editor.putString(context.getResources().getString(R.string.user_role),userInfos.getAccount_type());
            editor.putString(context.getResources().getString(R.string.user_email),userInfos.getEmail());
            editor.putString(context.getResources().getString(R.string.user_hash),userInfos.getUser_hash());
            editor.putString(context.getResources().getString(R.string.user_status),userInfos.getUser_status());
            editor.putString(context.getResources().getString(R.string.favourite_list),userInfos.getUser_favourites());
            editor.putString(context.getResources().getString(R.string.user_picture),userInfos.getPictureUrl());
            editor.putBoolean(context.getResources().getString(R.string.is_facebook_login),userInfos.isFacebook_login());
            editor.putString(context.getResources().getString(R.string.user_city),userInfos.getCity());



            editor.putBoolean(context.getResources().getString(R.string.login_status), true);

            saved = editor.commit();

        return saved;
    }

    public  boolean DeleteUserInfos(){
        boolean clear = sharedPreferences.edit().clear().commit();
        writeLoginStatus(false);
        return clear;
    }

    public String readUserID(){
        String id = "0";
        id = sharedPreferences.getString(context.getResources().getString(R.string.user_ID), "0");
        return id;
    }

    public UserInfos readUserInfos(){

            UserInfos user = new UserInfos();
            user.setId(sharedPreferences.getString(context.getResources().getString(R.string.user_ID), "0"));
            user.setDisplay_name(sharedPreferences.getString(context.getResources().getString(R.string.user_display_name), "0"));
            user.setFirst_name(sharedPreferences.getString(context.getResources().getString(R.string.user_fisrt_name), ""));
            user.setLast_name(sharedPreferences.getString(context.getResources().getString(R.string.user_last_name), ""));
            user.setDisplay_name(sharedPreferences.getString(context.getResources().getString(R.string.user_display_name), ""));
            user.setEmail(sharedPreferences.getString(context.getResources().getString(R.string.user_email), ""));
            user.setNice_name(sharedPreferences.getString(context.getResources().getString(R.string.user_nice_name), ""));
            user.setLogin_name(sharedPreferences.getString(context.getResources().getString(R.string.user_login), ""));
            user.setAccount_type(sharedPreferences.getString(context.getResources().getString(R.string.user_role), ""));
            user.setUser_hash(sharedPreferences.getString(context.getResources().getString(R.string.user_hash), ""));
            user.setUser_status(sharedPreferences.getString(context.getResources().getString(R.string.user_status), "0"));
            user.setUser_favourites(sharedPreferences.getString(context.getResources().getString(R.string.favourite_list), "0"));
            user.setPictureUrl(sharedPreferences.getString(context.getResources().getString(R.string.user_picture), ""));
            user.setCity(sharedPreferences.getString(context.getResources().getString(R.string.user_city), ""));
            user.setFacebook_login(sharedPreferences.getBoolean(context.getResources().getString(R.string.is_facebook_login),false));

        return user;
    }



    public boolean AddNewUserInfo(String stringId, String value, boolean update){
        boolean saved=false;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(stringId,value);
        if(update)
            editor.apply();
        else
            saved = editor.commit();
        return saved;
    }


    public boolean SaveArticles(ArticleObject article){
        boolean saved = false;
        JSONArray jsonArray = null;

        String art = sharedPreferences.getString(context.getResources().getString(R.string.saved_articles), "[]");

            try {
                 jsonArray = new JSONArray(art);
                    if(jsonArray.length()<100){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", article.getId());
                        jsonObject.put("title", article.getTitle());
                        jsonObject.put("thumbnail", article.getBanner());
                        jsonObject.put("post_since", article.getPostOn());
                        jsonObject.put("source", article.getSource());
                        jsonObject.put("author_name", article.getAuthor());
                        jsonObject.put("content", article.getContent());
                        jsonObject.put("link", article.getLink());
                        jsonObject.put("credits", article.getCredit_photo());
                        jsonObject.put("category", article.getCategories());
                        jsonObject.put("comments_count", article.getComment_count());

                        jsonArray.put(jsonObject);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(context.getResources().getString(R.string.saved_articles),jsonArray.toString());
                        saved = editor.commit();
                    }else {
                        Toast.makeText(context, "Désolé nombre maximum atteint vous ne pouvez plus enregister", Toast.LENGTH_LONG).show();
                    }


               // Toast.makeText(context, jsonArray.toString(),Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return saved;
    }

        public ArrayList<ArticleObject> ReadSavedArticles(){
            ArrayList<ArticleObject> articles = new ArrayList<>();

            String arts = sharedPreferences.getString(context.getResources().getString(R.string.saved_articles), "[]");

            try {
                JSONArray jsonArray = new JSONArray(arts);

                for (int i=0; i<jsonArray.length(); i++){

                    JSONObject obj = jsonArray.getJSONObject(i);
                    ArticleObject art =  new ArticleObject();

                    art.setId(obj.getString("id"));
                    art.setTitle(obj.getString("title"));
                    art.setBanner(obj.getString("thumbnail"));
                    art.setPostOn(obj.getString("post_since"));
                    art.setSource(obj.getString("source"));
                    art.setAuthor(obj.getString("author_name"));
                    art.setContent(obj.getString("content"));
                    art.setLink(obj.getString("link"));
                    art.setCredit_photo(obj.getString("credits"));
                    art.setCategories(obj.getString("category"));
                    art.setComment_count(obj.getString("comments_count"));

                    articles.add(art);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return articles;
        }


        public void DeleteArticle(int position){

        JSONArray jsonArray =new JSONArray();

            boolean operation = false;
                        ArrayList<ArticleObject> articles = new ArrayList<>();
                            articles = ReadSavedArticles();
                                articles.remove(position);

                           /* if (AddNewUserInfo(context.getResources().getString(R.string.favourite_list), "[]",false)){
                                for (ArticleObject object:articles) {
                                    operation= SaveArticles(object);
                                }
                            }*/


            try {

                for (ArticleObject article:articles) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", article.getId());
                    jsonObject.put("title", article.getTitle());
                    jsonObject.put("thumbnail", article.getBanner());
                    jsonObject.put("post_since", article.getPostOn());
                    jsonObject.put("source", article.getSource());
                    jsonObject.put("author_name", article.getAuthor());
                    jsonObject.put("content", article.getContent());
                    jsonObject.put("link", article.getLink());
                    jsonObject.put("credits", article.getCredit_photo());
                    jsonObject.put("category", article.getCategories());
                    jsonObject.put("comments_count", article.getComment_count());

                    jsonArray.put(jsonObject);

                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(context.getResources().getString(R.string.saved_articles),jsonArray.toString());
                operation = editor.commit();

                // Toast.makeText(context, jsonArray.toString(),Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

                 if(operation)
                     Toast.makeText(context, "Supprimé", Toast.LENGTH_SHORT).show();
                 else
                     Toast.makeText(context, "Une erreur s'est produite", Toast.LENGTH_SHORT).show();
                 }

}
