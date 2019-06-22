package com.smookcreative.icare;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.smookcreative.icare.Adapters.ParamAdapter;
import com.smookcreative.icare.Adapters.UserInfos;
import com.smookcreative.icare.Adapters.paramsObject;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.ILearn.ILearn;
import com.smookcreative.icare.SettingsClasses.Profile;
import com.smookcreative.icare.Tips.Fonts;
import com.smookcreative.icare.Tips.NonScrollListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    private MyPreferences myPreferences;
    private MenuItem menuItem;
    private Menu menu;
    private NonScrollListView nonScrollListView;
    private ParamAdapter adapter;
    private ArrayList<paramsObject> paramsObjects = new ArrayList<>();
    private NestedScrollView nestedScrollView;
    private CircleImageView profile_picture;
    private ImagePicker imagePicker;
    private ProgressBar imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        nestedScrollView = findViewById(R.id.container);
        profile_picture = findViewById(R.id.circleColor);
        imageLoader =findViewById(R.id.imageLoader);

        menu=bottomNavigationView.getMenu();
        menuItem=menu.getItem(2);
        menuItem.setChecked(true);

        //Set Typefaces
        Typeface ralewayRegular=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayRegular));
        Typeface ralewaybold=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayBold));
        Typeface ralewayitalic=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayItalic));
        //ViewGroup group =  getWindow().getDecorView().findViewById(R.id.container);

        Fonts.setAllTextView(toolbar,ralewayRegular);


        TextView user_name = findViewById(R.id.display_name);
        TextView hastag = findViewById(R.id.hastag);
        TextView email = findViewById(R.id.email);
        nonScrollListView = findViewById(R.id.listview);
        adapter = new ParamAdapter(this,paramsObjects);
        nonScrollListView.setAdapter(adapter);
        UserInfos userInfos;


            user_name.setTypeface(ralewaybold);
            hastag.setTypeface(ralewayitalic);
            email.setTypeface(ralewayRegular);

        myPreferences =new MyPreferences(this);
        userInfos=myPreferences.readUserInfos();

        user_name.setText(userInfos.getDisplay_name());
        hastag.setText(userInfos.getLogin_name());
        email.setText(userInfos.getEmail());

        boolean isAudio = myPreferences.isAudioConversion();

                paramsObjects.add(new paramsObject("Mon Profil", getResources().getDrawable(R.drawable.ic_person_black_blue_24dp), "my_profile"));
                //paramsObjects.add(new paramsObject("Notification", getResources().getDrawable(R.drawable.ic_notifications_black_24dp), true, "notification"));
                paramsObjects.add(new paramsObject("Langue", getResources().getDrawable(R.drawable.ic_flag_black_24dp), "language"));
                paramsObjects.add(new paramsObject("Controlleurs lecture audio", getResources().getDrawable(R.drawable.ic_skip_next_blue_24dp), true, isAudio, "audio"));
                paramsObjects.add(new paramsObject("Inviter des amis", getResources().getDrawable(R.drawable.ic_people_black_24dp), "invite"));
                paramsObjects.add(new paramsObject("Aide", getResources().getDrawable(R.drawable.ic_help_outline_black_24dp),"help"));
                paramsObjects.add(new paramsObject("Déconnexion", getResources().getDrawable(R.drawable.ic_settings_power_black_24dp), "signout"));

            if (!myPreferences.readUserInfos().isFacebook_login()) {
                paramsObjects.add(new paramsObject("Supprimer mon compte", getResources().getDrawable(R.drawable.ic_warning_black_24dp), "delete_account"));
            }


            adapter.notifyDataSetChanged();



          nonScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter.OnClickListenner(paramsObjects.get(position).getSlug());
                }
            });

        File file = new File(getFilesDir(),"Pictures/");
        String name = "profile_picture.jpg";

        File pic = new File(file, name);

         // profile_picture.setImageURI(Uri.fromFile(pic));


          final ImageRequest imageRequest = new ImageRequest(userInfos.getPictureUrl(), new Response.Listener<Bitmap>(){
            @Override
            public void onResponse(Bitmap response) {
                    profile_picture.setImageBitmap(response);
            }
        }, 200,200, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Settings.this, error.toString(), Toast.LENGTH_LONG).show();
                Log.d("Error Picture", error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(imageRequest);



        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Settings.this);
                    alertDialogBuilder.setTitle("Nouvelle photo de profil");
                    alertDialogBuilder.setMessage("Choisir depuis");

                    alertDialogBuilder.setPositiveButton("Galerie",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    imagePicker = new ImagePicker(Settings.this, null, new OnImagePickedListener() {
                                        @Override
                                        public void onImagePicked(Uri imageUri) {
                                            profile_picture.setImageURI(imageUri);
                                            uploaduserimage(imageUri);
                                        }
                                    }).setWithImageCrop(1,1);
                                    imagePicker.choosePicture(true);
                                }
                            });

                   alertDialogBuilder.setNegativeButton("Appareil photo",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                    imagePicker= new ImagePicker(Settings.this, null, new OnImagePickedListener() {
                                        @Override
                                        public void onImagePicked(Uri imageUri) {
                                            profile_picture.setImageURI(imageUri);
                                            uploaduserimage(imageUri);
                                        }
                                    }).setWithImageCrop(1,1);
                                    imagePicker.openCamera();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();*/

                imagePicker = new ImagePicker(Settings.this, null, new OnImagePickedListener() {
                    @Override
                    public void onImagePicked(Uri imageUri) {
                        profile_picture.setImageURI(imageUri);
                        uploaduserimage(imageUri);
                    }
                }).setWithImageCrop(1,1);
                imagePicker.choosePicture(true);

            }

        });




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.search:
                        Intent isearch_Activity = new Intent(getApplicationContext(), MainActivity.class);
                        isearch_Activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        isearch_Activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(isearch_Activity);
                        break;

                    case R.id.news:
                        Intent ilearn_Activity = new Intent(getApplicationContext(), ILearn.class);
                        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ilearn_Activity);
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



    public void uploaduserimage(final Uri imageUri){

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,15, baos);
        byte [] b=baos.toByteArray();
        final String temp= Base64.encodeToString(b, Base64.DEFAULT);

        imageLoader.setVisibility(View.VISIBLE);

        final MyPreferences myPreferences = new MyPreferences(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RemoteServer.User_Script_Picture, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject object = null;
                try {
                     object = new JSONObject(response);

                    if(object.getString("response").equals("done")){
                        profile_picture.setImageURI(imageUri);
                        Toast.makeText(getApplicationContext(), "Photo de profile mise è jour", Toast.LENGTH_LONG).show();
                    }else{
                        // exit
                        Toast.makeText(getApplicationContext(), "Une erreur s'est produite", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();
                }

                imageLoader.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Problème de connexion veuillez réessayer", Toast.LENGTH_LONG).show();
                imageLoader.setVisibility(View.INVISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("id",myPreferences.readUserID());
                param.put("login",myPreferences.readUserInfos().getLogin_name());
                param.put("token",myPreferences.readUserInfos().getUser_hash());
                param.put("image",temp);
                return param;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_params, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

            switch(id){

                case R.id.action_settings:
                    Intent intent = new Intent(this, Profile.class);
                    startActivity(intent);
                    break;
            }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }


}
