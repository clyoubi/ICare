package com.smookcreative.icare.ILearn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.smookcreative.icare.AboutActivity;
import com.smookcreative.icare.Adapters.CategoryObject;
import com.smookcreative.icare.BottomNavigationViewHelper;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.Login;
import com.smookcreative.icare.MainActivity;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Settings;
import com.smookcreative.icare.Tips.Fonts;

import java.util.ArrayList;

public class ILearn extends AppCompatActivity{


    Fonts yo =new Fonts();
    private Typeface ralewaybold;
    private Typeface ralewayRegular, ralewaySemibold;
    private Menu menu;
    private MenuItem menuItem;
    private RecyclerView category_layout_filer;
    private boolean isOpen=false;
    private PagerSlidingTabStrip tabsStrip;
    private ViewPager viewPager;
    private MyPreferences myPreferences;
    public static Toolbar toolbar;
    public static boolean isAppRunning;
   // private RecyclerView horizontal_recycler_view;
    //private RecycleCategoriesAdapters recycleCategoriesAdapters;
    private ArrayList<CategoryObject> categoriesList =new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ilearn);

        isAppRunning= true;
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.vpPager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
        tabsStrip.setAllCaps(false);
        category_layout_filer=(RecyclerView)findViewById(R.id.categories_filter);
        tabsStrip.setIndicatorHeight(10);
        tabsStrip.setAllCaps(false);



        ralewayRegular=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayRegular));
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_menu);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        menu=bottomNavigationView.getMenu();
        menuItem=menu.getItem(0);
        menuItem.setChecked(true);

        Fonts.setAllTextView(toolbar,ralewayRegular);
        Fonts.setAllTextView(tabsStrip,ralewaySemibold);
        yo.setAllTextView(bottomNavigationView, ralewayRegular);



        /*horizontal_recycler_view = (RecyclerView) findViewById(R.id.categories_filter);
        categoriesList.add(0, new CategoryObject("123456","Tout","1", "true"));
        categoriesList.add(1, new CategoryObject("123457","Favoris","1"));
        recycleCategoriesAdapters = new RecycleCategoriesAdapters(getApplicationContext(), categoriesList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ILearn.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(recycleCategoriesAdapters);

        LoadCategories();*/


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.search:
                        Intent ilearn_Activity = new Intent(getApplicationContext(), MainActivity.class);
                        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ilearn_Activity);
                        break;

                    case R.id.settings:
                        Intent settings_Activity = new Intent(getApplicationContext(), Settings.class);
                        settings_Activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        settings_Activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(settings_Activity);
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



    }









    public void ExitApplication(boolean disconnect){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if(!disconnect){

            alertDialogBuilder.setTitle("Fermeture");
            alertDialogBuilder.setMessage("Voulez-vous quitter I Care?");

            alertDialogBuilder.setPositiveButton("Oui",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Toast.makeText(ILearn.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory( Intent.CATEGORY_HOME );
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }
                    });

            alertDialogBuilder.setNegativeButton("Non",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }else{
            alertDialogBuilder.setTitle("Déconnexion");
            alertDialogBuilder.setMessage("Voulez-vous vraiment vous déconnecter? Cette action effacera vos données d'utilisation dans l'application!");

            alertDialogBuilder.setPositiveButton("Oui",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            myPreferences = new MyPreferences(getApplicationContext());
                            myPreferences.DeleteUserInfos();
                            Intent login = new Intent(getApplicationContext(), Login.class);
                            startActivity(login);
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("Non",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        ExitApplication(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppRunning=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }

        if (id==R.id.favoris) {
            Intent intent= new Intent(this, Favourites.class);
            startActivity(intent);
        }

        if(id==R.id.saved){
            Intent intent = new Intent(this,SavedArticles.class);
            startActivity(intent);
        }

        return true;
    }
}
