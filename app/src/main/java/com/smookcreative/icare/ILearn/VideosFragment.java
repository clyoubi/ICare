package com.smookcreative.icare.ILearn;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.smookcreative.icare.Adapters.DepechesObject;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.DatabasesManagers.Singleton;
import com.smookcreative.icare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideosFragment extends android.support.v4.app.Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;


    private static ArrayList<ArticleObject> articles = new ArrayList<ArticleObject>();

    private static ArticleAdapter adapter;
    private static boolean loading =false;
    private static boolean first =true;
    private String params="?type=3";
    private int offset;
    private static ProgressDialog pDialog;
    private  ListView listView;
    private ProgressBar progressBar;

    public static VideosFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        VideosFragment fragment = new VideosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        setHasOptionsMenu(true);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ilearn_videos, container, false);
        
        listView = view.findViewById(R.id.listView);
        //progressBar = view.findViewById(R.id.more_loader);

        LoadDatas("");

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articles.clear();
                first =true;
                LoadDatas(""); // your code
                pullToRefresh.setRefreshing(false);

            }
        });
        offset=0;
        articles.clear();
        adapter = new ArticleAdapter(view.getContext(), articles,2);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position<articles.size()){
                    Intent intent = new Intent(parent.getContext(),YouTubePlayer.class);
                    intent.putExtra("link", articles.get(position).getLink());
                    intent.putExtra("youtube_code", articles.get(position).getVideoId());
                    startActivity(intent);
                }else{
                    LoadDatas("&&args=offset,"+String.valueOf(articles.size()-1));
                }


            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void LoadDatas(final String args){

        listView.setVisibility(View.INVISIBLE);
        String URLRequest;
        URLRequest= RemoteServer.ILearn_News_Script+"?type=3"+args;

        pDialog = new ProgressDialog(getContext());
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
                                    art.setLink(obj.getString("link"));
                                    art.setCredit_photo(obj.getString("credits"));
                                    art.setCategories(obj.getString("category"));
                                    art.setComment_count(obj.getString("comments_count"));
                                    art.setVideoId(obj.getString("video_code"));

                                    articles.add(art);
                                }
                                //articles.add(new ArticleObject("Plus de vidéos",""));

                            }else{
                                Toast.makeText(getContext(), "Not found", Toast.LENGTH_LONG).show();
                            }
                            //Toast.makeText(getContext(), "succes!!!", Toast.LENGTH_LONG).show();
                            listView.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Try error", Toast.LENGTH_LONG).show();
                        }

                        adapter.notifyDataSetChanged();
                        loading = false;
                        first = false;
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
        Singleton.getInstance(getContext()).addToRequestqueue(movieReq);
        //AppController.getInstance().addToRequestQueue(movieReq);

    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String args="&&args=s,"+query;
                articles.clear();
                LoadDatas(args);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }



}
