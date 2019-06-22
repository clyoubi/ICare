package com.smookcreative.icare;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.smookcreative.icare.Adapters.UserInfos;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.DatabasesManagers.Singleton;
import com.smookcreative.icare.ILearn.ILearn;
import com.smookcreative.icare.OtherActivities.AccountCreatedCongrats;
import com.smookcreative.icare.Tips.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login extends AppCompatActivity {

    Fonts yo =new Fonts();

    Button connection_btn, create_account_btn;
    TextView error_message, onCreate_error_message;
    EditText userId, userpassword;
    private EditText login_name, first_name, last_name, user_email, user_password;
    private ProgressDialog progressDialog;
    private FloatingActionButton fab;
    private boolean isOpen=false;
    private RelativeLayout Create_layout;
    LoginButton loginButton;
    CallbackManager callbackManager;
    private MyPreferences myPreferences;
    private ArrayList<EditText> allFormTextIputs;
    private ProgressBar create_loader, signin_loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.login);
        loginButton = findViewById(R.id.login_button);

        //disconnectFromFacebook();




        //facebook Authentication
       callbackManager = CallbackManager.Factory.create();
       loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
       //loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends", "user_age_range", "user_gender", "user_location"));


        //LoginManager.getInstance().registerCallback(callbackManager,
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        final Profile profile = Profile.getCurrentProfile();
                        //Toast.makeText(Login.this, profile.toString(), Toast.LENGTH_LONG).show();

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback(){
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.d("LoginActivity", response.toString());
                                            Log.d("LoginActivity Object", object.toString());
                                            myPreferences = new MyPreferences(getApplicationContext());
                                            //Log.d("LoginActivity", response.getJSONObject().getString("graphObject").toString());
                                            // Application code
                                            //Toast.makeText(Login.this, object.toString(), Toast.LENGTH_SHORT).show();

                                               try {
                                                    UserInfos userInfos = new UserInfos();
                                                    userInfos.setId(object.getString("id"));
                                                    userInfos.setFirst_name(object.getString("first_name"));
                                                    userInfos.setLast_name(object.getString("last_name"));
                                                    userInfos.setDisplay_name(object.getString("name"));
                                                    userInfos.setEmail(object.getString("email"));
                                                    //userInfos.setLogin_name(object.getString("username"));
                                                    userInfos.setAccount_type("subscriber");
                                                    userInfos.setUser_status("1");
                                                    userInfos.setPictureUrl(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                                                    userInfos.setFacebook_login(true);

                                                   // userInfos.setDisplay_name(profile.getName());
                                                   //userInfos.setNice_name(object.getString("user_nicename"));
                                                    //userInfos.setUser_hash(object.getString(loginResult.getAccessToken().getToken()));

                                                    //myPreferences.saveUserinfos(userInfos);
                                                        CheckFaceBookUserEmail(userInfos, myPreferences);
                                                        //DownloadProfilImage(userInfos.getPictureUrl());

                                                    Intent main_activity = new Intent(getApplicationContext(), ILearn.class);
                                                    startActivity(main_activity);
                                                    finish();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                   Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
                                                }

                                        }
                                });
                       /* Toast.makeText(getApplicationContext(),loginResult.toString(),Toast.LENGTH_LONG).show();*/
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,first_name,last_name,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(Login.this, "Canceled", Toast.LENGTH_LONG).show();
                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(Login.this, "Facebook Exception"+exception.toString(), Toast.LENGTH_LONG).show();
                    }
                });



        // end Login

        login_name = findViewById(R.id.input_login_name);
        first_name = findViewById(R.id.input_first_name);
        last_name =findViewById(R.id.input_last_name);
        user_email = findViewById(R.id.input_email);
        user_password = findViewById(R.id.input_password);
            error_message= findViewById(R.id.error_message);
            onCreate_error_message= findViewById(R.id.onCreate_error_message);

        //Set Typefaces
        Typeface Kris=Typeface.createFromAsset(getAssets(),getString(R.string.kristen));
        ViewGroup group =  getWindow().getDecorView().findViewById(R.id.contenair);
        TextView welcome = findViewById(R.id.welcome);
        TextView welcome2 = findViewById(R.id.introText);
        //yo.setAllTextView(group,Kris);
        welcome.setTypeface(Kris);
        welcome2.setTypeface(Kris);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        Create_layout=findViewById(R.id.create_layout);

        //loaders
        create_loader = findViewById(R.id.create_loader);
        signin_loader = findViewById(R.id.signin_loader);

        connection_btn = findViewById(R.id.connection_btn);
        create_account_btn = findViewById(R.id.create_account_btn);
        userId =(EditText)findViewById(R.id.userId);
        userpassword= (EditText)findViewById(R.id.userPassword);

        connection_btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {


                if(userId.getText().length()==0 || userpassword.getText().length()==0){

                        userId.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext_red_stroke));
                        userpassword.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext_red_stroke));

                    error_message.setVisibility(View.VISIBLE);
                    error_message.setText("Saisissez vos pasramètres de connexion");
                }else{

                    connection_btn.setClickable(false);
                    connection_btn.setVisibility(View.INVISIBLE);
                    signin_loader.setVisibility(View.VISIBLE);

                final String name = userId.getText().toString();
                final String mdpasse = userpassword.getText().toString();



                StringRequest stringRequest = new StringRequest(Request.Method.POST, RemoteServer.ConnectionScript, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            error_message.setVisibility(View.INVISIBLE);
                            userId.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext));
                            userpassword.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext));

                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject=jsonArray.getJSONObject(0);

                            if(jsonObject.getString("response_code").equals("Found")) {

                                myPreferences = new MyPreferences(getApplicationContext());

                                UserInfos userInfos = new UserInfos();

                                JSONObject user = jsonArray.getJSONObject(1);

                                userInfos.setId(user.getString("id"));
                                userInfos.setFirst_name(user.getString("first_name"));
                                userInfos.setLast_name(user.getString("last_name"));
                                userInfos.setDisplay_name(user.getString("display_name"));
                                userInfos.setNice_name(user.getString("user_nicename"));
                                userInfos.setLogin_name(user.getString("user_login"));
                                userInfos.setEmail(user.getString("user_email"));
                                userInfos.setAccount_type(user.getString("user_role"));
                                userInfos.setUser_hash(user.getString("_token"));
                                userInfos.setUser_status(user.getString("user_status"));
                                userInfos.setPictureUrl(user.getString("picture_url"));


                                myPreferences.saveUserinfos(userInfos);
                                Intent main_activity = new Intent(getApplicationContext(), ILearn.class);
                                startActivity(main_activity);
                                finish();
                            }else
                            {
                                Toast.makeText(getApplicationContext(),"user not Found",Toast.LENGTH_LONG).show();
                                error_message.setVisibility(View.VISIBLE);
                                userId.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext_red_stroke));
                                userpassword.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext_red_stroke));
                            }


                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                        connection_btn.setClickable(true);
                        connection_btn.setVisibility(View.VISIBLE);
                        signin_loader.setVisibility(View.INVISIBLE);
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Volley Error",Toast.LENGTH_LONG).show();
                        connection_btn.setClickable(true);
                        connection_btn.setVisibility(View.VISIBLE);
                        signin_loader.setVisibility(View.INVISIBLE);
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new Hashtable<String, String>();
                        params.put("email",name);
                        params.put("password",mdpasse);
                        return params;
                    }
                };
                Singleton.getInstance(Login.this).addToRequestqueue(stringRequest);

            }

            }
        });


        final RelativeLayout rel = findViewById(R.id.container);
        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getAllTextViewField(rel);
                boolean inputValidation;
                boolean state=true;
               for(int i=0; i<allFormTextIputs.size(); i++){
                   inputValidation=VerifyInputs(allFormTextIputs.get(i));
                        if(!inputValidation){
                            state=false;
                        }
               }

                    if(state){
                        CreateUser();
                    }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int x = (int) v.getX();
                    int y = (int) v.getY();
                    reveal(x, y);
                }else{
                    isOpen=true;
                    Create_layout.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    private void DownloadProfilImage(String imageUrl){

        Bitmap userPic = null;

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            userPic = BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error Firebase Image", e.toString());
        }


        File file = new File(getFilesDir(),"Pictures/");
        if(!file.exists())
            file.mkdirs();

        String name = "profile_picture.jpg";
        File pic = new File(file, name);

        if(pic.exists()){pic.delete();}else{
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(pic);
                userPic.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void CreateUser(){

        final String f_name, l_name, login, email, password;
            f_name=first_name.getText().toString();
            l_name=last_name.getText().toString();
            login=login_name.getText().toString();
            email=user_email.getText().toString();
            password=user_password.getText().toString();

        create_account_btn.setClickable(false);
        create_account_btn.setVisibility(View.INVISIBLE);
        create_loader.setVisibility(View.VISIBLE);

        String url =RemoteServer.Create_User_Script+"";

        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String responseCode = jsonObject.getString("response");
                        switch (responseCode){
                            case "created":
                                Intent intent =new Intent(getApplicationContext(), AccountCreatedCongrats.class);
                                startActivity(intent);
                                finish();


                            case "login_exist":
                                //Toast.makeText(getApplicationContext(),responseCode, Toast.LENGTH_LONG).show();
                                login_name.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext_red_stroke));
                                login_name.getText().clear();
                            case "email_exist":
                                //Toast.makeText(getApplicationContext(),responseCode, Toast.LENGTH_LONG).show();
                                user_email.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext_red_stroke));
                                user_email.getText().clear();
                            case "wp_error":
                                //Toast.makeText(getApplicationContext(),responseCode, Toast.LENGTH_LONG).show();
                            case "mail_error":
                                //Toast.makeText(getApplicationContext(),responseCode, Toast.LENGTH_LONG).show();
                            case "update_hash_error":
                                //Toast.makeText(getApplicationContext(),responseCode, Toast.LENGTH_LONG).show();
                        }

                        if(responseCode.equals("login_exist") || responseCode.equals("email_exist")){
                            onCreate_error_message.setText(responseCode);
                            onCreate_error_message.setVisibility(View.VISIBLE);
                        }else{
                            onCreate_error_message.setVisibility(View.INVISIBLE);
                        }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                create_account_btn.setClickable(true);
                create_account_btn.setVisibility(View.VISIBLE);
                create_loader.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "VOLLEY ERROR "+error.toString(), Toast.LENGTH_LONG).show();
                create_account_btn.setClickable(true);
                create_account_btn.setVisibility(View.VISIBLE);
                create_loader.setVisibility(View.INVISIBLE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("login_name",login);
                params.put("first_name",f_name);
                params.put("last_name", l_name);
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }


    private boolean VerifyInputs(EditText textView){
        boolean correctInputs = false;
        String hint = textView.getHint().toString();

        if (textView.getText().length()<4){
            textView.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext_red_stroke));
            correctInputs = false;

            if(textView.getId()==R.id.input_first_name || textView.getId()==R.id.input_last_name){
                if(textView.length()>=2)
                    textView.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext));
                    correctInputs=true;
            }

        }else {
            textView.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext));
            correctInputs = true;
        }

            if(textView.getId()==R.id.input_email){
                /*String expression = "^[\\w\\.-]+@([\\w\\.-]+\\.)+[A-Z]{2,4}$";
                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(textView.getText().toString());*/
                boolean match = Patterns.EMAIL_ADDRESS.matcher(textView.getText().toString()).matches();


                    if(!match){
                        textView.setHint("Entrez une adresse mail correcte");
                        textView.setHintTextColor(getResources().getColor(R.color.red));
                        textView.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext_red_stroke));
                        textView.getText().clear();
                        correctInputs = false;
                    }else{
                        textView.setHint("Email");
                        textView.setBackground(getResources().getDrawable(R.drawable.round_corners_edittext));
                        correctInputs= true;
                    }
            }

        if(textView.getId()==R.id.input_password){

        }

        return correctInputs;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void reveal(int x, int y) {

        if (!isOpen) {
            int cx = Create_layout.getWidth();
            int cy = Create_layout.getHeight();

            float finalradius = Math.max(cx, cy) * 1.2f;
            Animator reveal = ViewAnimationUtils
                    .createCircularReveal(Create_layout, x, y, 0f, finalradius);
            Create_layout.setVisibility(View.VISIBLE);

            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    fab.hide();
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_black_24dp));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    fab.show();
                    userId.setVisibility(View.INVISIBLE);
                    userpassword.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            reveal.start();


            isOpen = true;
        } else {

            userId.setVisibility(View.VISIBLE);
            userpassword.setVisibility(View.VISIBLE);

            int cx = Create_layout.getWidth();
            int cy = Create_layout.getHeight();

            float finalradius = Math.max(cx, cy) * 1.2f;
            Animator reveal = ViewAnimationUtils
                    .createCircularReveal(Create_layout, x, y, finalradius, 0f);


            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    fab.hide();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Create_layout.setVisibility(View.INVISIBLE);

                    fab.setImageDrawable(getResources().getDrawable(R.drawable.add_user));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accentBlue)));
                    fab.show();


                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            reveal.start();

            isOpen = false;
        }

    }


    private void CheckFaceBookUserEmail(final UserInfos userInfos, final MyPreferences myPreferences){

        String url = RemoteServer.CheckEmailcript+"?email="+userInfos.getEmail();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                        JSONObject responseCode = jsonArray.getJSONObject(0);

                            if(responseCode.getString("response_code").equals("Email_Found")){
                               JSONObject user = jsonArray.getJSONObject(1);

                                    userInfos.setId(user.getString("id"));
                                    userInfos.setLogin_name(user.getString("user_login"));
                                    userInfos.setAccount_type(user.getString("user_role"));
                                    userInfos.setUser_hash(user.getString("user_hash"));
                                    userInfos.setUser_status(user.getString("user_status"));
                                    userInfos.setFacebook_login(false);
                                    if(user.getBoolean("has_picture"))
                                        userInfos.setPictureUrl(user.getString("user_picture"));

                            }else{
                                    userInfos.setFirstTime(true);
                            }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erreur de verification de l'email Facebook sur Icare", Toast.LENGTH_SHORT).show();
                }

                myPreferences.saveUserinfos(userInfos);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erreur de verification de l'email Facebook sur Icare probleme de connexion", Toast.LENGTH_SHORT).show();
                myPreferences.saveUserinfos(userInfos);
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    public void performingRequest(boolean state){
        int visibility;
        if(state)
            visibility= View.VISIBLE;
        else
            visibility=View.INVISIBLE;

        connection_btn.setClickable(!state);
        create_loader.setVisibility(visibility);
        connection_btn.setVisibility(visibility);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    public void getAllTextViewField(ViewGroup parent) {
        allFormTextIputs = new ArrayList<>();
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
             View child = parent.getChildAt(i);
           if(child instanceof EditText){
               allFormTextIputs.add((EditText) child);
           }
        }
       // Toast.makeText(this,String.valueOf(allFormTextIputs.size()),Toast.LENGTH_LONG).show();
    }

}



