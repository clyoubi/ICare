package com.smookcreative.icare.ILearn;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.smookcreative.icare.AboutActivity;
import com.smookcreative.icare.Adapters.ArticleAdapter;
import com.smookcreative.icare.Adapters.ArticleObject;
import com.smookcreative.icare.Adapters.CategoryFilterAdapter;
import com.smookcreative.icare.Adapters.CategoryObject;
import com.smookcreative.icare.Adapters.DepechesObject;
import com.smookcreative.icare.Adapters.RecycleCategoriesAdapters;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.DatabasesManagers.Singleton;
import com.smookcreative.icare.Login;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Tips.Fonts;
import com.smookcreative.icare.firebase.LikeService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class NewsFragment extends android.support.v4.app.Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private static ProgressDialog pDialog;

    public static ArrayList<ArticleObject> articles;
    public static ArrayList<DepechesObject> depeches = new ArrayList<DepechesObject>();
    private int mPage;
    private static ArticleAdapter adapter;
    private static boolean loading =false;
    private static boolean first =true;
    private String params="?type=1";
    private int offset;
    private RecyclerView horizontal_recycler_view;
    private CategoryFilterAdapter recycleCategoriesAdapters;
    private ArrayList<CategoryObject> categoriesList =new ArrayList<>();
    private boolean isOpen;
    private RelativeLayout no_result_layout;
    private MenuItem item;
    private  ListView listView;
    private ProgressBar progressBar;



    public static NewsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        NewsFragment fragment = new NewsFragment();
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
        View view = inflater.inflate(R.layout.ilearn_news, container, false);


        listView = view.findViewById(R.id.listView);
        //progressBar = view.findViewById(R.id.more_loader);
        articles = new ArrayList<ArticleObject>();

        // no result layout
        no_result_layout = view.findViewById(R.id.no_result_emoji);


        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articles.clear();
                categoriesList.clear();
                first =true;
                LoadDatas(""); // your code
                CategoryFilterAdapter.filterActivated=false;
                LoadCategories();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    item.getIcon().setTint(getResources().getColor(R.color.white));
                }
                pullToRefresh.setRefreshing(false);
            }
        });
        offset=0;
        articles.clear();
        adapter = new ArticleAdapter(view.getContext(), articles);
        listView.setAdapter(adapter);

        LoadDatas("");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position<articles.size()){
                    Intent intent = new Intent(parent.getContext(),ArticleDetail.class);
                    intent.putExtra("article", articles.get(position));
                    intent.putExtra("type", "article");
                    ArticleDetail.saveMode = false;
                        startActivity(intent);
                }else{
                   LoadDatas("&&args=offset,"+String.valueOf(articles.size()-1));
                }
            }
        });





listView.setOnScrollListener(new AbsListView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == (totalItemCount - visibleItemCount)) {
           /*if(!loading && !first){
               //Toast.makeText(getContext(),"fin des articles",Toast.LENGTH_SHORT).show();
               offset=offset+2;
               LoadDatas(offset);
               loading=true;
           }*/
            //Toast.makeText(getContext(),"fin des articles",Toast.LENGTH_SHORT).show();
        }
    }
});

        horizontal_recycler_view = (RecyclerView) view.findViewById(R.id.categories_filter);

        recycleCategoriesAdapters = new CategoryFilterAdapter(getContext(), categoriesList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(recycleCategoriesAdapters);

        LoadCategories();


        final RelativeLayout notificationLayout = (RelativeLayout)view.findViewById(R.id.notificationLayout);
        ImageView closeNotification = (ImageView)view.findViewById(R.id.closeNotification);
        Button actionButton = view.findViewById(R.id.actionButton);
        TextView note = view.findViewById(R.id.notificationText);

        final TextView notifcationText = view.findViewById(R.id.notificationText);
            closeNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //notificationLayout.setVisibility(View.INVISIBLE);
                    //notificationLayout.animate().scaleXBy((float) -0.3).scaleYBy((float) -0.3).alpha(0);
                    notificationLayout.animate().alpha(0).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            notificationLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
            });



            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    ArticleObject obj = articles.get(0);

                    AssyncNotification img = new AssyncNotification(obj.getBanner());
                        Bitmap bitmap = img.doInBackground(obj.getBanner());

                    Intent notificationIntent = new Intent(getContext(), ArticleDetail.class);
                        notificationIntent.putExtra("article", obj);
                        notificationIntent.putExtra("type", "article");

                    final PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent,
                            PendingIntent.FLAG_ONE_SHOT);

                    Intent likeIntent = new Intent(getContext(),AboutActivity.class);
                        PendingIntent likePendingIntent = PendingIntent.getService(getContext(), Integer.parseInt(obj.getId()),likeIntent,PendingIntent.FLAG_ONE_SHOT);



                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), "ADMIN_CHANNEL_ID")
                            .setLargeIcon(bitmap)
                            .setSmallIcon(R.mipmap.i_care_launcher)  //a resource for your custom small icon
                            .setContentTitle(obj.getTitle()) //the "title" value you sent in your notification
                            .setContentText(obj.getTitle()) //ditto
                            .setAutoCancel(true)  //dismisses the notification on click
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .setSummaryText(obj.getCategories())
                                    .bigPicture(bitmap))
                            .addAction(R.drawable.like_blue,
                                    "J'aime",likePendingIntent); //Setting the action;


                    NotificationManager notificationManager =
                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(Integer.parseInt(articles.get(0).getId()) /* ID of notification */, notificationBuilder.build());

                }
            });



        return view;
    }


    public Bitmap getBitmapfromUrl(String imageUrl) {

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error Firebase Image", e.toString());
            return BitmapFactory.decodeResource(getResources(),R.drawable.derick_akimba);
        }
    }


    public void LoadDatas(final String args){

        String URLRequest;
        URLRequest=RemoteServer.ILearn_News_Script+params+args;

        listView.setVisibility(View.INVISIBLE);
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
                                no_result_layout.setVisibility(View.INVISIBLE);

                                for (int i=1; i<jsonArray.length();i++){

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    ArticleObject art =  new ArticleObject();

                                    art.setId(obj.getString("id"));
                                    art.setAuthor_id(obj.getString("author_id"));
                                    art.setTitle(obj.getString("title"));
                                    art.setBanner(obj.getString("thumbnail"));
                                    art.setPostOn(obj.getString("post_since"));
                                    art.setSource(obj.getString("source"));
                                    art.setAuthor(obj.getString("author_name"));
                                    art.setAuthor_pic(obj.getString("author_picture"));
                                    art.setAuthor_email(obj.getString("author_email"));
                                    art.setAuthor_bio(obj.getString("author_description"));
                                    art.setContent(obj.getString("content"));
                                    art.setLink(obj.getString("link"));
                                    art.setCredit_photo(obj.getString("credits"));
                                    art.setCategories(obj.getString("category"));
                                    art.setComment_count(obj.getString("comments_count"));
                                    art.setRate(obj.getString("rate"));
                                    art.setAuthor_rate(obj.getString("author_rate"));

                                    articles.add(art);
                                }
                               //articles.add(new ArticleObject("Plus d'articles",""));
                                adapter.notifyDataSetChanged();
                                listView.setVisibility(View.VISIBLE);

                            }else{
                               //Toast.makeText(getContext(), "Not found", Toast.LENGTH_LONG).show();
                                    adapter.notifyDataSetChanged();
                                    no_result_layout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Try error", Toast.LENGTH_LONG).show();
                        }
                        listView.setAdapter(adapter);
                        loading = false;
                        first = false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(container.getContext(),"Check your Network settings",Toast.LENGTH_LONG).show();
                hidePDialog();
                error.printStackTrace();
                               if(articles.size()==0)
                    no_result_layout.setVisibility(View.VISIBLE);

                //Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }){

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



    private void LoadCategories() {

        String params = "?";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, RemoteServer.ILearn_Categories_Script, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject code = jsonArray.getJSONObject(0);


                    if (code.getString("response").equals("found")) {

                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            CategoryObject cat = new CategoryObject();
                            cat.setID(obj.getString("id"));
                            cat.setCatName(obj.getString("name"));
                            cat.setArticles_count(obj.getString("count"));
                            if(obj.getString("liked").equals("true"))
                                cat.setLiked(true);
                            else
                                cat.setLiked(false);
                            categoriesList.add(cat);
                        }
                        categoriesList.add(0, new CategoryObject("0","Tous","1", true));
                        categoriesList.add(1, new CategoryObject("123457","Favoris","1"));

                    } else {
                        Toast.makeText(getContext(), "", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

                recycleCategoriesAdapters.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Snackbar.make(listView, "Probleme de connexion au serveur veuillez réessayer", 2000).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    private void RevealFilterLayout(MenuItem item){
        if(!isOpen){
            horizontal_recycler_view.setVisibility(View.VISIBLE);
            item.setIcon(getResources().getDrawable(R.drawable.ic_done_all_black_24dp));
            isOpen=true;
        }else{
            horizontal_recycler_view.setVisibility(View.INVISIBLE);
            item.setIcon(getResources().getDrawable(R.drawable.ic_filter_list_black_24dp));
            isOpen=false;

                //Toast.makeText(getContext(),CategoryFilterAdapter.categories_selected.get(i), Toast.LENGTH_LONG).show();
            String choosen= "0";
            try {
                choosen = recycleCategoriesAdapters.Categories_Choosen();
                if(CategoryFilterAdapter.filterActivated){
                    LoadDatas("&&args=cat,"+choosen);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        item.getIcon().setTint(getResources().getColor(R.color.notification));
                    }
                    if(choosen.equals("[\"0\"]")){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            item.getIcon().setTint(getResources().getColor(R.color.white));
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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


        MenuItem searchSelected = menu.findItem(R.id.filter);
        item =searchSelected;
        searchSelected.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                RevealFilterLayout(item);
                return true;
            }
        });

        /*MenuItem saveMenuItem = menu.findItem(R.id.saved);
            saveMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                   Intent intent = new Intent(getContext(),SavedArticles.class);
                        startActivity(intent);
                    return true;
                }
            });*/
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }



}


class AssyncNotification extends AsyncTask<String, Bitmap, Bitmap>{

    String src;
    public AssyncNotification(String src) {
        this.src = src;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error Firebase Image", e.toString());
            return null;
        }
    }

}