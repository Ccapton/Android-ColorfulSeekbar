package com.capton.colorfulseekbardemo;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.capton.colorfulseekbar.ColorfulProgressbar;
import com.capton.colorfulseekbar.ColorfulSeekbar;


public class MainActivity extends AppCompatActivity implements ColorfulSeekbar.OnSeekBarChangeListener{

    ColorfulSeekbar mySeekbar;
    ColorfulSeekbar mySeekbar2;
    ColorfulProgressbar progressbar;
    VideoView videoView;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mySeekbar.setProgress(msg.what);
            if(msg.what==mySeekbar.getMaxProgress()){
                prepared=false;
            }
            super.handleMessage(msg);
        }
    };
    private boolean prepared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySeekbar=((ColorfulSeekbar)findViewById(R.id.myseekbar));
        mySeekbar2=((ColorfulSeekbar)findViewById(R.id.myseekbar2));


        mySeekbar.setOnSeekBarChangeListener(this);
        mySeekbar2.setOnSeekBarChangeListener(this);

        videoView= (VideoView) findViewById(R.id.videoview);
        videoView.setVideoURI(Uri.parse("http://192.168.1.111/bolange.mp4"));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mySeekbar.setMaxProgress(mp.getDuration());
                prepared=true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (prepared) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(videoView.getCurrentPosition());
                        }
                    }
                }).start();
            }
        });
        videoView.start();

    }

    @Override
    public void onProgressChanged(ColorfulSeekbar seekBar, int progress, boolean fromUser) {
        if(seekBar==mySeekbar2) {
            if(progress>=0&&progress<10){
                seekBar.setThumbText("这");
            }else if(progress>=10&&progress<20){
                seekBar.setThumbText("里");
            }else if(progress>=20&&progress<30){
                seekBar.setThumbText("可");
            }else if(progress>=30&&progress<40){
                seekBar.setThumbText("添");
            }else if(progress>=40&&progress<50){
                seekBar.setThumbText("加");
            }else if(progress>=50&&progress<60){
                seekBar.setThumbText("文");
            }else if(progress>=60&&progress<70){
                seekBar.setThumbText("字");
            }else if(progress>=70&&progress<80){
                seekBar.setThumbText("提");
            }else if(progress>=80&&progress<90){
                seekBar.setThumbText("示");
            }else if(progress>=90&&progress<100){
                seekBar.setThumbText("哦");
            }
        }
    }

    @Override
    public void onStartTrackingTouch(ColorfulSeekbar seekBar) {
        if(seekBar==mySeekbar) {
            videoView.pause();
        }
    }

    @Override
    public void onStopTrackingTouch(ColorfulSeekbar seekBar) {
        if(seekBar==mySeekbar) {
            prepared=true;
            videoView.seekTo((int) seekBar.getProgress());
            videoView.start();
        }
    }
}
