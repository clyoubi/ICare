package com.smookcreative.icare.ILearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smookcreative.icare.Adapters.CategoryAdapter;
import com.smookcreative.icare.Adapters.CategoryObject;
import com.smookcreative.icare.Adapters.CommentsAdapter;
import com.smookcreative.icare.Adapters.CommentsObject;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Favourites extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView listView;
    private CategoryAdapter adapter;
    private ArrayList<CategoryObject> categoriesList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_favourites);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);
        adapter = new CategoryAdapter(getApplicationContext(), categoriesList);
        listView.setAdapter(adapter);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoriesList.clear();
                LoadCategories();
                pullToRefresh.setRefreshing(false);
            }
        });
        LoadCategories();
    }



    private void LoadCategories() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Chargemment...");
        pDialog.show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, RemoteServer.ILearn_Categories_Script, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidePDialog();

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

                    } else {
                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }

                    setFavourites();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                Snackbar.make(listView, "Probleme de connexion au serveur veuillez réessayer", 2000).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.draft, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
        }
        if (id==R.id.action_save){
            saveFavourites();
        }
        return true;
    }


      public void setFavourites(){
        MyPreferences myPreferences = new MyPreferences(this);

        try {
            JSONArray favourites =new JSONArray(myPreferences.readUserInfos().getUser_favourites());
            for (int i=0;i<favourites.length();i++){

                for (CategoryObject cat:categoriesList) {
                       // Toast.makeText(this, favourites.getString(i), Toast.LENGTH_LONG).show();
                                if (favourites.getString(i).equals(cat.getID())){
                                    cat.setLiked(true);
                                }
                           }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
}


    private boolean saveFavourites(){
        String favourites = adapter.getFavourites();
        MyPreferences myPreferences = new MyPreferences(this);
        Toast.makeText(this, favourites, Toast.LENGTH_LONG).show();
         return myPreferences.AddNewUserInfo(getResources().getString(R.string.favourite_list),favourites, false);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ILearn.class));
        finish();
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
