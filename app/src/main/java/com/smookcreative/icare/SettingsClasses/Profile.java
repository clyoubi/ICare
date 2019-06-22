package com.smookcreative.icare.SettingsClasses;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.hbb20.CountryCodePicker;
import com.smookcreative.icare.Adapters.UserInfos;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.DatabasesManagers.Singleton;
import com.smookcreative.icare.ILearn.ILearn;
import com.smookcreative.icare.Login;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Settings;
import com.smookcreative.icare.Tips.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private TextInputLayout passworf_layout;
    private EditText name, surname, displayname, email, login, phone, password;
    private MyPreferences myPreferences;
    private UserInfos userInfos;
    private Toolbar toolbar;
    private RelativeLayout container;
    private Menu menu;
    private RadioButton male, female;
    private RadioGroup radioGroup;
    List<EditText> editTexts;
    boolean modeEditable = false;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_profile);
            initViews();
        DisableFocusable();
        //if (AccessToken.getCurrentAccessToken() != null) {DisableFocusable(container, false);}
                setSupportActionBar(toolbar);

              userInfos = myPreferences.readUserInfos();
                setDatas(userInfos);

    }


    void setDatas(UserInfos user){

        name.setText(user.getLast_name());
        surname.setText(user.getFirst_name());
        displayname.setText(user.getDisplay_name());
        email.setText(user.getEmail());
        if(!user.getLogin_name().equals("@")){
            login.setText(user.getLogin_name());
        }
        //name.setText(user.getPictureUrl());

    }


    void initViews(){
        name = findViewById(R.id.input_first_name);
        surname = findViewById(R.id.input_last_name);
        displayname = findViewById(R.id.input_display_name);
        email = findViewById(R.id.input_email);
        login = findViewById(R.id.input_login);
        phone = findViewById(R.id.input_phone);
        password = findViewById(R.id.input_password);
        passworf_layout = findViewById(R.id.input_layout_password);
        radioGroup = findViewById(R.id.input_layout_gender);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        countryCodePicker = findViewById(R.id.countryPicker);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        container =findViewById(R.id.contenair);
        myPreferences = new MyPreferences(this);

            editTexts = new ArrayList<>();
            editTexts.add(name);
            editTexts.add(surname);
            editTexts.add(displayname);
            editTexts.add(email);
            editTexts.add(login);
            editTexts.add(password);
            editTexts.add(phone);


            Typeface ralewayRegular =Typeface.createFromAsset(getAssets(),getString(R.string.ralewayRegular));
            Fonts.setAllTextView(toolbar,ralewayRegular);
    }



    private void DisableFocusable() {

       for(int i =0; i<editTexts.size(); i++){
           editTexts.get(i).setFocusable(false);
           editTexts.get(i).setTypeface(Typeface.createFromAsset(getAssets(),getString(R.string.ralewayRegular)));
           editTexts.get(i).setTextColor(getResources().getColor(R.color.gray));
       }
        male.setClickable(false);
        female.setClickable(false);
        countryCodePicker.setClickable(false);
    }


    private void EnableFocusable() {

        for(int i =0; i<editTexts.size(); i++){
            editTexts.get(i).setFocusable(true);
            editTexts.get(i).setFocusableInTouchMode(true);
            editTexts.get(i).setTextColor(getResources().getColor(R.color.raven));
            passworf_layout.setVisibility(View.VISIBLE);

               if(editTexts.get(i).getId()==R.id.input_email){
                   editTexts.get(i).setFocusable(false);
                   editTexts.get(i).setFocusableInTouchMode(false);
                   editTexts.get(i).setTextColor(getResources().getColor(R.color.gray));
                }

                if(userInfos.getLogin_name().length()>3){
                    if(editTexts.get(i).getId()==R.id.input_login){
                        editTexts.get(i).setFocusable(false);
                        editTexts.get(i).setFocusableInTouchMode(false);
                        editTexts.get(i).setTextColor(getResources().getColor(R.color.gray));
                    }
                }

        }

        male.setClickable(true);
        male.setTextColor(getResources().getColor(R.color.raven));
        female.setClickable(true);
        female.setTextColor(getResources().getColor(R.color.raven));
        countryCodePicker.setClickable(true);
        countryCodePicker.setContentColor(getResources().getColor(R.color.raven));
    }


    void editMode(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);
        if(myPreferences.readUserInfos().isFacebook_login()){
            alertDialogBuilder.setTitle("Configurer mon compte");
            alertDialogBuilder.setMessage("Vous êtes connecté avec votre compte Facebook cette action créera un compte I Care lié a votre profil Facebook, vous pourrez ainsi vous connecter des deux manières");

            alertDialogBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            EnableFocusable();
                            menu.removeItem(R.id.edit);
                        }
                    });

            alertDialogBuilder.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });


        }else{
            alertDialogBuilder.setTitle("Confirmer votre Compte");
            alertDialogBuilder.setMessage("Veuillez entrer votre mot de passe");

                final EditText text = new EditText(this);
                    text.setWidth(200);
                    text.setId(R.id.input_password);
                    text.setHint("Mot de passe");
                    text.setSingleLine();
                    alertDialogBuilder.setView(text);

                        alertDialogBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        text.setFocusable(false);
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, RemoteServer.ConnectionScript, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                
                                                try {
                                                    JSONArray jsonArray = new JSONArray(response);
                                                    JSONObject jsonObject=jsonArray.getJSONObject(0);

                                                    if(jsonObject.getString("response_code").equals("Found")) {
                                                        EnableFocusable();
                                                        password.setText(text.getText().toString());
                                                        modeEditable = true;
                                                    }else
                                                    {
                                                        Toast.makeText(Profile.this, "Mot de passe Incorrect", Toast.LENGTH_SHORT).show();
                                                    }


                                                } catch (JSONException e) {
                                                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                                                    e.printStackTrace();
                                                }

                                                text.setFocusable(true);
                                                text.setFocusableInTouchMode(true);
                                            }


                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(Profile.this, "Problème de connexion", Toast.LENGTH_SHORT).show();
                                                text.setFocusable(true);
                                                text.setFocusableInTouchMode(true);
                                            }
                                        }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new Hashtable<String, String>();
                                                params.put("email",myPreferences.readUserInfos().getEmail());
                                                params.put("password",text.getText().toString());
                                                return params;
                                            }
                                        };
                                        Singleton.getInstance(Profile.this).addToRequestqueue(stringRequest);


                                    }

                                });

                        alertDialogBuilder.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

            
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.draft, menu);
            menu.add(1,R.id.edit,1,"Modifier").setIcon(getResources().getDrawable(R.drawable.ic_edit_black_24dp)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
            switch (id){
                case R.id.action_save:
                    CheckEntries();
                    break;
                case R.id.edit:
                    editMode();
                    break;
            }

        return true;
    }


    private void UpdateUserInfos(){
        final JSONObject jsonObject = new JSONObject();

            myPreferences = new MyPreferences(this);
            UserInfos userInfos = myPreferences.readUserInfos();


                    try {
                        boolean gender;
                        if(radioGroup.getCheckedRadioButtonId()== R.id.male)
                            gender = true;
                        else
                            gender = false;

                        JSONArray jsonArray = new JSONArray();

                            final JSONObject object = new JSONObject();
                            final JSONObject object_meta = new JSONObject();
                                //jsonArray.put(0, object);


                        object.put("ID", userInfos.getId());
                        object.put("last_name", name.getText().toString());
                        object.put("first_name", surname.getText().toString());
                        object.put("display_name", displayname.getText().toString());
                        object.put("user_pass", password.getText().toString());

                        String code = countryCodePicker.getSelectedCountryCode();
                        JSONArray array = new JSONArray();
                            array.put(code);
                            array.put(phone.getText().toString());

                        object_meta.put("phone_number",array.toString());
                        object_meta.put("user_gender", gender);
                        //object.put("picture_url", userInfos.getPictureUrl());


                        StringRequest stringRequest = new StringRequest(Request.Method.POST, RemoteServer.Update_User_Script, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //save local version
                                try {
                                    JSONObject obj = new JSONObject(response);

                                    if(obj.getString("response_code").equals("done")){
                                        SaveLocaly(object, object_meta);
                                        Toast.makeText(Profile.this, "Sauvegarde réussie", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(Profile.this, "Un problème est survenu durant l'execution", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Profile.this, "Problème de connexion", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new Hashtable<String, String>();
                                params.put("user_infos", object.toString());
                                params.put("user_meta", object_meta.toString());
                                params.put("token", myPreferences.readUserInfos().getUser_hash());
                                return params;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(stringRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

    }


    private void SaveLocaly(JSONObject object, JSONObject object2){
        UserInfos infos;
            infos = myPreferences.readUserInfos();

        try {

            infos.setFirst_name(object.getString("first_name"));
            infos.setLast_name(object.getString("last_name"));
            infos.setDisplay_name(object.getString("display_name"));
            infos.setPhone(object2.getString("phone_number"));
            infos.setGender(object2.getBoolean("user_gender"));

                myPreferences.saveUserinfos(infos);

            ((Settings)getApplicationContext()).finish();

            Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private boolean CheckEntries(){
        boolean state = false;
        if(name.getText().length()<3 || surname.getText().length()<3 || displayname.getText().length()<3 || phone.getText().length()<7){
            state=false;
            Toast.makeText(this, "Veuillez remplir correctement tous les champs", Toast.LENGTH_SHORT).show();
                return state;
        }else{
            state= true;
            UpdateUserInfos();
        }

        return state;
    }





    @Override
    public void onBackPressed() {

        if(!modeEditable){
            Intent settings= new Intent(this, Settings.class);
            startActivity(settings);
            finish();
        }else{

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);
                alertDialogBuilder.setTitle("Annuler l'édition");
                alertDialogBuilder.setMessage("Voulez-vous annuler la modification");

                    alertDialogBuilder.setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DisableFocusable();
                            modeEditable = false;
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }
}
