package com.smookcreative.icare;

import android.Manifest;
import android.animation.Animator;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.smookcreative.icare.Adapters.HealthTypeObject;
import com.smookcreative.icare.Adapters.ILearn.CentersAdapter;
import com.smookcreative.icare.Adapters.PlacesAdapter;
import com.smookcreative.icare.Adapters.PlacesObject;
import com.smookcreative.icare.Adapters.RecycleHelthTypeAdapters;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.ILearn.ILearn;
import com.smookcreative.icare.ISearch.BookingObject;
import com.smookcreative.icare.ISearch.ItemDetail;
import com.smookcreative.icare.ISearch.MoreCities;
import com.smookcreative.icare.Tips.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ListView listView;
    public static PlacesAdapter adapter;
    private CentersAdapter centerAdapter;
    public static ArrayList<PlacesObject> places = new ArrayList<PlacesObject>();
    public static ArrayList<BookingObject> bookingList = new ArrayList<BookingObject>();
    public static ArrayList<ArrayList<BookingObject>> MainbookingList = new ArrayList<>();

    private ProgressDialog pDialog;
    private ArrayList<PlacesObject> yeah = new ArrayList<PlacesObject>();
    private ArrayList<PlacesObject> cities = new ArrayList<PlacesObject>();
    public int change = 0;
    private RelativeLayout PlacesMenu;
    private RelativeLayout FilterLocationLayout;
    private Toolbar toolbar;
    private boolean contextual = false, open_filtered;
    private String number = "";

    private boolean isOpen = false;
    private boolean isOpenFilterLayout = false;
    private boolean isLocationFiltrered = false;
    public static boolean IS_CENTER_ADATER;

    ImageView setupLocationFilterButton;
    ImageView deleteLocationFilterButton;
    SeekBar seekBar;
    TextView search_zone_textview;
    MyPreferences myPreferences;

    private Menu ToolbarMenu;

    RecyclerView horizontal_recycler_view;
    RecycleHelthTypeAdapters horizontalAdapter;
    public static ArrayList<HealthTypeObject> data = new ArrayList<>();

    Fonts yo = new Fonts();
    private Typeface ralewaybold, ralewayRegular, ralewaySemibold;
    private Menu menu;
    private MenuItem menuItem;

    static String datas_type_to_fecth = RemoteServer.PlacesScript;


    private RelativeLayout relativeLayoutDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(this);

            if(myPreferences.readUserInfos().getCity().equals("")){
                Intent intent = new Intent(this, MoreCities.class);
                intent.putExtra("launch", true);
                startActivity(intent);
                finish();
            }

        try {
            JSONArray jsonArray = new JSONArray(myPreferences.readUserInfos().getCity());
            datas_type_to_fecth = RemoteServer.PlacesScript+"?city="+jsonArray.get(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ralewayRegular = Typeface.createFromAsset(getAssets(), getString(R.string.ralewayRegular));
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        menu = bottomNavigationView.getMenu();
        menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        seekBar = (SeekBar) findViewById(R.id.search_zone_picker);
        seekBar.setMax(7);
        seekBar.setProgress(3);

        search_zone_textview = findViewById(R.id.search_zone_textview);
        search_zone_textview.setText(String.valueOf(seekBar.getProgress()));
        relativeLayoutDetail = findViewById(R.id.detailedView);
        relativeLayoutDetail.setVisibility(View.INVISIBLE);

        Fonts.setAllTextView(relativeLayoutDetail, ralewayRegular);
        TextView title1 = relativeLayoutDetail.findViewById(R.id.title1);
        TextView title2 = relativeLayoutDetail.findViewById(R.id.title2);
        TextView name = relativeLayoutDetail.findViewById(R.id.name);

        title1.setTypeface(ralewaySemibold);
        title2.setTypeface(ralewaySemibold);
        name.setTypeface(ralewaybold);

        Fonts.setAllTextView(toolbar, ralewayRegular);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                isLocationFiltrered = true;

                search_zone_textview.setText(String.valueOf(progress));

               /* if (isLocationFiltrered) {
                    MenuItem location = ToolbarMenu.findItem(R.id.ping_location);
                    location.setIcon(getResources().getDrawable(R.drawable.ic_done_all_black_24dp));
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        location.getIcon().setTint(getResources().getColor(R.color.notification));
                    }
                }*/

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        PlacesMenu = (RelativeLayout) findViewById(R.id.open);
        FilterLocationLayout = (RelativeLayout) findViewById(R.id.location_zone_layout);
        //setupLocationFilterButton =(ImageView)findViewById(R.id.setup_filter_action);
        deleteLocationFilterButton = (ImageView) findViewById(R.id.delete_location_zone);

        yo.setAllTextView(bottomNavigationView, ralewayRegular);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.news:
                        LaunchILearnActivity();
                        break;

                    case R.id.settings:
                        Intent ilearn_Activity = new Intent(getApplicationContext(), Settings.class);
                        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ilearn_Activity);
                        break;

                    case R.id.more:
                        Intent more_Activity = new Intent(getApplicationContext(), AboutActivity.class);
                        more_Activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        more_Activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(more_Activity);
                        break;
                }

                return true;
            }
        });


        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bookingList.clear();
                LoadDatas(datas_type_to_fecth); // your code
                pullToRefresh.setRefreshing(false);
            }
        });


        //get The city
        String city = myPreferences.readUserInfos().getCity();

        if (city.isEmpty()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider callin
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 112);
                    return;
                }

            }

        listView = (ListView) findViewById(R.id.listView);
        horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);

        // Creating volley request obj
        bookingList.clear();
        LoadDatas(datas_type_to_fecth);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this,ItemDetail.class);
                    intent.putExtra("color", bookingList.get(position).getColor());
                    intent.putExtra("item", bookingList.get(position));
                    intent.putExtra("position", position);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {


                    ActivityOptionsCompat activityOptions;
                    if(IS_CENTER_ADATER){
                        android.support.v4.util.Pair[] pairs = new android.support.v4.util.Pair[6];
                        android.support.v4.util.Pair<View, String> nameT = android.support.v4.util.Pair.create(view.findViewById(R.id.name), "nameTransition");
                        android.support.v4.util.Pair<View, String> locT = android.support.v4.util.Pair.create(view.findViewById(R.id.location), "locationTransition");
                        android.support.v4.util.Pair<View, String> icon = android.support.v4.util.Pair.create(view.findViewById(R.id.iconLayout), "imageTransition");
                        android.support.v4.util.Pair<View, String> sub = android.support.v4.util.Pair.create(view.findViewById(R.id.line2), "subDetailTransition");

                        activityOptions = makeSceneTransitionAnimation(MainActivity.this, nameT, locT, icon, sub);
                    }else{

                        android.support.v4.util.Pair[] pairs = new android.support.v4.util.Pair[5];
                        android.support.v4.util.Pair<View, String> nameT = android.support.v4.util.Pair.create(view.findViewById(R.id.name), "nameTransition");
                        android.support.v4.util.Pair<View, String> locT = android.support.v4.util.Pair.create(view.findViewById(R.id.location), "locationTransition");
                        android.support.v4.util.Pair<View, String> icon = android.support.v4.util.Pair.create(view.findViewById(R.id.iconLayout), "imageTransition");
                        android.support.v4.util.Pair<View, String> phon = android.support.v4.util.Pair.create(view.findViewById(R.id.phone), "phoneTransition");
                        android.support.v4.util.Pair<View, String> like = android.support.v4.util.Pair.create(view.findViewById(R.id.like), "likeTransition");

                        activityOptions = makeSceneTransitionAnimation(MainActivity.this, nameT, locT, icon, phon, like);
                    }

                    startActivity(intent, activityOptions.toBundle());

                }else{
                    startActivity(intent);
                }

            }
        });

       /* listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Menu menu = toolbar.getMenu();
                menu.clear();
                getMenuInflater().inflate(R.menu.contextual_menu_search, menu);
                String title = places.get(position).getName();
                toolbar.setTitle(title.substring(10, title.length()));
                contextual = true;
                number = places.get(position).getPhone();

                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibe != null) {
                    vibe.vibrate(200); // 50 is time in ms
                }

                return true;
            }
        });*/



       /* deleteLocationFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLocationFiltrered){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        MenuItem location= ToolbarMenu.findItem(R.id.ping_location);
                        location.getIcon().setTint(getResources().getColor(R.color.white));
                    }
                    isLocationFiltrered=false;
                    showLocationZoneFilter();
                }
            }
        });*/



        // fab animation for detailled view respond to scroll event
        NestedScrollView scrollView = (NestedScrollView)findViewById(R.id.scrollView);
        final FloatingActionButton fab = findViewById(R.id.fab);
        final  boolean comment_state=false;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(oldScrollY<scrollY)
                    fab.hide();
                else
                if(!comment_state)
                    fab.show();

                if(scrollY>(height/3)){

                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }else{
                    toolbar.setBackgroundResource(android.R.color.transparent);
                }

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        this.ToolbarMenu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_refresh) {
            LoadDatas(datas_type_to_fecth);
        }

        if (id == android.R.id.home) {
            close();
        }
        if (id == R.id.health_types) {
            View v =item.getActionView();
                reveal(v);
        }

        if (id == R.id.call) {
            MakeCall(number);
        }

        /*if (id == R.id.ping_location) {
            showLocationZoneFilter();
        }*/

        if (id == R.id.open_filter) {
            ShowOpenCenter(item);
        }

        if (id == R.id.more_cities) {
           Intent intent = new Intent(this, MoreCities.class);
           startActivity(intent);
           finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowOpenCenter(MenuItem item) {

        if(!open_filtered){
            final ArrayList<BookingObject> newname=new ArrayList<BookingObject>();

            adapter = new PlacesAdapter(this, newname, 1);
            listView.setAdapter(adapter);

            for(BookingObject phar:MainbookingList.get(RecycleHelthTypeAdapters.index)){

                if(phar.isOpen()){
                    newname.add(phar);
                }
            }

            bookingList = newname;
            adapter.notifyDataSetChanged();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                item.getIcon().setTint(getResources().getColor(R.color.notification));
            }else{
                item.setIcon(getResources().getDrawable(R.drawable.ic_radio_button_checked_green_24dp));
            }
              open_filtered = true;
        }else{


            bookingList = MainbookingList.get(RecycleHelthTypeAdapters.index);

            adapter = new PlacesAdapter(this, bookingList, 1);
            listView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                item.getIcon().setTint(getResources().getColor(R.color.white));
            }else{
                item.setIcon(getResources().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));
            }
            open_filtered = false;
        }


    }


    public void reveal(View v) {

        int cx = PlacesMenu.getWidth();
        int cy = PlacesMenu.getHeight();

        if (!isOpen) {
            float finalradius = Math.max(cx, cy) * 1.2f;
            Animator reveal = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                reveal = ViewAnimationUtils
                        .createCircularReveal(PlacesMenu, cx/2, cy/2, 0f, finalradius);

            }else{
                PlacesMenu.setVisibility(View.VISIBLE);
            }
            PlacesMenu.setVisibility(View.VISIBLE);


            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                   listView.animate().translationY(PlacesMenu.getHeight());
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            reveal.start();

            isOpen = true;
        } else {


            float finalradius = Math.max(cx, cy) * 1.2f;
            Animator reveal = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                reveal = ViewAnimationUtils
                        .createCircularReveal(PlacesMenu, cx/2, cy/2, finalradius, 0f);
            }else{
                PlacesMenu.setVisibility(View.INVISIBLE);
            }


            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    PlacesMenu.setVisibility(View.INVISIBLE);
                    listView.animate().translationY(0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            reveal.start();
            isOpen = false;
        }

    }



    public void MakeCall(String number) {

        if (number.equals("")) {

        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 117);
                MakeCall(number);
            }
            startActivity(callIntent);
        }
}


    private void LoadDatas(String type){

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Chargemment...");
        pDialog.show();


        JsonArrayRequest movieReq = new JsonArrayRequest(type,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        MainbookingList.clear();
                        bookingList.clear();
                        hidePDialog();
                        String responseCode = null;
                        try {
                            responseCode = response.getJSONObject(0).getString("response_code");
                            //get horizontal recycleView Adapter
                            JSONArray typeListArray = response.getJSONObject(0).getJSONArray("typeList");
                            data.clear();
                            for(int j=0; j<typeListArray.length(); j++){
                                JSONObject typeObject = typeListArray.getJSONObject(j);
                                HealthTypeObject healthTypeObject = new HealthTypeObject();
                                healthTypeObject.setId(typeObject.getInt("id"));
                                healthTypeObject.setName(typeObject.getString("name"));
                                healthTypeObject.setImageUrl(typeObject.getString("icon"));
                                healthTypeObject.setCount(typeObject.getInt("count"));
                                data.add(healthTypeObject);
                            }

                            horizontalAdapter = new RecycleHelthTypeAdapters(getApplicationContext(), data, listView, adapter, toolbar);
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
                            horizontal_recycler_view.setAdapter(horizontalAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(responseCode.equals("found")) {
                            // Parsing json
                            for (int i = 1; i < response.length(); i++) {

                                ArrayList<BookingObject> bookingList = new ArrayList<>();
                                try {
                                    // set Horizontal adapter
                                    JSONObject object = response.getJSONObject(i);
                                    //bookingObject.setTypeListID(object.getInt("id"));

                                    for (int n = 0; n < object.getJSONArray("item").length(); n++) {
                                        JSONObject item = object.getJSONArray("item").getJSONObject(n);
                                        BookingObject bookingObject = new BookingObject();

                                        bookingObject.setId(item.getString("id"));
                                        bookingObject.setName(item.getString("name"));
                                        bookingObject.setOpen(item.getBoolean("open"));
                                        bookingObject.setI_like(item.getBoolean("i_like"));
                                        bookingObject.setBiometry(item.getBoolean("biometry"));

                                        try{
                                            bookingObject.setPictures(item.getJSONArray("pictures"));
                                        }catch (JSONException e){

                                        }
                                        bookingObject.setLocation(item.getJSONObject("location"));
                                        bookingObject.setPlaces(item.getInt("places"));
                                        bookingObject.setLikes(item.getInt("likes"));
                                        bookingObject.setComments_count(item.getInt("comments_count"));
                                        bookingObject.setContact(item.getJSONObject("contact").toString());
                                        bookingObject.setPricing(item.getString("pricing"));
                                        bookingObject.setHours(item.getJSONObject("hours"));
                                        bookingObject.setSpecialities(item.getJSONArray("specialities"));

                                        //bookingObject.setMore_infos(item.getJSONObject("other").toString());
                                        // bookingObject.setComments(item.getJSONArray("comments"));

                                        if (object.getString("slug").equals("personnel")) {
                                            bookingObject.setGrade(item.getJSONObject("grade"));
                                            bookingObject.setWork_place(item.getJSONObject("contact").getJSONObject("work_place"));
                                        }
                                        bookingList.add(bookingObject);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                MainbookingList.add(bookingList);
                            }
                                adapter = new PlacesAdapter(getApplicationContext(), MainbookingList.get(0), 1);
                                listView.setAdapter(adapter);
                                bookingList = MainbookingList.get(0);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(container.getContext(),"Check your Network settings",Toast.LENGTH_LONG).show();
                hidePDialog();
                error.printStackTrace();

            }
        }){

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    //articles.clear();

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
                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    try {
                        return Response.success(new JSONArray(jsonString), cacheEntry);
                    } catch (JSONException e) {
                        return Response.error(new ParseError(e));
                    }

                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONArray response) {
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
        AppController.getInstance().addToRequestQueue(movieReq);

        if(open_filtered){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.getMenu().findItem(R.id.open_filter).getIcon().setTint(getResources().getColor(R.color.white));
            }else{
                toolbar.getMenu().findItem(R.id.open_filter).setIcon(getResources().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));
            }
        }

    }


    @Override
    public boolean onQueryTextSubmit(String newText) {
        boolean returnState =false;
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        final ArrayList<BookingObject> newname=new ArrayList<BookingObject>();

        adapter = new PlacesAdapter(this, newname, 1);
        listView.setAdapter(adapter);

            for(BookingObject phar:MainbookingList.get(RecycleHelthTypeAdapters.index)){
                String kwat = phar.getQuater().toLowerCase();

                if(kwat.contains(newText)){
                    //bookingList.remove(phar);
                    newname.add(phar);
                }
            }
        bookingList = newname;
        adapter.notifyDataSetChanged();

        return true;
    }



    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // Do work using string*
        }
    }





    private void showLocationZoneFilter(){
        if(!isOpenFilterLayout){
            FilterLocationLayout.setVisibility(View.VISIBLE);
            isOpenFilterLayout=true;
        }else{
            FilterLocationLayout.setVisibility(View.INVISIBLE);
            isOpenFilterLayout=false;
        }

        /*if(isLocationFiltrered){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ToolbarMenu.findItem(R.id.ping_location).setIcon(getResources().getDrawable(R.drawable.ic_my_location_black_24dp));
                ToolbarMenu.findItem(R.id.ping_location).getIcon().setTint(getResources().getColor(R.color.notification));
            }
        }*/

    }

    private void LaunchILearnActivity(){
        Intent ilearn_Activity = new Intent(getApplicationContext(), ILearn.class);
        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(ilearn_Activity);
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


    @Override
    public void onBackPressed() {
       close();
    }

    public void close(){
        if(!contextual)
            LaunchILearnActivity();
        else{
            Menu menu=toolbar.getMenu();
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_main, menu);
            toolbar.setTitle("Pharmacies");
            contextual=false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        menuItem = menu.getItem(1);
        menuItem.setChecked(true);
//        adapter.notifyDataSetChanged();
    }

}



