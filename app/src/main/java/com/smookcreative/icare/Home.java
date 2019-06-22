package com.smookcreative.icare;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.smookcreative.icare.ILearn.ILearn;
import com.smookcreative.icare.Tips.Fonts;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    private CircleImageView profile;
    private CardView SearchView, IlearnCard, Planning;
    Fonts yo =new Fonts();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Set Typefaces
        Typeface ralewayRegular=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayRegular));
        Typeface ralewaybold=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayBold));
        ViewGroup header =  getWindow().getDecorView().findViewById(R.id.relativeLayout);
        ViewGroup cards =  getWindow().getDecorView().findViewById(R.id.cards);

        yo.setAllTextView(header,ralewaybold);
        yo.setAllTextView(cards,ralewayRegular);

        profile = findViewById(R.id.profile);

        //Declaring Card Views
        SearchView = findViewById(R.id.search);
        IlearnCard = findViewById(R.id.ilearn);
        Planning = findViewById(R.id.planning);

        SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search_Activity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(search_Activity);
            }
        });

        IlearnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ilearn_Activity = new Intent(getApplicationContext(), ILearn.class);
                startActivity(ilearn_Activity);
            }
        });

        Planning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ilearn_Activity = new Intent(getApplicationContext(), Planning.class);
                startActivity(ilearn_Activity);
            }
        });



    }


    private void closeApp(){


        AlertDialog.Builder alertDialog=  new AlertDialog.Builder(this);

        alertDialog.setTitle("Fermeture");
        alertDialog.setMessage("voulez-vous fermer l'application?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert=alertDialog.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }

        if (id==R.id.icare4u){
            Intent ICare4u = new Intent(getApplicationContext(), Icare4u.class);
            startActivity(ICare4u);
        }

        return true;
    }
}
