package com.example.photosound;
 
import java.io.File;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.vn.R;
import android.widget.Button;
import android.widget.SeekBar;
 
public class PictureSoundActivity extends Activity {
 
    private Button buttonPlayStop;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    
    private final Handler handler = new Handler();
 
    // Here i override onCreate method.
    //
    // setContentView() method set the layout that you will see then
    // the application will starts
    //
    // initViews() method i create to init views components.
    @Override
    public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.activity_picture_sound);
            initViews();  
 
    }
 
    // This method set the setOnClickListener and method for it (buttonClick())
    private void initViews() {
        buttonPlayStop = (Button) findViewById(R.id.ButtonPlayStop);
        buttonPlayStop.setOnClickListener(new OnClickListener() {@Override public void onClick(View v) {buttonClick();}});
 
        //mediaPlayer = MediaPlayer.create(this, R);
        Uri fileUri=Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+File.separator+"music.mp3"));
        mediaPlayer = MediaPlayer.create(this, fileUri);
        //mediaPlayer = MediaPlayer.
        seekBar = (SeekBar) findViewById(R.id.SeekBar01);
        seekBar.setMax(mediaPlayer.getDuration());
        Log.d("maxsecond", Integer.toString(mediaPlayer.getDuration()));
        seekBar.setOnTouchListener(new OnTouchListener() {@Override public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                        return false; }
                });
 
    }
    
    public void startPlayProgressUpdater() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            
                if (mediaPlayer.isPlaying()) {
                        Runnable notification = new Runnable() {
                        public void run() {
                                startPlayProgressUpdater();
                                }
                    };
                    handler.postDelayed(notification,1000);
            }else{
                    mediaPlayer.pause();
                    buttonPlayStop.setText(getString(R.string.play_str));
                    seekBar.setProgress(0);
            }
    } 
 
    // This is event handler thumb moving event
    private void seekChange(View v){
            if(mediaPlayer.isPlaying()){
                    SeekBar sb = (SeekBar)v;
                        mediaPlayer.seekTo(sb.getProgress());
                }
    }
 
    // This is event handler for buttonClick event
    private void buttonClick(){
        if (buttonPlayStop.getText() == getString(R.string.play_str)) {
            buttonPlayStop.setText(getString(R.string.pause_str));
            try{
                    mediaPlayer.start();
                startPlayProgressUpdater(); 
            }catch (IllegalStateException e) {
                    mediaPlayer.pause();
            }
        }else {
            buttonPlayStop.setText(getString(R.string.play_str));
            mediaPlayer.pause();
        }
    }
}
