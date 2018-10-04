package com.example.sss.team_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.sss.team_project.utills.PreferenceUtil;

public class Logo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                String value = PreferenceUtil.getInstance(Logo.this).getStringExtra("id");

                if (value == null || value.equals("")) {
                    Intent intent = new Intent(Logo.this, LoginActivity.class);
                    startActivity(intent);
                    Logo.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
                    finish();
                } else {
                    Intent intent = new Intent(Logo.this, HomeActivity.class);
                    startActivity(intent);
                    Logo.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
                    finish();
                }

            }
        }, 800);
    }
}