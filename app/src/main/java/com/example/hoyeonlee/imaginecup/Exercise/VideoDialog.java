package com.example.hoyeonlee.imaginecup.Exercise;

import android.os.Bundle;

import com.example.hoyeonlee.imaginecup.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by hoyeonlee on 2018. 3. 23..
 */

public class VideoDialog extends YouTubeBaseActivity{
    ExerciseActivity context;
    private String url = "";
    YouTubePlayerView youTubeView;
    YouTubePlayer.OnInitializedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_video);
        youTubeView = (YouTubePlayerView)findViewById(R.id.youtubeView);
        url = getIntent().getStringExtra("url");
        listener = new YouTubePlayer.OnInitializedListener(){
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(url);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youTubeView.initialize("AIzaSyAUpdF-wFmOmoWqiZ_UYX54BRQSuFqktKI",listener);
    }
}
