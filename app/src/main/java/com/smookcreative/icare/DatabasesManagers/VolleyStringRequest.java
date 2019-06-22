package com.smookcreative.icare.DatabasesManagers;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.smookcreative.icare.Adapters.ArticleAdapter;
import com.smookcreative.icare.Adapters.ArticleObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class VolleyStringRequest {

    private  Context context;
    private   ArrayList<ArticleObject> articles = new ArrayList<>();
    private   ArticleAdapter adapter;
    private   ListView listview;

    //views
    private static ProgressDialog pDialog;



    public VolleyStringRequest(Context context, ListView listview, ArticleAdapter adapter) {
        this.context = context;
        this.listview = listview;
        this.adapter = adapter;
    }




    public void LoadMoreNews(String args){

        listview.setVisibility(View.INVISIBLE);
        pDialog = new ProgressDialog(context);
        // Showing progress dialog before making http request
        pDialog.setMessage("Chargemment...");
        pDialog.show();
        //depeches.clear();
        String URLRequest;
        URLRequest=RemoteServer.ILearn_Alerts_Script+args;

        StringRequest depechesRequest = new StringRequest(Request.Method.GET, URLRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        hidePDialog();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject code=jsonArray.getJSONObject(0);


                            if(code.getString("status").equals("found")){


                                for (int i=1; i<jsonArray.length();i++){

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
                                //depeches.add(new ArticleObject("More",""));
                            }else{

                            }
                            //Toast.makeText(getContext(), "succes!!!", Toast.LENGTH_LONG).show();
                            listview.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(container.getContext(),"Check your Network settings",Toast.LENGTH_LONG).show();
                hidePDialog();
                error.printStackTrace();
                //Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(depechesRequest);

    }




    private static void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


}
