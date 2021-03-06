package com.example.project;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PageWelcome extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    //variable
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        setAnimationForScreen();

    }

    public void setAnimationForScreen() {
        // Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // hooks

        image = findViewById(R.id.img_logo);
        logo = findViewById(R.id.tv_title);
        slogan = findViewById(R.id.tv_under_title);

        // set animation
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        // chuyen trang
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(PageWelcome.this, Login.class);

                Pair[] pairs = new Pair[2];
                // view o day la image, string se la transitionname
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logo, "logo_text");
                // post first screen and second is pair
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(PageWelcome.this, pairs);
                startActivity(intent, options.toBundle());
                //truy???n animation v?? c??i intent
            }
        }, SPLASH_SCREEN);
    }
}