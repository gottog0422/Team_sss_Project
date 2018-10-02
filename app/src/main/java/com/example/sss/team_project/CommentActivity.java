package com.example.sss.team_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.sss.team_project.adapter.CommentAdapter;
import com.example.sss.team_project.bus.BusProvider;
import com.example.sss.team_project.customdialog.Dialog_comment;
import com.example.sss.team_project.customdialog.Dialog_member_info;
import com.example.sss.team_project.model.CommentFile;
import com.example.sss.team_project.retrofit.RetrofitService;
import com.example.sss.team_project.utills.PreferenceUtil;
import com.squareup.otto.Bus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    @BindView(R.id.ll_no_comment)
    LinearLayout ll_no_comment;
    @BindView(R.id.lv_comment)
    ListView lv_comment;
    @BindView(R.id.et_comment)
    EditText et_comment;
    @BindView(R.id.bt_comment_add)
    Button bt_comment_add;
    @BindView(R.id.bt_back)
    Button bt_back;

    Bus bus = BusProvider.getInstance().getBus();

    ArrayList<CommentFile> items = new ArrayList<>();

    CommentAdapter commentAdapter;

    String id;
    Long board_id;
    String board_writer_id;

    final static int MODIFY_COMMENT = 5454;
    final static int MODIFYOK = 2323;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        bus.register(this);


        id = PreferenceUtil.getInstance(this).getStringExtra("id");

        Intent getintent = getIntent();
        board_id = getintent.getLongExtra("board_id", 0);
        board_writer_id = getintent.getStringExtra("board_writer_id");

        commentAdapter = new CommentAdapter(items, id,board_writer_id,this);
        lv_comment.setAdapter(commentAdapter);

        getCommentFile();
    }

    public void getCommentFile() {
        Call<ArrayList<CommentFile>> observ = RetrofitService.getInstance().getRetrofitRequest().getComment(board_id);
        observ.enqueue(new Callback<ArrayList<CommentFile>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentFile>> call, Response<ArrayList<CommentFile>> response) {
                if (response.isSuccessful()) {
                    items.clear();
                    items.addAll(response.body());

                    if (items.size() == 0) {
                        ll_no_comment.setVisibility(View.VISIBLE);
                    } else {
                        ll_no_comment.setVisibility(View.GONE);
                    }

                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommentFile>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.bt_comment_add)
    public void add_comment() {
        String str = et_comment.getText().toString();
        if (!str.equals("")) {
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            long mNow = System.currentTimeMillis();
            Date mDate = new Date(mNow);
            final String date = mFormat.format(mDate);


            Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().writeComment(board_id, id, str, date);
            observ.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    et_comment.setText("");

                    getCommentFile();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }

    @OnItemClick(R.id.lv_comment)
    public void comment_click(AdapterView<?> parent, final int i) {
        Boolean b;
        if (id.equals(items.get(i).getMember_id())) {
            b = true;
        } else {
            b = false;
        }

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비

        final Dialog_comment dialog_comment = new Dialog_comment(this, b);
        dialog_comment.setCallBack(new Dialog_comment.CallBack() {
            @Override
            public void onClickBt0() {

            }

            @Override
            public void onClickBt1() {
                get_memberInfo(items.get(i).getMember_id());
                dialog_comment.dismiss();
            }

            @Override
            public void onClickBt2() {
                Intent intent = new Intent(CommentActivity.this, CommentModifyActivity.class);
                intent.putExtra("comment", items.get(i).getComment());
                intent.putExtra("comment_id", items.get(i).getId());
                dialog_comment.dismiss();
                startActivityForResult(intent, MODIFY_COMMENT);
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
            }

            @Override
            public void onClickBt3() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CommentActivity.this);
                dialog.setTitle("댓글 삭제").setMessage("정말 삭제 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int a) {

                                Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().delComment(items.get(i).getId());
                                observ.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        getCommentFile();
                                        dialog_comment.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        }).create().show();
            }
        });

        WindowManager.LayoutParams wm = dialog_comment.getWindow().getAttributes();
        wm.copyFrom(dialog_comment.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width - (width / 4);  //화면 너비의 절반

        dialog_comment.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MODIFY_COMMENT) {
            if (resultCode == MODIFYOK) {
                getCommentFile();
            }
        }
    }



    @OnClick(R.id.bt_back)
    public void go_back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_right_out);
    }

    public void get_memberInfo(String member_id) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비

        final Dialog_member_info dialog_member_info = new Dialog_member_info(this, member_id, id);

        dialog_member_info.setCallBack(new Dialog_member_info.CallBack() {
            @Override
            public void onClick_send(String member_id) {
                Log.d("ksg", member_id);
            }

            @Override
            public void onClick_close() {
                dialog_member_info.dismiss();
            }
        });

        WindowManager.LayoutParams wm = dialog_member_info.getWindow().getAttributes();
        wm.copyFrom(dialog_member_info.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width - (width / 6);  //화면 너비의 절반

        dialog_member_info.show();
    }
}
