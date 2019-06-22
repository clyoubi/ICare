package com.smookcreative.icare.ILearn;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.smookcreative.icare.Adapters.ArticleAdapter;
import com.smookcreative.icare.Adapters.ArticleObject;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.DatabasesManagers.Singleton;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Settings;
import com.smookcreative.icare.Tips.Fonts;
import com.smookcreative.icare.Tips.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ArticleDetail extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private NetworkImageView imageBanner;
    private TextView credit, title, infos, comments_count, plus;
    private HtmlTextView article;
    //private TextView article;
    private String link;
    private FloatingActionButton fab, send;
    private NestedScrollView scrollView;
    private RelativeLayout rootscrollview, audioPlayerLayout;
    private int oldY=0;
    private int oldDiff=0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Fonts yo =new Fonts();
    private RelativeLayout comment_layout, blured_layout, ratemainLayout, creatingAudioLayout;
    private boolean comment_state=false, isPlaying;
    private EditText comment_textView;
    private int colorAccent;
    private NonScrollListView nonScrollListView;
    private ArrayList<ArticleObject>  articles=new ArrayList<>();
    private ArticleAdapter adapter;
    boolean loaded=false;
    private Intent ilearn_activity;
    private TextToSpeech textToSpeech;
    private ArticleObject post;
    public static Toolbar toolbar;
    public static boolean saveMode = false;
    int POSITION, DELAYMILLIS=1000;
    private String userID, TABSTYPE;
    private ProgressBar progressBar, convertionProgress;
    private Typeface ralewayRegular,ralewaybold;
    private SeekBar seekBar;
    private int current = 0;
    private boolean   running = true, isConverting = true;
    private int duration = 0, SPEECHREQUESTCODE;
    private MediaPlayer mp;
    private ImageView control;
    private TextView rateValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ilearn_article_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ilearn_activity= new Intent(this,ILearn.class);
        MyPreferences myPreferences =new MyPreferences(this);
            userID = myPreferences.readUserID();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int toolbar_margin=0;

        switch (metrics.densityDpi){
            case DisplayMetrics.DENSITY_HIGH:
                toolbar_margin=48;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                toolbar_margin=32;
                break;
            case DisplayMetrics.DENSITY_LOW:
                toolbar_margin=24;
                break;
            default:
                toolbar_margin=32;
        }

        int resourceId= getResources().getIdentifier("status_bar_height","dimen","android");
            if(resourceId>0)
                toolbar_margin=getResources().getDimensionPixelSize(resourceId);



       /* ViewGroup.MarginLayoutParams param =(ViewGroup.MarginLayoutParams)toolbar.getLayoutParams();
        param.topMargin=toolbar_margin;
        toolbar.setLayoutParams(param);*/

        toolbar.setPadding(0,toolbar_margin,0,0);
        progressBar = findViewById(R.id.progress_bar);
        convertionProgress = findViewById(R.id.convertionProgress);


        //Set Typefaces
         ralewayRegular=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayRegular));
         ralewaybold=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayBold));
        ViewGroup group =  getWindow().getDecorView().findViewById(R.id.container);
        yo.setAllTextView(group,ralewayRegular);

        fab = findViewById(R.id.commentfab);
        send = findViewById(R.id.send);
        send.hide();
        scrollView = findViewById(R.id.container);
        rootscrollview = findViewById(R.id.rootscrollview);
        nonScrollListView=findViewById(R.id.nonscroll_view);
        audioPlayerLayout=findViewById(R.id.audioPlayerLayout);
        creatingAudioLayout = findViewById(R.id.creatingAudioLayout);


        imageBanner = findViewById(R.id.image_banner);
        credit = findViewById(R.id.credit_photo);
        infos =findViewById(R.id.infos);
        title = findViewById(R.id.article_title);
        article = findViewById(R.id.html_text);
        comments_count = findViewById(R.id.comments_number);
        comment_textView = findViewById(R.id.comment_text);
        rateValue = findViewById(R.id.rateValue);

        comment_layout = findViewById(R.id.comment_layout);
        blured_layout = findViewById(R.id.blured_layout);
        ratemainLayout = findViewById(R.id.rateLayout);
        seekBar = findViewById(R.id.audioseek);
        mp=new MediaPlayer();


        title.setTypeface(ralewaybold);

        if(saveMode)
            colorAccent=getResources().getColor(R.color.gray);
        else
            colorAccent=getResources().getColor(R.color.colorAccent);

        // Offline mode
        RelativeLayout rateLayout = findViewById(R.id.rateLayout);
        plus = findViewById(R.id.plus);
            if(saveMode){
                fab.setVisibility(View.INVISIBLE);
                comments_count.setVisibility(View.INVISIBLE);
                rateLayout.setVisibility(View.INVISIBLE);
                plus.setVisibility(View.INVISIBLE);
            }


         Intent i = getIntent();
         post = i.getParcelableExtra("article");
         POSITION = i.getIntExtra("position",23);
         TABSTYPE = i.getStringExtra("type");

            title.setText(post.getTitle());

            article.setHtml(post.getContent(),new HtmlHttpImageGetter(article));

        //article.setText(Html.fromHtml(post.getContent()));
        article.setMovementMethod(LinkMovementMethod.getInstance());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //article.setText(Html.fromHtml(post.getContent(), Html.FROM_HTML_MODE_COMPACT));
        }

            infos.setText(String.format("%s - %s", post.getAuthor(), post.getPostOn()));
            link = post.getLink();
            credit.setText(post.getCredit_photo());
            comments_count.setText(String.format("%s Commentaire(s)", post.getComment_count()));


        rateValue.setText(post.getRate());

        if(!saveMode){
            switch (post.getCategories()){
                case "adverts":
                    colorAccent=getResources().getColor(R.color.yellowLight);
                    break;

                case "good_news":
                    colorAccent=getResources().getColor(R.color.greenLight);
                    break;

                case "urgents":
                    colorAccent=getResources().getColor(R.color.red);
                    break;
            }
        }



        credit.setBackgroundColor(colorAccent);
        title.setBackgroundColor(colorAccent);

        imageLoader = AppController.getInstance().getImageLoader();
        imageBanner.setImageUrl(post.getBanner(), imageLoader);



        adapter = new ArticleAdapter(this, articles,1);
        nonScrollListView.setAdapter(adapter);


        nonScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<articles.size()){
                    Intent intent = new Intent(parent.getContext(),ArticleDetail.class);
                    intent.putExtra("article", articles.get(position));
                    intent.putExtra("type", TABSTYPE);
                    startActivity(intent);
                    finish();
                }else{
                    LoadDatas("");
                }
            }
        });


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        if(!saveMode){
            if(oldScrollY<scrollY)
                fab.hide();
            else
            if(!comment_state)
                fab.show();

            if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() ) && !loaded) {
                if(TABSTYPE.equals("article"))
                    LoadDatas("?args=posts_per_page,2,category__not_in,28,category,"+post.getCategories()+",exclude,"+post.getId());
                else
                    LoadDatas("?n=3");

                loaded=true;
            }
        }

        if(scrollY>(height/3)){
            toolbar.setBackgroundColor(colorAccent);
        }else{
            toolbar.setBackgroundResource(android.R.color.transparent);
        }

    }
});

        comments_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post.getComment_count().equals("x")){

                }else{
                    Intent intent = new Intent(getApplicationContext(),Comments.class);
                    intent.putExtra("id", post.getId());
                    startActivity(intent);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    fab.hide();
                    comment_layout.setVisibility(View.VISIBLE);
                    send.show();
                    blured_layout.setVisibility(View.VISIBLE);
                    comment_state=true;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(comment_textView.getText().toString().matches(""))
                    Toast.makeText(v.getContext(),"Veuillez écrire quelque chose", Toast.LENGTH_LONG).show();
                else{
                        send.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        writeComment(comment_textView.getText().toString(),post.getId());
                }


            }
        });

        blured_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseCommentSection();
            }
        });

        ratemainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateArticle();
            }
        });


        infos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAuthorInfos();
            }
        });


        control = findViewById(R.id.control);
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    control.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    mp.pause();
                }
                else{
                    control.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    mp.start();
                    seekBar.postDelayed(onEverySecond, 1000);
                }

            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBa, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int min = seekBar.getProgress();
                mp.seekTo(min);
            }
        });


    }




    @Override
    public void onBackPressed() {
      close();
    }


    private void close(){
        if(comment_state)
            CloseCommentSection();
        else{

            if(isPlaying){
                textToSpeech.shutdown();
            }else{
                running =false;
                mp.stop();
                mp.release();
            }

            if(saveMode){
                finish();
            }else{
                startActivity(ilearn_activity);
                finish();
            }

            //textToSpeech.shutdown();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(!saveMode){
            inflater.inflate(R.menu.article_detail_menu, menu);
        }else{
           inflater.inflate(R.menu.save_articles, menu);
            menu.add(0,R.id.listen,1,"Ecouter").setIcon(R.drawable.ic_record_voice_over_black_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==android.R.id.home) {
           close();
        }
        if (id==R.id.listen)
            performSpeech();

        if(id==R.id.share){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT,link);

            startActivity(Intent.createChooser(i,"Partager via"));
        }

        if(id==R.id.download){
            if(SaveArticle())
                Toast.makeText(this, "Sauvegardé", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Une erreur s'est produite réessayer", Toast.LENGTH_SHORT).show();
        }

        if(id==R.id.delete){
            DeleteSaved();
        }

        return true;
    }

    private boolean SaveArticle() {
        MyPreferences myPreferences = new MyPreferences(this);
           return myPreferences.SaveArticles(post);
    }


    private void DeleteSaved(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Supprimer");
        alertDialogBuilder.setMessage("Voulez-vous vraiment supprimer cet article? Cette action effacera cet article en mode hors connexion");

        alertDialogBuilder.setPositiveButton("Oui",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        MyPreferences myPreferences = new MyPreferences(ArticleDetail.this);
                        myPreferences.DeleteArticle(POSITION);
                        startActivity(new Intent(ArticleDetail.this,SavedArticles.class));
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("Non",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void CloseCommentSection(){
        send.hide();
        comment_layout.setVisibility(View.INVISIBLE);
        blured_layout.setVisibility(View.INVISIBLE);
        comment_state=false;
        fab.show();
    }


    public void LoadDatas(final String args){

        String URLRequest;
        URLRequest = RemoteServer.ILearn_News_Script+args;

        if(TABSTYPE.equals("news"))
            URLRequest = RemoteServer.ILearn_Alerts_Script+args;



        StringRequest movieReq = new StringRequest(Request.Method.GET, URLRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                                //Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                                plus.setVisibility(View.INVISIBLE);
                            }
                            //Toast.makeText(getContext(), "succes!!!", Toast.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(), "Try error", Toast.LENGTH_LONG).show();
                        }

                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(container.getContext(),"Check your Network settings",Toast.LENGTH_LONG).show();
                error.printStackTrace();
                plus.setVisibility(View.INVISIBLE);
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(movieReq);



}


    private boolean RateArticle(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Noter cet article");
        alertDialogBuilder.setMessage("Comment avez-vous trouvé cet article?");
        final View alertView = LayoutInflater.from(this).inflate(R.layout.settings_dialog_rating, null);
        alertDialogBuilder.setView(alertView);

        final RatingBar ratingBar = (RatingBar)alertView.findViewById(R.id.ratingBar);
        final TextView descrip =alertView.findViewById(R.id.rateDescription);

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if(rating<=1){
                        descrip.setText("j'ai détesté");
                    }

                    if(rating>1 && rating<=2){
                        descrip.setText("je n'ai pas aimé");
                    }

                    if(rating>2 && rating<=3){
                        descrip.setText("c'etait pas mal");
                    }

                    if(rating>3 && rating<=4){
                        descrip.setText("Ca m'a beaucoup plus");
                    }

                    if(rating>4 && rating<=5){
                        descrip.setText("j'ai adoré");
                    }

                }
            });

        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        final MyPreferences myPreferences =new MyPreferences(getApplicationContext());
                        String URL = RemoteServer.ILearn_Rate_Article_Script;
                            StringRequest raterequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(ArticleDetail.this, response, Toast.LENGTH_LONG).show();
                                    Log.d("volley response", response);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(ArticleDetail.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new Hashtable<String, String>();
                                    params.put("id",post.getId());
                                    params.put("email",myPreferences.readUserInfos().getEmail());
                                    params.put("rate",String.valueOf(ratingBar.getRating()));
                                    params.put("facebook", String.valueOf(myPreferences.readUserInfos().isFacebook_login()));
                                    return params;
                                }
                            };
                            AppController.getInstance().addToRequestQueue(raterequest);
                    }
                });

        alertDialogBuilder.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ratingBar.setRating(0);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return true;
    }


    public void performSpeech(){
        //SPEECHREQUESTCODE = (int) Math.random();
        SPEECHREQUESTCODE = 101;

            mp.release();
            Intent checkAPI = new Intent();
            checkAPI.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkAPI,SPEECHREQUESTCODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==SPEECHREQUESTCODE && resultCode==TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
            textToSpeech=new TextToSpeech(this, this);
        }else{
            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installIntent);
        }
    }

    @Override
    public void onInit(int status) {


        final String text = article.getText().toString();
        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);
        String tempFilename = post.getId()+"_"+post.getTitle().substring(0,20)+".wav";

        File file;
        final String url = "Audio Articles";
       file =  new File(this.getFilesDir(),"Music/");

       if (!file.exists())
           file.mkdirs();

        String tempDestFile = file.getAbsolutePath()+File.separator+tempFilename;

        if(status==TextToSpeech.SUCCESS){
            int languageStatus = textToSpeech.setLanguage(Locale.FRENCH);

            if(languageStatus==TextToSpeech.LANG_MISSING_DATA || languageStatus==TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "langue non supportée", Toast.LENGTH_LONG).show();
            }else {

                MyPreferences myPreferences = new MyPreferences(this);
                if (!myPreferences.isAudioConversion()) {
                    int k;
                    k = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, hashMap);
                        if(k == TextToSpeech.SUCCESS){
                            Toast.makeText(this, "Début de la lecture audio", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, "Désolé une erreur s'est produite", Toast.LENGTH_SHORT).show();
                        }
                    isPlaying = true;
                }

                else{

                    File audio = new File(this.getFilesDir(), "Music/" + tempFilename);

                    if (!audio.exists()) {

                        creatingAudioLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(this, tempDestFile, Toast.LENGTH_SHORT).show();
                        int i = textToSpeech.synthesizeToFile(text, hashMap, tempDestFile);

                        if (i == TextToSpeech.ERROR) {
                            Toast.makeText(this, "Erreur lors de la conversion du fichier audio", Toast.LENGTH_LONG).show();
                        }

                        final String finalTempDestFile = tempDestFile;
                        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onStart(String utteranceId) {
                            }

                            @Override
                            public void onDone(String utteranceId) {
                                PlayAudio(text, hashMap);
                                isConverting = false;
                            }

                            @Override
                            public void onError(String utteranceId) {
                            }
                        });

                        } else {
                            PlayAudio(text, hashMap);
                        }

                }
            }

        }else{
            Toast.makeText(this, "Erreur lors de l'initialisation de la lecture", Toast.LENGTH_LONG).show();
        }

    }


    private void PlayAudio(final String text, final HashMap<String, String> hashMap){

        String tempFilename = post.getId()+"_"+post.getTitle().substring(0,20)+".wav";
        File file =  new File(this.getFilesDir(),"Music/"+tempFilename);

        String tempDestFile = file.getAbsolutePath();

        mp.release();
        creatingAudioLayout.setVisibility(View.INVISIBLE);
        audioPlayerLayout.setVisibility(View.VISIBLE);

        mp = new MediaPlayer();
        try {
            mp.setDataSource(tempDestFile);
            mp.prepare();
            duration = mp.getDuration();
            seekBar.setMax(duration);
            mp.start();
            seekBar.postDelayed(onEverySecond, DELAYMILLIS);

            } catch (Exception e) {
                e.printStackTrace();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("Erreur de creation du fichier");
            alertDialogBuilder.setMessage("Voulez-vous essayer de lire cet article sans controle audio?");
            alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, hashMap);
                    isPlaying = true;
                }
            });

            alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

                audioPlayerLayout.setVisibility(View.INVISIBLE);
            }

        //textToSpeech.shutdown();
    }



    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run(){
            if(running){
                if(seekBar != null) {
                    seekBar.setProgress(mp.getCurrentPosition());
                    if(seekBar.getProgress()==mp.getDuration()){
                        control.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    }
                }
                if(mp.isPlaying()) {
                    seekBar.postDelayed(onEverySecond, DELAYMILLIS);
                    updateTime();
                }
            }
        }
    };



    private void updateTime(){
        do {
            current = mp.getCurrentPosition();


            /*System.out.println("duration - " + duration + " current- "
                    + current);
            int dSeconds = (int) (duration / 1000) % 60 ;
            int dMinutes = (int) ((duration / (1000*60)) % 60);
            int dHours   = (int) ((duration / (1000*60*60)) % 24);

            int cSeconds = (int) (current / 1000) % 60 ;
            int cMinutes = (int) ((current / (1000*60)) % 60);
            int cHours   = (int) ((current / (1000*60*60)) % 24);

            if(dHours == 0){
                mMediaTime.setText(String.format("%02d:%02d / %02d:%02d", cMinutes, cSeconds, dMinutes, dSeconds));
            }else{
                mMediaTime.setText(String.format("%02d:%02d:%02d / %02d:%02d:%02d", cHours, cMinutes, cSeconds, dHours, dMinutes, dSeconds));
            }*/

            try{
                Log.d("Value: ", String.valueOf((int) (current * 100 / duration)));
                if(seekBar.getProgress() >= 100){
                    break;
                }
            }catch (Exception e) {}
        }while (seekBar.getProgress() <= 100);
    }


  /*  @Override
    public void onPrepared(MediaPlayer mp) {
// TODO Auto-generated method stub
        duration = mp.getDuration();
        seekBar.setMax(duration);
        seekBar.postDelayed(onEverySecond, 1000);
    }

*/




    public void writeComment(final String comment, final String postId){

        String request = RemoteServer.ILearn_New_Comment_Script;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, request, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Posté", Toast.LENGTH_LONG).show();
                send.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                send.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("comment",comment);
                params.put("userid",userID);
                params.put("postid",postId);
                return params;
            }
        };

        Singleton.getInstance(ArticleDetail.this).addToRequestqueue(stringRequest);
    }


    private boolean ShowAuthorInfos(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.article_author_dialog_box, null);
        alertDialogBuilder.setView(view);
            TextView author_name = view.findViewById(R.id.author_name);
            TextView author_description = view.findViewById(R.id.author_description);
            TextView rate = view.findViewById(R.id.rate);
            ImageView mail = view.findViewById(R.id.mail);
            ImageView phone = view.findViewById(R.id.call);
            final ImageView picture = view.findViewById(R.id.circleColor);

                author_name.setText(post.getAuthor());
                author_name.setTypeface(ralewaybold);
                author_description.setTypeface(ralewayRegular);

                phone.setVisibility(View.INVISIBLE);

                    mail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_SEND);

                                intent.setType("vnd.android.cursor.dir/email");
                                String to[] = {post.getAuthor_email()};
                                intent.putExtra(Intent.EXTRA_EMAIL, to);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Mail depuis I Care");

                                final PackageManager packageManager = getPackageManager();
                                    final List<ResolveInfo> matches = packageManager.queryIntentActivities(intent,0);
                                    ResolveInfo best = null;
                                        for (final ResolveInfo info:matches){
                                            if(info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                                                best = info;

                                           if(best!=null){
                                               intent.setClassName(best.activityInfo.packageName,best.activityInfo.name);
                                           }
                                        }
                                    startActivity(intent);
                                        //startActivity(Intent.createChooser(intent,"Envoyer un email"));

                        }
                    });


                final ImageRequest imageRequest = new ImageRequest(post.getAuthor_pic(), new Response.Listener<Bitmap>(){
                    @Override
                    public void onResponse(Bitmap response) {
                        picture.setImageBitmap(response);

                    }
                }, 200,200, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Settings.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        AppController.getInstance().addToRequestQueue(imageRequest);



        phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*try{
                                String number = "237655474705";
                                    Intent openChat = new Intent("android.intent.action.MAIN");
                                        openChat.setAction(Intent.ACTION_SEND);
                                        openChat.setType("text/plain");
                                        openChat.putExtra("jid", number+ "@s.whatsapp.net");
                                        openChat.setPackage("com.whatsapp");
                                        startActivity(openChat);
                            }catch (Exception e){
                                Log.d("open whatsapp error", e.toString());
                                Toast.makeText(ArticleDetail.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }*/

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:222854939"));
                            startActivity(intent);
                        }
                    });

                    author_description.setText(post.getAuthor_bio());
                    rate.setText(post.getAuthor_rate());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running=false;
        this.mp.release();
        //textToSpeech.shutdown();
    }


}


