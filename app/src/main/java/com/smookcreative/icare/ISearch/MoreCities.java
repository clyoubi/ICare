package com.smookcreative.icare.ISearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.smookcreative.icare.Adapters.CategoryObject;
import com.smookcreative.icare.Adapters.MoreCitiesAdapter;
import com.smookcreative.icare.Adapters.UserInfos;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.MainActivity;
import com.smookcreative.icare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoreCities extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ArrayList<CategoryObject> liste = new ArrayList<>();
    ArrayList<CategoryObject> list = new ArrayList<>();
    MoreCitiesAdapter moreCitiesAdapter;
    ListView listView;
    RelativeLayout default_city_layout;
    Typeface ralewayRegular;
    public static int idPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_cities);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ralewayRegular = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.ralewayRegular));
        default_city_layout =findViewById(R.id.default_city);
        RadioButton radio_city = findViewById(R.id.default_city_radio);

        boolean launch = getIntent().getBooleanExtra("launch", false);

            if(launch){
                toolbar.setTitle("Ville par défaut");
                default_city_layout.setVisibility(View.GONE);
            }

        MyPreferences myPreferences = new MyPreferences(this);
        try {
            JSONArray jsonArray = new JSONArray(myPreferences.readUserInfos().getCity());
                radio_city.setChecked(true);
                radio_city.setText(Html.fromHtml(jsonArray.get(1).toString()));
                radio_city.setTypeface(ralewayRegular);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.pullToRefresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    LoadDatas();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        listView = findViewById(R.id.listView);

           LoadDatas();

    }

    public void LoadDatas(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(RemoteServer.CitiesScript, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    liste.clear();
                    for(int i=0; i<response.length(); i++){

                            JSONObject obj = response.getJSONObject(i);
                            CategoryObject cat = new CategoryObject();
                               cat.setID(obj.getString("slug"));
                                cat.setCatName( obj.getString("name"));
                                cat.setArticles_count(obj.getString("count"));

                             liste.add(cat);
                    }

                    list = liste;
                    moreCitiesAdapter = new MoreCitiesAdapter(list, getApplicationContext());
                    listView.setAdapter(moreCitiesAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


    }


    private void ShowAlerDialog(final ArrayList<CategoryObject> List, final int pos){


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Changer de ville?");
        alertDialogBuilder.setMessage("Utiliser comme ville par défaut?");

        alertDialogBuilder.setPositiveButton("Oui",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        List.get(pos).setLiked(true);
                        JSONArray array = new JSONArray();
                        array.put(List.get(pos).getID());
                        array.put(List.get(pos).getCatName());

                        MyPreferences myPreferences = new MyPreferences(getApplicationContext());
                        UserInfos userInfos = myPreferences.readUserInfos();
                        userInfos.setCity(array.toString());
                        myPreferences.saveUserinfos(userInfos);
                        moreCitiesAdapter.notifyDataSetChanged();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("city",List.get(pos).getID());
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Non",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("city",List.get(pos).getID());
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            /*case R.id.go:
                SetCity(idPosition);
                break;*/
            case R.id.save:
                setDefaultCity(idPosition);
                break;
        }
        return true;
    }


    void setDefaultCity(int pos){
        Toast.makeText(this, list.get(pos).getID(), Toast.LENGTH_LONG).show();
        list.get(pos).setLiked(true);
        JSONArray array = new JSONArray();
        array.put(list.get(pos).getID());
        array.put(list.get(pos).getCatName());

        MyPreferences myPreferences =new MyPreferences(this);
        UserInfos userInfos = myPreferences.readUserInfos();
        userInfos.setCity(array.toString());
        myPreferences.saveUserinfos(userInfos);
        moreCitiesAdapter.notifyDataSetChanged();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("city",list.get(pos).getID());
        startActivity(intent);
        finish();
    }

    void SetCity(int pos){
        Toast.makeText(this, liste.get(pos).getID(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("city",liste.get(pos).getID());
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        list = new ArrayList<>();
        moreCitiesAdapter = new MoreCitiesAdapter(list, this);
        listView.setAdapter(moreCitiesAdapter);
        for(CategoryObject cat:liste){
            if(cat.getCatName().toLowerCase().contains(newText.toLowerCase())){
                list.add(cat);
            }
        }
        moreCitiesAdapter.notifyDataSetChanged();

        return false;
    }
}
