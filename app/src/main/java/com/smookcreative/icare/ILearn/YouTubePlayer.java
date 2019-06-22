package com.smookcreative.icare.ILearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerView;
import com.smookcreative.icare.Adapters.ArticleObject;
import com.smookcreative.icare.R;

public class YouTubePlayer extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;
    private final String API_KEY="AIzaSyDUq_vGH-qQcSow1qD80aFDRhqvJNXAKXs";
    private String link="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubeplayer);

        youTubePlayerView= (YouTubePlayerView)findViewById(R.id.player);

        Intent i = getIntent();
       final String code = i.getStringExtra("youtube_code");
       link = i.getStringExtra("link");

        youTubePlayerView.initialize(API_KEY, new com.google.android.youtube.player.YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(com.google.android.youtube.player.YouTubePlayer.Provider provider, com.google.android.youtube.player.YouTubePlayer youTubePlayer, boolean b) {
                if(!b){
                    youTubePlayer.loadVideo(code);
                    //youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.setFullscreen(true);
                    youTubePlayer.setManageAudioFocus(true);

                }
            }

            @Override
            public void onInitializationFailure(com.google.android.youtube.player.YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }

        if(id==R.id.share){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT,link);

            startActivity(Intent.createChooser(i,"Partager via"));
        }

        return true;
    }
}
