package com.example.sss.team_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sss.team_project.retrofit.RetrofitService;
import com.example.sss.team_project.utills.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.et_login_id)
    EditText et_login_id;
    @BindView(R.id.et_login_pw)
    EditText et_login_pw;
    @BindView(R.id.bt_login)
    Button bt_login;
    @BindView(R.id.bt_signUp)
    Button bt_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_login)
    public void onclick_login() {
        final String member_id = et_login_id.getText().toString();
        String member_pw = et_login_pw.getText().toString();

        //Login 버튼 클릭시 server request ID PW 확인
        Call<Boolean> observ = RetrofitService.getInstance().getRetrofitRequest().login(member_id, member_pw);
        observ.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean login_suc = response.body();
                    if (login_suc) {
                        //로그인 성공 시 전역 preference ID값 저장 (자동로그인)
                        PreferenceUtil.getInstance(LoginActivity.this).putStringExtra("id", member_id);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else if (!login_suc) {
                        Toast.makeText(LoginActivity.this, "로그인 실패\n" +
                                "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("ksg", "서버연결실패");
            }
        });
    }

    @OnClick(R.id.bt_signUp)
    public void go_signUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);

    }
}
