package com.example.sss.team_project;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sss.team_project.retrofit.RetrofitService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    boolean c_id = false;
    boolean c_pw = false;
    boolean c_pw2 = false;
    boolean c_nick = false;

    @BindView(R.id.bt_signUp_back)
    Button bt_signUp_back;
    @BindView(R.id.bt_signUp_ok)
    Button bt_signUp_ok;
    @BindView(R.id.et_signUp_id)
    EditText et_signUp_id;
    @BindView(R.id.et_signUp_pw)
    EditText et_signUp_pw;
    @BindView(R.id.et_signUp_pw2)
    EditText et_signUp_pw2;
    @BindView(R.id.et_signUp_nick)
    EditText et_signUp_nick;
    @BindView(R.id.tv_notice_id)
    TextView tv_notice_id;
    @BindView(R.id.tv_notice_pw)
    TextView tv_notice_pw;
    @BindView(R.id.tv_notice_pw2)
    TextView tv_notice_pw2;
    @BindView(R.id.tv_notice_nick)
    TextView tv_notice_nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnFocusChange(R.id.et_signUp_id)
    public void onFocus_id(View v, boolean hasFocus) {
        if (!hasFocus) {
            check_id();
        }
    }

    @OnFocusChange(R.id.et_signUp_pw)
    public void onFocus_pw(View v, boolean hasFocus) {
        if (!hasFocus) {
            check_pw();
        }
    }

    @OnFocusChange(R.id.et_signUp_pw2)
    public void onFocus_pw2(View v, boolean hasFocus) {
        if (!hasFocus) {
            check_pw2();
        }
    }

    @OnFocusChange(R.id.et_signUp_nick)
    public void check_nick(View v, boolean hasFocus) {
        if (!hasFocus) {
            check_nick();
        }
    }

    @OnClick(R.id.bt_signUp_back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.bt_signUp_ok)
    public void onclick_signUp_ok() {
        //가입 완료 버튼 클릭시 edit text 요구사항 체크
        check_id();
        check_pw();
        check_pw2();
        check_nick();

        //체크 완료시 서버 request
        if (c_id && c_pw && c_pw2 && c_nick) {
            final String id = et_signUp_id.getText().toString();
            final String pw = et_signUp_pw.getText().toString();
            final String nick = et_signUp_nick.getText().toString();
            Call<ArrayList<Integer>> observ = RetrofitService.getInstance().getRetrofitRequest().checkoverlap(id, nick);
            observ.enqueue(new Callback<ArrayList<Integer>>() {
                @Override
                public void onResponse(Call<ArrayList<Integer>> call, Response<ArrayList<Integer>> response) {
                    //Json response (id,nick 중복검사)
                    ArrayList<Integer> check = response.body();

                    if (check.get(0) > 0) {
                        tv_notice_id.setTextColor(Color.RED);
                        tv_notice_id.setText("중복된 아이디입니다.");
                    }
                    if (check.get(1) > 0) {
                        tv_notice_nick.setTextColor(Color.RED);
                        tv_notice_nick.setText("중복된 닉네임입니다");
                    }

                    //모든 요구 사항 충족시 가입
                    if (check.get(0) == 0 && check.get(1) == 0) {
                        signUp_ok(id, pw, nick);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Integer>> call, Throwable t) {

                }
            });

        }


    }

    public void check_id() {
        c_id = false;
        String id = et_signUp_id.getText().toString();
        if (id.toString().length() == 0) {
            tv_notice_id.setTextColor(Color.RED);
            tv_notice_id.setText("필수 정보입니다.");
        } else {
            if (id.toString().length() < 5 || id.toString().length() > 15 || !id.matches("[0-9|a-z|]*")) {
                tv_notice_id.setTextColor(Color.RED);
                tv_notice_id.setText("5~15자의 영문 소문자, 숫자와 \n특수기호(_),(-)만 사용 가능합니다.");
            } else {
                tv_notice_id.setText("");
                c_id = true;
            }
        }

    }

    public void check_pw() {
        c_pw = false;

        String pw = et_signUp_pw.getText().toString();
        if (pw.length() == 0) {
            tv_notice_pw.setTextColor(Color.RED);
            tv_notice_pw.setText("필수 정보입니다.");
        } else {
            if (pw.length() < 6 || pw.length() > 16) {
                tv_notice_pw.setTextColor(Color.RED);
                tv_notice_pw.setText("6~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
            } else {
                tv_notice_pw.setText("");
                c_pw = true;
            }
        }
    }

    public void check_pw2() {
        c_pw2 = false;

        String pw = et_signUp_pw.getText().toString();
        String pw2 = et_signUp_pw2.getText().toString();
        if (pw2.length() == 0) {
            tv_notice_pw2.setTextColor(Color.RED);
            tv_notice_pw2.setText("필수 정보입니다.");
        } else {
            if (pw.equals(pw2)) {
                tv_notice_pw2.setText("");
                c_pw2 = true;
            } else if (!pw.equals(pw2)) {
                tv_notice_pw2.setTextColor(Color.RED);
                tv_notice_pw2.setText("비밀번호가 일치하지 않습니다.");
                et_signUp_pw2.setText("");
            }
        }
    }

    public void check_nick() {
        c_nick = false;

        String nick = et_signUp_nick.getText().toString();
        if (nick.length() == 0) {
            tv_notice_nick.setTextColor(Color.RED);
            tv_notice_nick.setText("필수 정보입니다.");
        } else {
            if (nick.length() < 2 || nick.length() > 10 || !nick.matches("[0-9|a-z|A-Z|가-힣|]*")) {
                tv_notice_nick.setTextColor(Color.RED);
                tv_notice_nick.setText("2~10자 한글, 영문 대 소문자, 숫자를 사용하세요");
                Log.d("ksg", "1");
            } else {
                tv_notice_nick.setText("");
                Log.d("ksg", "2");
                c_nick = true;
            }
        }

    }

    public void signUp_ok(String id, String pw, String nick) {
        Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().inputMember(id, pw, nick);
        observ.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }


}


