package com.example.puru.pbplayer;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ToggleButton;
import android.widget.VideoView;

import java.util.ArrayList;

public class VedioActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    VideoView videoView;
    SensorManager sm1;
    Sensor ms;
    ToggleButton toggleButton;
    int position;
    ArrayList filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio);

        sm1=(SensorManager)getSystemService(SENSOR_SERVICE);
        ms=sm1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        toggleButton=(ToggleButton)findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener((View.OnClickListener)this);
        Intent intent = getIntent();
        final Bundle b = intent.getExtras();
        position = b.getInt("position");
        filePath = (ArrayList) b.getParcelableArrayList("list");

        /*if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }*/


        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse((String) filePath.get(position)));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        videoView.setMediaController(new MediaController(this));
        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm1.registerListener((SensorEventListener) VedioActivity.this,ms,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sm1.unregisterListener((SensorEventListener) VedioActivity.this);
    }

    public void next( )
    {
        videoView.stopPlayback();
        videoView.resume();
        Uri uri;
        /*try {
            sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        position = (position+1)%filePath.size();
        //uri = Uri.parse(filePath.get(position).toString());
        uri = (Uri.parse((String) filePath.get(position)));

        videoView.setVideoURI(Uri.parse((String) filePath.get(position)));
        videoView.start();
    }

    public void prev(){

        videoView.stopPlayback();
        videoView.resume();
        Uri uri;
        /*try {
            sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        position = (position-1)<0?filePath.size()-1:position-1;
        //uri = Uri.parse(filePath.get(position).toString());
        uri = (Uri.parse((String) filePath.get(position)));

        videoView.setVideoURI(Uri.parse((String) filePath.get(position)));
        videoView.start();
    }

    public void fwd()
    {
        videoView.seekTo(videoView.getCurrentPosition()+5000);
    }
    public void rwd()
    {
        videoView.seekTo(videoView.getCurrentPosition()-5000);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(toggleButton.isChecked()) {
            float[] v = event.values;
            int x = (int) v[0];
            int y = (int) v[1];
            if (x < -5) {
                next();
            } else if (x > 5) {
                prev();
            }
            if (y > 5) {
                fwd();
            } else if (y < -5) {
                rwd();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {

    }
}
