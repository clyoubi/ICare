package com.smookcreative.icare.ISearch;

import android.Manifest;
import android.animation.Animator;
import android.app.SharedElementCallback;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.smookcreative.icare.Adapters.CommentsAdapter;
import com.smookcreative.icare.Adapters.CommentsObject;
import com.smookcreative.icare.Adapters.ParamAdapter;
import com.smookcreative.icare.Adapters.RecycleHelthTypeAdapters;
import com.smookcreative.icare.Adapters.UserInfos;
import com.smookcreative.icare.Adapters.paramsObject;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.DatabasesManagers.Singleton;
import com.smookcreative.icare.ILearn.ArticleDetail;
import com.smookcreative.icare.ILearn.Comments;
import com.smookcreative.icare.MainActivity;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Settings;
import com.smookcreative.icare.Tips.CircularNetworkImageView;
import com.smookcreative.icare.Tips.Fonts;
import com.smookcreative.icare.Tips.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import ss.com.bannerslider.Slider;


public class ItemDetail extends AppCompatActivity{

    private TextView name, location, places, recommandations, comments, website, hours, phoneNumber, spetialities, more_infos;
    Typeface ralewayRegular, ralewaybold, ralewaySemibold, getRalewaybold;
    RelativeLayout header, detailView, line2, filter, blured_layout, comment_layout;
    LinearLayout linearLayout, headerContainer;
    Slider bannerSlider;
    CircleImageView circleImageView;
    NonScrollListView nonScrollListView, contactScrollView;
    NestedScrollView nestedScrollView;
    ParamAdapter paramAdapter;
    private ArrayList<paramsObject> paramsObjects = new ArrayList<>();
    FloatingActionButton commentFAB, likeFAB, fab;
    Menu menu;

    ArrayList<CommentsObject> commentList = new ArrayList<>();
    CommentsAdapter commentsAdapter;
    TextView firstChar;
    EditText comment_textView;
    ProgressBar progressBar;
    String number;
    int position;
    boolean bannerIsShown, comment_state;
    BookingObject bookingItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center_item_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        InitView();



        final BookingObject item = getIntent().getParcelableExtra("item");
        bookingItem = item;


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int toolbar_margin=0;

        int resourceId= getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0)
            toolbar_margin=getResources().getDimensionPixelSize(resourceId);

        toolbar.setPadding(0,toolbar_margin,0,0);

            position = getIntent().getIntExtra("position", 0);
            int i = getIntent().getIntExtra("color", 0);
                filter.setBackground(getResources().getDrawable(randomColor(i)));
                    filter.getBackground().setAlpha(166);

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent showWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+item.getWebsite()));
                    startActivity(showWebsite);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Erreur aucun navigateur installer sur votre appareil", Toast.LENGTH_LONG).show();
                }

            }
        });


            name.setText(Html.fromHtml(item.getName()));
            location.setText(Html.fromHtml(String.format("%s - %s", item.getQuater(), item.getLocation_precision())));
            spetialities.setText(Html.fromHtml(item.getSpecialities()));
            places.setText(item.getPlaces());
            recommandations.setText(item.getLikes());
            comments.setText(item.getComments_count());
            //hours.setText(Html.fromHtml(item.getHours_resume()));
            hours.setText(item.getHours_resume());
            website.setText(item.getWebsite());
            more_infos.setText(Html.fromHtml(String.format("%s <br> %s", item.getEmail(), item.getMore_infos())));


            Character x = item.getName().charAt(item.getName().indexOf(" ")+1);
            firstChar.setText(x.toString());

        JSONArray telephone = null;
        StringBuilder call = new StringBuilder();
        try {
            telephone = new JSONArray(item.getPhone());
            for(int j=0; j<telephone.length(); j++){
                call.append(telephone.get(j)).append(" / ");
            }
            number = telephone.get(0).toString();
            phoneNumber.setText(call.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


       /* paramsObjects.add(new paramsObject(item.getWebsite(), getResources().getDrawable(R.drawable.earth), "my_profile"));
        paramsObjects.add(new paramsObject(item.getHours_resume(), getResources().getDrawable(R.drawable.clock), "my_profile"));
        paramsObjects.add(new paramsObject(call.toString(), getResources().getDrawable(R.drawable.phone_gray), "my_profile"));

        paramAdapter = new ParamAdapter(this, paramsObjects, true);
            contactScrollView.setAdapter(paramAdapter);
*/

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


                    if(oldScrollY<scrollY)
                        commentFAB.hide();
                    else
                    if(!comment_state)
                        commentFAB.show();

            }
        });


        JSONArray pictures=null;
        boolean empty=false;
        try {
            pictures = new JSONArray(item.getPictures());
            if(pictures.getString(0).equals("")){
                empty = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int couleur = getIntent().getIntExtra("color", 0);

        assert pictures != null;
        if(pictures.length()==1 && empty){

            firstChar.setText(getFirstChar(item.getName()).toString());
            PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService(this);
            picassoImageLoadingService.loadImage(randomColor(couleur), circleImageView);

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setEnterSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                        firstChar.setVisibility(View.VISIBLE);
                    }
                });

            }else{
                firstChar.setVisibility(View.VISIBLE);
            }*/


        }else{
            firstChar.setVisibility(View.INVISIBLE);

                try {
                    Slider.init(new PicassoImageLoadingService(this));
                    bannerSlider.setAdapter(new SlideBannerAdapter(this, pictures));
                    PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService(this);
                    picassoImageLoadingService.loadImage(pictures.getString(0), circleImageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                    firstChar.setVisibility(View.VISIBLE);
                }


        }


        final boolean finalEmpty = empty;
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!finalEmpty)
                    RevealSlideBanner(v);
            }
        });


        if(item.isI_like()){
            likeFAB.setImageDrawable(getResources().getDrawable(R.drawable.heart));
        }

        likeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.isI_like()){
                    likeFAB.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                    item.setI_like(true);
                    item.setLikes(Integer.parseInt(item.getLikes())+1);
                    MainActivity.bookingList.get(position).setI_like(true);
                    MainActivity.bookingList.get(position).setLikes(Integer.parseInt(MainActivity.bookingList.get(position).getLikes())+1);
                    MainActivity.adapter.notifyDataSetChanged();

                    UpdateLikes(item.getId(), true);
                }else{
                    likeFAB.setImageDrawable(getResources().getDrawable(R.drawable.emptyheart));
                    item.setI_like(false);
                    item.setLikes(Integer.parseInt(item.getLikes())-1);

                    MainActivity.bookingList.get(position).setI_like(false);
                    MainActivity.bookingList.get(position).setLikes(Integer.parseInt(MainActivity.bookingList.get(position).getLikes())-1);
                    MainActivity.adapter.notifyDataSetChanged();

                    UpdateLikes(item.getId(), false);
                }

            }
        });

        TextView titl2 =findViewById(R.id.title2);
            titl2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Comments.class);
                    intent.putExtra("booking", true);
                    intent.putExtra("id", bookingItem.getId());
                    startActivity(intent);
                }
            });

        commentFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentFAB.hide();
                comment_layout.setVisibility(View.VISIBLE);
                fab.show();
                blured_layout.setVisibility(View.VISIBLE);
                comment_state=true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(comment_textView.getText().toString().matches(""))
                    Toast.makeText(v.getContext(),"Veuillez écrire quelque chose", Toast.LENGTH_LONG).show();
                else{
                    fab.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    writeComment(comment_textView.getText().toString());
                }
            }
        });


        blured_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseCommentSection();
            }
        });

    }

    private void CloseCommentSection(){
        fab.hide();
        comment_layout.setVisibility(View.INVISIBLE);
        blured_layout.setVisibility(View.INVISIBLE);
        comment_state=false;
        commentFAB.show();

    }


    void InitView(){

        ralewayRegular=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayRegular));
        ralewaybold=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayBold));
        ralewaySemibold=Typeface.createFromAsset(getAssets(),getString(R.string.ralewaySemiBold));

        //circularNetworkImageView = findViewById(R.id.image);
        circleImageView = findViewById(R.id.image);
        header = findViewById(R.id.header);
        linearLayout = findViewById(R.id.linearLayout);
        headerContainer = findViewById(R.id.headerContainer);
        detailView = findViewById(R.id.detail);
        line2 = findViewById(R.id.line2);
        filter = findViewById(R.id.filter);
        bannerSlider = findViewById(R.id.slider);

        //textView
        firstChar = findViewById(R.id.caracter);

        //String array = "[\"http://www.smookcreative.com/I_Care/Booking_Pictures/56/index.png\",\"http://www.smookcreative.com/I_Care/Booking_Pictures/56/photo_1.jpg\",\"http://www.smookcreative.com/I_Care/Booking_Pictures/56/photo_2.jpg\",\"http://www.smookcreative.com/I_Care/Booking_Pictures/56/photo_3.jpg\", \"http://www.smookcreative.com/I_Care/Booking_Pictures/56/photo_4.jpg\"]";

        name = findViewById(R.id.name);
        location = findViewById(R.id.location);

        places = findViewById(R.id.places);
        recommandations = findViewById(R.id.like);
        comments = findViewById(R.id.commentsText);
        website= findViewById(R.id.website);
        hours = findViewById(R.id.openHour);
        phoneNumber = findViewById(R.id.phone);
        spetialities = findViewById(R.id.specialities);
        more_infos = findViewById(R.id.more);

        nonScrollListView = findViewById(R.id.commentList);
        commentFAB = findViewById(R.id.fab);
        likeFAB = findViewById(R.id.likefab);
        fab = findViewById(R.id.send);
        contactScrollView = findViewById(R.id.contactLiview);
        nestedScrollView = findViewById(R.id.scrollView);

        blured_layout = findViewById(R.id.blured_layout);
        comment_layout = findViewById(R.id.comment_layout);
        comment_textView = findViewById(R.id.comment_text);
        progressBar = findViewById(R.id.progress_bar);

        GetComments();
        /*commentsAdapter = new CommentsAdapter(this, commentList);
        nonScrollListView.setAdapter(commentsAdapter);*/

        Fonts.setAllTextView(header, ralewayRegular);
        Fonts.setAllTextView(detailView, ralewayRegular);
        Fonts.setAllTextView(line2, ralewayRegular);
        name.setTypeface(ralewaybold);
    }


    public Character getFirstChar(String name){
        Character x = name.charAt(name.indexOf(" ")+1);
        return x;
    }

    public int randomColor(int x){

        Random random = new Random();
        Drawable color=getResources().getDrawable(R.drawable.gray_back);
        int res;

        switch (x){
            case 0:
                color=getResources().getDrawable(R.drawable.pink_back);
                res = R.drawable.pink_back;
                break;
            case 1:
                color=getResources().getDrawable(R.drawable.blue_back);
                res = R.drawable.blue_back;
                break;
            case 2:
                color=getResources().getDrawable(R.drawable.purple_back);
                res = R.drawable.purple_back;
                break;
            case 3:
                color=getResources().getDrawable(R.drawable.orange_back);
                res = R.drawable.orange_back;
                break;
            default:
                res = R.drawable.gray_back;
        }


        return res;
    }

    private void RevealSlideBanner(View v){

        final int visibility;
        int cx = bannerSlider.getWidth();
        int cy = bannerSlider.getHeight();

        int[] values = new int[2];

        circleImageView.getLocationOnScreen(values);
            int X = (int) v.getX();
            int Y = (int) v.getY();

            X = cx/5;
            Y = cy/2;

        float finalradius, startraduis;

        if(!bannerIsShown){
            finalradius = Math.max(cx, cy) * 1.2f;
            startraduis=0f;
            visibility = 4;
        }else{
            startraduis = Math.max(cx, cy) * 1.2f;
            finalradius=0f;
            visibility = 0;
        }

        Animator reveal;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            reveal = ViewAnimationUtils
                    .createCircularReveal(bannerSlider, X, Y,startraduis, finalradius);

            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    DisplayView(visibility);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            reveal.start();

        }else{
            DisplayView(visibility);
        }


    }

    private void DisplayView(int visibility){
        name.setVisibility(visibility);
        location.setVisibility(visibility);
        line2.setVisibility(visibility);
        filter.setVisibility(visibility);
        circleImageView.setVisibility(visibility);
        bannerIsShown = !bannerIsShown;
    }


    public void MakeCall(String number) {

        if (number.equals("")) {

        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 117);
                return;
            }
            startActivity(callIntent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contextual_menu_search, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.call:
                MakeCall(number);
                break;
            /*case R.id.map:
                OpenMap();
                break;*/

            case android.R.id.home:
                if(bannerIsShown)
                    RevealSlideBanner(circleImageView);
                else
                    finish();
                break;

        }

        return true;
    }


    void OpenMap(){
        try {
            //BookingObject book = getIntent().getParcelableExtra("item");
            JSONArray jsonArray = new JSONArray(bookingItem.getCoordinates());

            if((jsonArray.get(0) == null) && (jsonArray.get(1) == null)){
                Snackbar.make(linearLayout, "Désolé pas de coordonnées disponible pour le moment", 2000).show();
            }else{
                startActivity(new Intent(this, Maps.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    void UpdateLikes(String id, boolean like){

        MyPreferences myPreferences = new MyPreferences(this);
        UserInfos userInfos = myPreferences.readUserInfos();
        String url = RemoteServer.ISearch_Update_Like_Booking_Script + "?token="+userInfos.getUser_hash()+"&&id="+userInfos.getId()+"&&book="+id+"&&like="+like;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ItemDetail.this, "Désolé une erreur s'est produite", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    
    void GetComments(){

        String url = RemoteServer.ISearch_Booking_Comments_Script;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<CommentsObject> arrayList = new ArrayList<>();
                for(int i=0; i<3; i++){
                    try {
                        CommentsObject commentsObject = new CommentsObject();

                        commentsObject.setId(response.getJSONObject(i).getString("comment_ID"));
                        commentsObject.setComment(response.getJSONObject(i).getString("comment_content"));
                        commentsObject.setName(response.getJSONObject(i).getString("comment_author"));
                        commentsObject.setPost_date(response.getJSONObject(i).getString("comment_date"));
                        commentsObject.setAuthor_id(response.getJSONObject(i).getString("user_id"));
                        commentsObject.setAuthor_email(response.getJSONObject(i).getString("comment_author_email"));

                        arrayList.add(commentsObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                CommentsAdapter commentsAdapter = new CommentsAdapter(getApplicationContext(), arrayList);
                    nonScrollListView.setAdapter(commentsAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }


    void writeComment(final String comment){

        MyPreferences myPreferences = new MyPreferences(this);
        final UserInfos userInfos = myPreferences.readUserInfos();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RemoteServer.ISearch_Booking_Insert_Comments_Script, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                comment_layout.setVisibility(View.INVISIBLE);
                blured_layout.setVisibility(View.INVISIBLE);
                commentFAB.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                comment_layout.setVisibility(View.INVISIBLE);
                blured_layout.setVisibility(View.INVISIBLE);
                commentFAB.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("comment",comment);
                params.put("userid",userInfos.getId());
                params.put("postid",bookingItem.getId());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(comment_state){
            CloseCommentSection();
        }else{
            if(bannerIsShown)
                RevealSlideBanner(circleImageView);
            else
                supportFinishAfterTransition();
        }


    }


}
