package com.smookcreative.icare.OtherActivities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smookcreative.icare.AboutActivity;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

public class ReportIssues extends AppCompatActivity {

    TextView policy;
    private MyPreferences myPreferences;
    Button sendRepport;
    EditText textView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_issues);
        Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            sendRepport = findViewById(R.id.sendRepport);
            textView = findViewById(R.id.issuesText);
            progressBar = findViewById(R.id.progressBar);


            myPreferences = new MyPreferences(this);

            policy = findViewById(R.id.poilicy);
            policy.setText(Html.fromHtml( "Les informations sur votre appareil, votre compte et cette application seront automatiquement inclusent dans ce rapport pour en savoir plus sur l'utilisation de vos données veuillez consulter notre <b style='color:#573d5e;'>Politique de confidentialité</b>"));


                textView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        if(s.length()<20){
                            sendRepport.setClickable(false);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                sendRepport.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));

                            }else{
                                sendRepport.setBackgroundColor(getResources().getColor(R.color.gray));
                            }
                        }else{
                            sendRepport.setClickable(true);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                sendRepport.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                            }else{
                                sendRepport.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            }
                        }

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                sendRepport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(textView.getText().length()>20)
                        SendIssues();
                }
            });


    }


    private void SendIssues(){

        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("ID", Build.ID);
            jsonObject.put("OS", System.getProperty("os.version"));
            jsonObject.put("SDK", Build.VERSION.SDK);
            jsonObject.put("Product", Build.PRODUCT);
            jsonObject.put("User", Build.USER);
            jsonObject.put("Device", Build.DEVICE);
            jsonObject.put("Model", Build.MODEL);
            jsonObject.put("Brand", Build.BRAND);
            jsonObject.put("Manufacturer", Build.MANUFACTURER);
            jsonObject.put("Hardware", Build.HARDWARE);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Json Add exception", e.toString());
        }

        sendRepport.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RemoteServer.Report_Crashes_Script, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ReportIssues.this, "Votre probleme a bien été reçu merci de nous aider à améliorer notre produit pour vous", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                sendRepport.setVisibility(View.VISIBLE);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReportIssues.this, "Erreur de connexion au réseau: "+error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                sendRepport.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("user_id",myPreferences.readUserInfos().getId());
                params.put("user_email",myPreferences.readUserInfos().getEmail());
                params.put("device_info", jsonObject.toString());
                params.put("description", textView.getText().toString());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }


    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        finish();*/
        supportFinishAfterTransition();
    }


}
