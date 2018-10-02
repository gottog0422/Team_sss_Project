package com.example.sss.team_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginChoiceActivity extends AppCompatActivity {
    @BindView(R.id.bt_login_type0)
    Button bt_login_type0;
    @BindView(R.id.bt_signUp)
    Button bt_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);
        ButterKnife.bind(this);
    }



    @OnClick(R.id.bt_login_type0)
    public void onclick_login_type0() {
        Intent intent = new Intent(LoginChoiceActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.bt_signUp)
    public void onclick_sign_up() {
        Intent intent = new Intent(LoginChoiceActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
