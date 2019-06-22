package com.smookcreative.icare.OtherActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smookcreative.icare.Login;
import com.smookcreative.icare.R;

public class AccountCreatedCongrats extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_create_succes);


        Button activateAccout = findViewById(R.id.contiueWith);
        TextView signin = findViewById(R.id.signin);

            activateAccout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   try{
                       Intent i = new Intent(Intent.ACTION_VIEW);
                       i.setClassName("com.google.android.gm","com.google.android.gm.ConversationListActivity");
                       startActivity(i);
                   }catch (Exception e){
                       Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                   }

                }
            });


            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent login = new Intent(getApplicationContext(), Login.class);
                    startActivity(login);
                    finish();
                }
            });

    }
}
