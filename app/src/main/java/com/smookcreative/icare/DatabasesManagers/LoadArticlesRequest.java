package com.smookcreative.icare.DatabasesManagers;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.util.HashMap;
import java.util.Map;

public class LoadArticlesRequest {

    Context context;
    ArticleAdapter adapter;
    ArrayList<ArticleObject> articles;
    ProgressDialog pDialog;
    boolean loading;
    String args="";

    public LoadArticlesRequest(Context context, ArticleAdapter adapter,  ArrayList<ArticleObject> articles) {
        this.context = context;
        this.adapter = adapter;
        this.articles = articles;
    }


    public LoadArticlesRequest(Context context, ArticleAdapter adapter,  ArrayList<ArticleObject> articles, String args) {
        this.context = context;
        this.adapter = adapter;
        this.articles = articles;
        this.args=args;
    }



    public void LoadDatas(final String args){

        String URLRequest;
        URLRequest=RemoteServer.ILearn_News_Script+args;

        pDialog = new ProgressDialog(context);
        // Showing progress dialog before makinthisg http request
        pDialog.setMessage("Chargemment...");
        pDialog.show();

        StringRequest movieReq = new StringRequest(Request.Method.GET, URLRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();
                        loading = true;
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


                            }else{
                                Toast.makeText(context, "Not found", Toast.LENGTH_LONG).show();
                            }
                            //Toast.makeText(getContext(), "succes!!!", Toast.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Try error", Toast.LENGTH_LONG).show();
                        }

                        adapter.notifyDataSetChanged();
                        loading = false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(container.getContext(),"Check your Network settings",Toast.LENGTH_LONG).show();
                hidePDialog();
                error.printStackTrace();
                //Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("args",args);
                return hashMap;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    articles.clear();

                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };


        // Adding request to request queue
        Singleton.getInstance(context).addToRequestqueue(movieReq);
        //AppController.getInstance().addToRequestQueue(movieReq);
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }



}
