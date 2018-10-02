package com.example.sss.team_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sss.team_project.adapter.MessageAdapter;
import com.example.sss.team_project.customdialog.Dialog_member_info;
import com.example.sss.team_project.customdialog.Dialog_message;
import com.example.sss.team_project.customdialog.Dialog_searchUser;
import com.example.sss.team_project.customdialog.Dialog_send_message;
import com.example.sss.team_project.model.MessageFile;
import com.example.sss.team_project.retrofit.RetrofitService;
import com.example.sss.team_project.utills.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    @BindView(R.id.bt_back)
    Button bt_back;

    @BindView(R.id.tv_title_title)
    TextView tv_title_title;
    @BindView(R.id.lv_message)
    ListView lv_message;

    @BindView(R.id.tv_change_below_message)
    TextView tv_change_below_message;

    @BindView(R.id.ll_refresh)
    LinearLayout ll_refresh;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_change)
    LinearLayout ll_change;
    @BindView(R.id.ll_search_member)
    LinearLayout ll_search_member;

    int type;
    String id;

    MessageAdapter messageAdapter;
    ArrayList<MessageFile> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        id = PreferenceUtil.getInstance(MessageActivity.this).getStringExtra("id");
        type = intent.getIntExtra("type", 0);

        setTitle();

        messageAdapter = new MessageAdapter(items, this);
        lv_message.setAdapter(messageAdapter);

        get_Message();
    }

    public void setTitle() {
        if (type == 0) {
            tv_title_title.setText("받은 쪽지함");
            tv_change_below_message.setText("보낸 쪽지함");
        } else {
            tv_title_title.setText("보낸 쪽지함");
            tv_change_below_message.setText("받은 쪽지함");
        }
    }

    public void get_Message() {
        unbt();
        items.clear();
        Call<ArrayList<MessageFile>> observ = RetrofitService.getInstance().getRetrofitRequest().getMessage(id, type);
        observ.enqueue(new Callback<ArrayList<MessageFile>>() {
            @Override
            public void onResponse(Call<ArrayList<MessageFile>> call, Response<ArrayList<MessageFile>> response) {
                if (response.isSuccessful()) {
                    items.addAll(response.body());

                    messageAdapter.notifyDataSetChanged();
                    bt();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MessageFile>> call, Throwable t) {

            }
        });
    }

    @OnItemClick(R.id.lv_message)
    public void see_Message(AdapterView<?> parent, final int i) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비

        final Dialog_message dialog_message = new Dialog_message(this, items.get(i), type);

        dialog_message.setCallBack(new Dialog_message.CallBack() {
            @Override
            public void click_replay(String member_id) {
                send_message(member_id);
                dialog_message.dismiss();
            }

            @Override
            public void click_close() {
                dialog_message.dismiss();
            }

            @Override
            public void click_del() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MessageActivity.this);
                dialog.setTitle("쪽지 삭제").setMessage("정말 삭제 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int a) {
                                Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().delMessage(items.get(i).getMessages().getId(), type);
                                observ.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            setLl_refresh();
                                            dialog_message.dismiss();
                                            return;
                                        }
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

        WindowManager.LayoutParams wm = dialog_message.getWindow().getAttributes();
        wm.copyFrom(dialog_message.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width - (width / 6);  //화면 너비의 절반

        dialog_message.show();
    }


    @OnClick(R.id.ll_search_member)
    public void do_search_member() {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비

        final Dialog_searchUser dialog_searchUser = new Dialog_searchUser(this);

        dialog_searchUser.setCallBack(new Dialog_searchUser.CallBack() {
            @Override
            public void onClick_user(String member_id) {
                get_memberInfo(member_id);
                dialog_searchUser.dismiss();
            }
        });

        WindowManager.LayoutParams wm = dialog_searchUser.getWindow().getAttributes();
        wm.copyFrom(dialog_searchUser.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width - (width / 6);

        dialog_searchUser.show();
    }

    @OnClick(R.id.bt_back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.ll_change)
    public void change_type() {
        if (type == 1) {
            type = 0;
        } else if (type == 0) {
            type = 1;
        }
        setTitle();
        get_Message();
    }

    @OnClick(R.id.ll_refresh)
    public void setLl_refresh() {
        get_Message();
    }

    @OnClick(R.id.ll_home)
    public void go_home() {
        Intent intent = new Intent(MessageActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_right_out);
    }

    public void unbt() {
        ll_home.setEnabled(false);
        ll_change.setEnabled(false);
        ll_refresh.setEnabled(false);
    }

    public void bt() {
        ll_home.setEnabled(true);
        ll_change.setEnabled(true);
        ll_refresh.setEnabled(true);
    }

    public void get_memberInfo(String member_id) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비

        final Dialog_member_info dialog_member_info = new Dialog_member_info(this, member_id, id);

        dialog_member_info.setCallBack(new Dialog_member_info.CallBack() {
            @Override
            public void onClick_send(String member_id) {
                send_message(member_id);
                dialog_member_info.dismiss();
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


    public void send_message(String member_id) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비

        final Dialog_send_message dialog_send_message = new Dialog_send_message(this, member_id, id);

        dialog_send_message.setCallBack(new Dialog_send_message.CallBack() {
            @Override
            public void onClick_close() {
                dialog_send_message.dismiss();
            }
        });


        WindowManager.LayoutParams wm = dialog_send_message.getWindow().getAttributes();
        wm.copyFrom(dialog_send_message.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width - (width / 6);  //화면 너비의 절반

        dialog_send_message.show();
    }


}
