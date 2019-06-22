package com.smookcreative.icare;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smookcreative.icare.Adapters.UserInfos;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.ILearn.ILearn;
import com.smookcreative.icare.OtherActivities.ReportIssues;
import com.smookcreative.icare.Tips.Fonts;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class AboutActivity extends AppCompatActivity {
    private MyPreferences myPreferences;
    private BottomNavigationView bottomNavigationView;
    private final String smookcreative="https://smookcreative.com";
    private final String icare_policy="https://icare.smookcreative.com/politique-de-confidentialite";
    private Menu menu;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_layout);

        //initialise bottom menu
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        menu=bottomNavigationView.getMenu();
        menuItem= menu.getItem(3);
        menuItem.setChecked(true);


        //Set Typefaces
        Typeface ralewayRegular=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayRegular));
        Typeface ralewaybold=Typeface.createFromAsset(getAssets(),getString(R.string.ralewayBold));
        ViewGroup group =  getWindow().getDecorView().findViewById(R.id.foreground);
        Fonts.setAllTextView(group,ralewayRegular);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.news:
                        LaunchILearnActivity();
                        finish();
                        break;

                    case R.id.search:
                        Intent ilearn_Activity = new Intent(getApplicationContext(), MainActivity.class);
                        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ilearn_Activity);
                        finish();
                        break;

                    case R.id.settings:
                        Intent settings_Activity = new Intent(getApplicationContext(), Settings.class);
                        settings_Activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        settings_Activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(settings_Activity);
                        finish();
                        break;

                }

                return true;
            }
        });

        final Button don = findViewById(R.id.actionButton);
        TextView userId = findViewById(R.id.userid);
        TextView version =findViewById(R.id.version);
        TextView copyright =findViewById(R.id.copyright);
       // TextView issues =findViewById(R.id.issues);
        TextView legal =findViewById(R.id.legals);
        TextView accountType = findViewById(R.id.accountType);
        final Button button = findViewById(R.id.actionButton);

            myPreferences = new MyPreferences(this);
            UserInfos userInfos = myPreferences.readUserInfos();
                userId.setText(userInfos.getLogin_name());
                accountType.setText(userInfos.getAccount_type());

                 version.setText(String.format("Version %s", BuildConfig.VERSION_NAME));


        //rate the application





                copyright.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            Intent showWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse(smookcreative));
                            startActivity(showWebsite);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Erreur aucun navigateur installer sur votre appareil", Toast.LENGTH_LONG).show();
                        }

                    }
                });


                legal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            Intent showWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse(icare_policy));
                            startActivity(showWebsite);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Erreur aucun navigateur installer sur votre appareil", Toast.LENGTH_LONG).show();
                        }
                    }
                });


        final ImageView logo = findViewById(R.id.profile);
            button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent issueActivity = new Intent(AboutActivity.this, ReportIssues.class);
                            startActivity(issueActivity);

                    }
                });

    }




    private void LaunchILearnActivity(){
        Intent ilearn_Activity = new Intent(getApplicationContext(), ILearn.class);
        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ilearn_Activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(ilearn_Activity);
    }



    @Override
    protected void onResume() {
        super.onResume();
        menuItem = menu.getItem(3);
        menuItem.setChecked(true);
    }
    @Override
    protected void onRestart() {

        super.onRestart();
        menuItem = menu.getItem(3);
        menuItem.setChecked(true);
    }


}
