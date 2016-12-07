package com.example.lijie.mediaplayertest;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jarly
 * Time :16/11/29
 * Description:
 * contact:13710397702&&848287246@qq.com
 */

public class TextureViewActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    @BindView (R.id.texture_view) MyTextureView textureView;
    @BindView (R.id.img) ImageView img;
    private MediaPlayer mediaPlayer;
    private Surface mSurface;
    private int posi = 0;
    private boolean isPause = false;
    MediaMetadataRetriever mediaMetadataRetriever;

    private Bitmap bitmap;
    private String str = "http://www.beiletech.com/resource/L.mp4";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj == null) {
                        mediaPlayer.setSurface(null);
                    } else {
                        Surface holder = (Surface) msg.obj;
                        if (holder.isValid()) {
                            mediaPlayer.setSurface(holder);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    img.setVisibility(View.GONE);
//                                    textureView.requestLayout();
                                }
                            });
                        }
                    }
                    break;
                case 2:
                    playVideo(str);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textureview);
        ButterKnife.bind(this);
        init();
    }

    @OnClick (R.id.btn)
    public void btnClick() {
//        bitmap = mediaMetadataRetriever.getFrameAtTime(mediaPlayer.getCurrentPosition() * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

        mediaPlayer.pause();
        startActivity(new Intent(TextureViewActivity.this, TestActivity.class));
    }

    private void init() {
        mediaPlayer = new MediaPlayer();
        //获取视频最后一帧，暂停返回不会黑屏
//        mediaMetadataRetriever = new MediaMetadataRetriever();
//        mediaMetadataRetriever.setDataSource(str,new HashMap<String, String>());
        textureView.setSurfaceTextureListener(this);
        Message msg = new Message();
        msg.what = 2;
        handler.sendMessage(msg);
    }

    //播放网上视频
    private void playVideo(String url) {
        mediaPlayer.release();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
                mediaPlayer.seekTo(posi);
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
        Message msg = new Message();
        msg.what = 1;
        msg.obj = mSurface;
        handler.sendMessage(msg);
        //        playVideo(str);
        //        playLocalVideo();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

        posi = mediaPlayer.getCurrentPosition();
        surface.release();
        return true;
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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            isPause = false;
            if (bitmap != null) {
                img.setVisibility(View.VISIBLE);
                img.setImageBitmap(bitmap);
            }
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        mediaPlayer.pause();
        bitmap = mediaMetadataRetriever.getFrameAtTime(mediaPlayer.getCurrentPosition() * 1000,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

    }
}
