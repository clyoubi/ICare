package com.smookcreative.icare.ILearn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smookcreative.icare.Adapters.ArticleAdapter;
import com.smookcreative.icare.Adapters.ArticleObject;
import com.smookcreative.icare.Adapters.CategoryAdapter;
import com.smookcreative.icare.Adapters.CategoryObject;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Tips.Fonts;

import java.util.ArrayList;

public class SavedArticles extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView listView;
    private ArticleAdapter adapter;
    public static ArrayList<ArticleObject> articles =new ArrayList<>();
    private int count=0;
    private Menu mainMenu;
    private boolean multiSelectMode=false, checkstate;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_articles);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyPreferences myPreferences = new MyPreferences(this);
        articles =  myPreferences.ReadSavedArticles();
        //articles.add(new ArticleObject("Plus d'articles",""));

        listView = findViewById(R.id.listView);
        adapter = new ArticleAdapter(this, articles,1);
        listView.setAdapter(adapter);

        title = "Sauvegardés("+String.valueOf(articles.size())+"/100)";
        getSupportActionBar().setTitle(title);
        //ILearn.toolbar.setTitle("Sauvegardés("+String.valueOf(articles.size()-1)+")");
        adapter.notifyDataSetChanged();

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //articles.clear();
                pullToRefresh.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!multiSelectMode){
                    if(position<articles.size()){
                        Intent intent = new Intent(parent.getContext(),ArticleDetail.class);
                        intent.putExtra("article", articles.get(position));
                        ArticleDetail.saveMode = true;
                        intent.putExtra("savedMode",true);
                        intent.putExtra("position", position);
                        startActivity(intent);
                       // finish();
                    }
                }else{
                    count++;
                    view.setBackgroundColor(getResources().getColor(R.color.gray));
                    getSupportActionBar().setTitle(String.valueOf(count));

                }

            }
        });



/*
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(!checkstate){
                    count=1;
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibe != null) {
                        vibe.vibrate(200); // 50 is time in ms
                    }
                   OpenContextual();
                    view.setBackgroundColor(getResources().getColor(R.color.gray));
                }

                return true;
            }
        });
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // MenuInflater menuInflater = getMenuInflater();
        //menuInflater.inflate(R.menu.save_articles, menu);
        mainMenu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            CloseContextual();
        }
        if (id==R.id.action_save){

        }
        return true;
    }

    @Override
    public void onBackPressed() {
            CloseContextual();
    }

    private void OpenContextual(){
        getSupportActionBar().setTitle(String.valueOf(count));
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_articles, mainMenu);
        multiSelectMode=true;
        checkstate = true;
    }


    private void CloseContextual(){
        if(multiSelectMode){
            getSupportActionBar().setTitle(title);
            mainMenu.clear();
            multiSelectMode=false;
            checkstate = false;
        }else{
            startActivity(new Intent(this, ILearn.class));
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
       adapter.notifyDataSetChanged();
    }
}

