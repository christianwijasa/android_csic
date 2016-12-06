package xyz.christianwijasa.tutorial;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class MateriActivity extends AppCompatActivity {

    private int position = 1;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        Button buttonPlay = (Button) findViewById(R.id.buttonPlay);
        final Button buttonPause = (Button) findViewById(R.id.buttonPause);
        Button buttonStop = (Button) findViewById(R.id.buttonStop);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        final VideoView video = (VideoView) findViewById(R.id.video);

        String uriPath = "android.resource://xyz.christianwijasa.tutorial/"+R.raw.movie;
        final Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();

        buttonPlay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                video.setVideoURI(uri);
                video.start();
            }
        });

        buttonPause.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = video.getCurrentPosition();
                if (video.isPlaying()) {
                    video.pause();
                    video.seekTo(currentPosition);
                    buttonPause.setText("Resume");
                }
                else if (buttonPause.getText().toString().trim().equals("Resume")) {
                    video.start();
                    buttonPause.setText("Pause");
                }
            }
        });

        buttonStop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                video.stopPlayback();
                buttonPause.setText("Pause");
            }
        });
    }
}
