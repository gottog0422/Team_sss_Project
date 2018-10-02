package com.example.sss.team_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sss.team_project.retrofit.RetrofitService;
import com.example.sss.team_project.utills.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentModifyActivity extends AppCompatActivity {
    @BindView(R.id.bt_back)
    Button bt_back;
    @BindView(R.id.bt_modify)
    TextView bt_modify;
    @BindView(R.id.et_comment)
    EditText et_comment;

    String str;
    Long comment_id;
    Boolean b;

    final static int MODIFYOK = 2323;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_modify);
        ButterKnife.bind(this);

        final int color_gray = getResources().getColor(R.color.black_gray);
        final int color_white = getResources().getColor(R.color.white);

        Intent getIntent = getIntent();
        str = getIntent.getStringExtra("comment");
        comment_id = getIntent.getLongExtra("comment_id", 0);

        et_comment.setText(str);

        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_comment.getText().toString().equals("")) {
                    b = false;
                    bt_modify.setTextColor(color_gray);
                } else {
                    b = true;
                    bt_modify.setTextColor(color_white);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @OnClick(R.id.bt_modify)
    public void modify() {
        if (b) {
            String s = et_comment.getText().toString();
            Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().modifyComment(comment_id, s);
            observ.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    setResult(MODIFYOK);
                    finish();
                    overridePendingTransition(R.anim.anim_not_move, R.anim.anim_right_out);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }

    @OnClick(R.id.bt_back)
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CommentModifyActivity.this);
        dialog.setTitle("댓글 수정 취소").setMessage("정말 취소 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_right_out);
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).create().show();
    }
}
