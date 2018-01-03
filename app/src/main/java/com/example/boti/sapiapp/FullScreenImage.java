package com.example.boti.sapiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

public class FullScreenImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_full_screen_image);

        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        GlideApp.with(this).load(image).into(imageView);
    }
}
