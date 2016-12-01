package com.example.lijie.mediaplayertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView (R.id.btn_surfaceview) Button btnSurfaceview;
    @BindView (R.id.btn_textureview) Button btnTextureview;
    @BindView (R.id.activity_main) LinearLayout activityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_surfaceview)
    public void btnSurface(){
        startActivity(new Intent(MainActivity.this,SurfaceViewActivity.class));
    }

    @OnClick(R.id.btn_textureview)
    public void btnTextureView(){
        startActivity(new Intent(MainActivity.this,TextureViewActivity.class));
    }
}
