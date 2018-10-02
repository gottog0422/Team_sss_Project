package com.example.sss.team_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sss.team_project.utills.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.bt_back)
    Button bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);


    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_right_out);

        super.onPause();
    }

    @OnClick(R.id.bt_back)
    public void back() {
        onBackPressed();
    }
}
