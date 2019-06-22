package com.smookcreative.icare.ILearn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.smookcreative.icare.Adapters.ArticleObject;
import com.smookcreative.icare.Adapters.CommentsAdapter;
import com.smookcreative.icare.Adapters.CommentsObject;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.DatabasesManagers.RemoteServer;
import com.smookcreative.icare.DatabasesManagers.Singleton;
import com.smookcreative.icare.Login;
import com.smookcreative.icare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class Comments extends AppCompatActivity {
    private String id;
    private ArrayList<CommentsObject> comments =new ArrayList<>();
    private CommentsAdapter adapter;
    private ProgressDialog pDialog;
    private ListView listView;
    private Toolbar toolbar;
    private boolean contextual=false;
    private MyPreferences myPreferences;
    private String userID;
    FloatingActionButton fab;
    private Menu menu;
    TextView reply_to;
    String reply_to_name, reply_to_content, edit_comment;
    boolean replyMode=false;
    String parent_post_id = "0";
    String parent_id = "0";
    private ProgressBar progressBar;
    private String user_mail, user_name;
    private TextView commentText;
    String URL=RemoteServer.ILearn_Comments_Script;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab =(FloatingActionButton)findViewById(R.id.send);
        progressBar =findViewById(R.id.progress_bar);
        commentText =findViewById(R.id.comment_text);

        listView = findViewById(R.id.listView);
        adapter = new CommentsAdapter(getApplicationContext(), comments);
        listView.setAdapter(adapter);
        Intent i = getIntent();
        id=i.getStringExtra("id");
        reply_to = findViewById(R.id.reply_to);

        if(getIntent().getBooleanExtra("booking", false)){
            URL = RemoteServer.ISearch_Booking_Comments_Script;
        }

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                comments.clear();
                LoadComments(id); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        LoadComments(id);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibe != null) {
                    vibe.vibrate(200); // 50 is time in ms
                }

                menu.clear();
                getMenuInflater().inflate(R.menu.contextual_comment, menu);
                String title = "";
                toolbar.setTitle(title);
                contextual = true;
                reply_to_name=comments.get(position).getName();
                reply_to_content=comments.get(position).getComment();
                edit_comment=comments.get(position).getComment();
                parent_id =comments.get(position).getId();

                if(!comments.get(position).getAuthor_email().equals(myPreferences.readUserInfos().getEmail())){
                    menu.removeItem(R.id.delete);
                }

                return true;
            }
        });

        myPreferences = new MyPreferences(getApplicationContext());
        userID = myPreferences.readUserID();
        user_name = myPreferences.readUserInfos().getDisplay_name();
        user_mail = myPreferences.readUserInfos().getEmail();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(commentText.getText().toString().matches(""))
                    Toast.makeText(v.getContext(),"Veuillez écrire quelque chose", Toast.LENGTH_LONG).show();
                else{
                    writeComment(commentText.getText().toString(),id);
                    fab.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                }

            }
        });

    }


    public void close(){
        if(!contextual)
            finish();
        else{
            Menu menu=toolbar.getMenu();
            menu.clear();
            getMenuInflater().inflate(R.menu.main_menu, menu);
            toolbar.setTitle("Commentaires");
            contextual=false;
            if(replyMode){
                reply_to.setVisibility(View.INVISIBLE);
                replyMode=false;
                parent_post_id ="0";
                parent_id = "0";
            }
        }
    }


    @Override
    public void onBackPressed() {
        close();
    }

    private void LoadComments(final String id){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Chargemment...");
        pDialog.show();
        String url = URL+"?id="+id;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidePDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject code=jsonArray.getJSONObject(0);

                    if(code.getString("response").equals("found")){

                        for (int i=1; i<jsonArray.length();i++){

                            JSONObject obj = jsonArray.getJSONObject(i);
                            CommentsObject com =  new CommentsObject();
                            com.setId(obj.getString("ID"));
                            com.setComment(obj.getString("comment"));
                            com.setName(obj.getString("author"));
                            com.setPost_date(obj.getString("postOn"));
                            com.setAuthor_id(obj.getString("user_id"));
                            com.setAuthor_email(obj.getString("author_email"));
                            comments.add(com);

                                JSONArray array = obj.getJSONArray("replies");
                                    if(array.length()>0){

                                        for(int j=0; j<array.length(); j++){
                                            JSONObject object = array.getJSONObject(j);
                                            CommentsObject commentsObject =  new CommentsObject();
                                            commentsObject.setId(object.getString("comment_ID"));
                                            commentsObject.setComment(object.getString("comment_content"));
                                            commentsObject.setName(object.getString("comment_author"));
                                            commentsObject.setPost_date(object.getString("comment_date"));
                                            commentsObject.setAuthor_id(object.getString("user_id"));
                                            commentsObject.setAuthor_email(object.getString("comment_author_email"));
                                            commentsObject.setReply(true);
                                            comments.add(commentsObject);
                                        }

                                    }

                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "Pas de Commentaires", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                Snackbar.make(listView,"Probleme de connexion au serveur veuillez réessayer",2000).show();
            }
        });
        Singleton.getInstance(Comments.this).addToRequestqueue(jsonObjectRequest);
    }



    public void writeComment(final String comment, final String postId){

        String request = URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, request, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                comments.clear();
               // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                LoadComments(id);
                fab.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                fab.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("comment",comment);
                params.put("userid",userID);
                params.put("postid",postId);
                params.put("email",user_mail);
                params.put("name",user_name);
                params.put("parent",parent_id);
                return params;
            }
        };

        Singleton.getInstance(Comments.this).addToRequestqueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            close();
        }

        if(id==R.id.delete){
            //detelete comment
            DeleteComment();
        }

        if(id==R.id.reply){
            //reply to a comment
            replyMode=true;
            reply_to.setVisibility(View.VISIBLE);
            reply_to.setText(String.format("Répondre à %s@%s", reply_to_name,reply_to_content));
            parent_post_id =parent_id;
        }

        if(id==R.id.edit){
           // commentText.setText(edit_comment);
        }

        return true;
    }



    private void DeleteComment() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("Suppression");
            alertDialogBuilder.setMessage("Voulez-vous vraiment supprimer le commentaire: "+reply_to_content);
                alertDialogBuilder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, RemoteServer.ILearn_Delete_Comment_Script, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                comments.clear();
                                LoadComments(id);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new Hashtable<String, String>();
                                params.put("id", parent_id);
                                params.put("email", myPreferences.readUserInfos().getEmail());
                                params.put("token", myPreferences.readUserInfos().getUser_hash());
                                return params;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(stringRequest);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }

    }

}
