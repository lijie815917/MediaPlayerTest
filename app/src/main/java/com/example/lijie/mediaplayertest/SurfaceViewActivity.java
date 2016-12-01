package com.example.lijie.mediaplayertest;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jarly
 * Time :16/11/29
 * Description:
 * contact:13710397702&&848287246@qq.com
 */

public class SurfaceViewActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    @BindView (R.id.surface_view) MySurfaceVew surfaceView;
    private static MediaPlayer mediaPlayer;
    private String str = "http://www.beiletech.com/resource/L.mp4";
    private int posi = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);
        ButterKnife.bind(this);
        init();
    }

    @OnClick (R.id.btn)
    public void btnClick() {
        startActivity(new Intent(SurfaceViewActivity.this, TestActivity.class));
    }

    private void init() {
        surfaceView.getHolder().addCallback(this);
        surfaceView.getHolder().setKeepScreenOn(true);
        //设置透明
        surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    //在线url
    private void playVideo(String str) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(this, Uri.parse(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setDisplay(surfaceView.getHolder());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
//                int height = mediaPlayer.getVideoHeight();
//                int width = mediaPlayer.getVideoWidth();
//                surfaceView.setVideoSize(new Point(width, height));
                //去除上面三句将全屏显示
                mp.seekTo(posi);
                mp.start();
            }
        });
    }

    //本地url

    private void playLocalVideo() {
        try {
            AssetFileDescriptor fd = null;
            fd = this.getAssets().openFd("test.mp4");
            try {
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
                        fd.getLength());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //                    int height = mediaPlayer.getVideoHeight();
                    //                    int width = mediaPlayer.getVideoWidth();
                    //                    surfaceView.setVideoSize(new Point(width,height));

                    //去除上面三句将全屏显示
                    mp.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //播放线上url
        playVideo(str);
        //播放线下url
        //        playLocalVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        posi = mediaPlayer.getCurrentPosition();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
