package com.smookcreative.icare;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.ILearn.ILearn;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

public class Launcher extends AppCompatActivity {

    private MyPreferences myPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

    /*
        if (prefs.termsAndConditionsAccepted) {
            val fabric = new Fabric.Builder(this)
                    .kits(Crashlytics())
                    .debuggable(BuildConfig.DEBUG) // Enables Crashlytics debugger
                    .build();
            Fabric.with(fabric)
        }
    */


        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.smookcreative.icare",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        final Intent intent = new Intent(this, Login.class);

        final Intent intent2;

        myPreferences = new MyPreferences(getApplicationContext());
        boolean status = myPreferences.readLoginStatus();
        if(!status){
            intent2= new Intent(this, Login.class);
        }else{
            intent2= new Intent(this, ILearn.class);
        }

        Thread splashpage = new Thread() {

            public void run() {

                try {
                    int splashtime = 0;
                    while (splashtime < 5000) {
                        sleep(100);
                        splashtime = splashtime + 100;
                    }

                   // startActivity(intent);
                    startActivity(intent2);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }


            }
        };

        splashpage.start();




    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}