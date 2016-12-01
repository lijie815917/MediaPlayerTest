package com.example.lijie.mediaplayertest;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.width;

/**
 * Created by Jarly
 * Time :16/11/29
 * Description:
 * contact:13710397702&&848287246@qq.com
 */

public class TextureViewActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    @BindView (R.id.texture_view) MyTextureView textureView;
    private MediaPlayer mediaPlayer;
    private Surface mSurface;
    private String str = "http://www.beiletech.com/resource/L.mp4";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textureview);
        ButterKnife.bind(this);
    }

    @OnClick (R.id.btn)
    public void btnClick() {
        startActivity(new Intent(TextureViewActivity.this, TestActivity.class));
    }

    //播放网上视频
    private void playVideo(String url) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setSurface(mSurface);
        try {
            mediaPlayer.setDataSource(this, Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

//                int height = mediaPlayer.getVideoHeight();
//                int width = mediaPlayer.getVideoWidth();
//                textureView.setVideoSize(new Point(width, height));

                mediaPlayer.start();
            }
        });
    }

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
            //播放sd卡的本地视频
            //            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/open.mp4";
            //            mediaPlayer.setDataSource(path);

            mediaPlayer.setSurface(mSurface);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    int height = mediaPlayer.getVideoHeight();
                    int width = mediaPlayer.getVideoWidth();
                    textureView.setVideoSize(new Point(width, height));

                    mediaPlayer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        playVideo(str);
        //        playLocalVideo();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
    }

    @Override
    protected void onResume() {
        textureView.setSurfaceTextureListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        textureView.setSurfaceTextureListener(null);
        super.onPause();


    }
}
