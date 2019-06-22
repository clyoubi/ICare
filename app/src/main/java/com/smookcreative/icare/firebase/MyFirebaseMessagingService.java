package com.smookcreative.icare.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smookcreative.icare.Adapters.ArticleObject;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.DatabasesManagers.Singleton;
import com.smookcreative.icare.ILearn.ArticleDetail;
import com.smookcreative.icare.ILearn.ILearn;
import com.smookcreative.icare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private NotificationManager notificationManager;
    int notificationId;
    String post_id="1", SRC="i_care", TITLE, MESSAGE;
    ArticleObject art;
    Intent notificationIntent;
    NotificationCompat.Builder notificationBuilder;
    Bitmap bitmap;
    PendingIntent pendingIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        post_id = remoteMessage.getData().get("post_id");
        SRC =remoteMessage.getData().get("src");
        TITLE =remoteMessage.getData().get("title");
        MESSAGE =remoteMessage.getData().get("message");

        //notificationIntent = new Intent(this, ArticleDetail.class);
        notificationIntent = new Intent("com.smookcreative.icare.ArticleDetail");
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //LoadDatas();

        if(ILearn.isAppRunning){
            notificationIntent = new Intent(this, ArticleDetail.class);
        }else{
            notificationIntent = new Intent(this, ArticleDetail.class);
        }
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);


        //Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("image-url")); //obtain the image

        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }


        notificationId = new Random().nextInt(60000);

       Intent likeIntent = new Intent(this,LikeService.class);
            likeIntent.putExtra("NOTIFICATION_ID_EXTRA",notificationId);
            likeIntent.putExtra("IMAGE_URL_EXTRA",remoteMessage.getData().get("image-url"));
            PendingIntent likePendingIntent = PendingIntent.getService(this,notificationId+1,likeIntent,PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


         notificationBuilder = new NotificationCompat.Builder(this, "ADMIN_CHANNEL_ID")
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.i_care_launcher)  //a resource for your custom small icon
                .setContentTitle(remoteMessage.getData().get("title")) //the "title" value you sent in your notification
                .setContentText(remoteMessage.getData().get("message")) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
                //.setStyle(new NotificationCompat.BigPictureStyle()
                  //      .setSummaryText(remoteMessage.getData().get("message"));
                        //.bigPicture(bitmap));
                //.addAction(R.drawable.like_blue,
                  //      "J'aime",likePendingIntent); //Setting the action;


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, notificationBuilder.build());



    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel("ADMIN_CHANNEL_ID", adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }


    private void InitNoficationBuilder(){

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder = new NotificationCompat.Builder(this, "ADMIN_CHANNEL_ID")
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.i_care_launcher)  //a resource for your custom small icon
                .setContentTitle("A lire") //the "title" value you sent in your notification
                .setContentText(TITLE) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setSummaryText(MESSAGE)
                        .bigPicture(bitmap));
                //.addAction(R.drawable.like_blue,
                //        "J'aime",likePendingIntent); //Setting the action;

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }


    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error Firebase Image", e.toString());
            return null;
        }
    }



   /* public void LoadDatas(){

        String URLRequest, params = "";

        switch (SRC){

            case "i_care":
                URLRequest= RemoteServer.ILearn_News_Script+"?args=id,"+post_id;
                break;

            case "actu_cameroun":
                URLRequest= RemoteServer.ILearn_Alerts_Script+"?args=id,"+post_id;
                break;

            default:
                URLRequest= RemoteServer.ILearn_News_Script+"?args=id,"+post_id;
        }


        StringRequest movieReq = new StringRequest(Request.Method.GET, URLRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject code=jsonArray.getJSONObject(0);

                            if(code.getString("status").equals("found")){

                                    JSONObject obj = jsonArray.getJSONObject(1);
                                    art =  new ArticleObject();

                                    art.setId(obj.getString("id"));
                                    art.setTitle(obj.getString("title"));
                                    art.setBanner(obj.getString("thumbnail"));
                                    art.setPostOn(obj.getString("post_since"));
                                    art.setSource(obj.getString("source"));
                                    art.setAuthor(obj.getString("author_name"));
                                    art.setContent(obj.getString("content"));
                                    art.setLink(obj.getString("link"));
                                    art.setCredit_photo(obj.getString("credits"));
                                    art.setCategories(obj.getString("category"));
                                    art.setComment_count(obj.getString("comments_count"));

                                    notificationIntent.putExtra("article", art);
                                    bitmap = getBitmapfromUrl(art.getBanner());
                                    InitNoficationBuilder();

                            }else{
                                //Toast.makeText(getContext(), "Not found", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        Singleton.getInstance(getApplicationContext()).addToRequestqueue(movieReq);

    }*/


}

