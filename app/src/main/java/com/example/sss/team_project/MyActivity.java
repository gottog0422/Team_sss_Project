package com.example.sss.team_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sss.team_project.bus.BusProvider;
import com.example.sss.team_project.customdialog.Dialog_Pic;
import com.example.sss.team_project.customdialog.Dialog_change_pw;
import com.example.sss.team_project.event.ChangePro;
import com.example.sss.team_project.retrofit.RetrofitService;
import com.example.sss.team_project.utills.PreferenceUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyActivity extends AppCompatActivity {
    @BindView(R.id.bt_back)
    Button bt_back;
    @BindView(R.id.rl_memberPic)
    RelativeLayout rl_memberPic;
    @BindView(R.id.iv_member_pic)
    ImageView iv_member_pic;

    @BindView(R.id.tv_member_nick)
    TextView tv_member_nick;
    @BindView(R.id.bt_change_nick)
    ImageView bt_change_nick;

    @BindView(R.id.bt_change_pw)
    Button bt_change_pw;

    @BindView(R.id.sv_intro)
    ScrollView sv_intro;
    @BindView(R.id.tv_intro)
    TextView tv_intro;

    @BindView(R.id.bt_change_intro)
    ImageView bt_change_intro;

    Bus bus = BusProvider.getInstance().getBus();

    Uri imgUri = null;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        bus.register(this);

        id = PreferenceUtil.getInstance(MyActivity.this).getStringExtra("id");

        setMemberInfo(id);
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


    @OnClick(R.id.bt_change_pw)
    public void change_pw() {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비

        final Dialog_change_pw dialog_change_pw = new Dialog_change_pw(this);

        dialog_change_pw.setCallBack(new Dialog_change_pw.CallBack() {
            @Override
            public void bt_cancel() {
                dialog_change_pw.dismiss();
            }

            @Override
            public void bt_change(String et_str1, String et_str2, String et_str3) {
                if (et_str2.length() < 6 || et_str2.length() > 16) {
                    Toast.makeText(MyActivity.this, "6~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (!et_str2.equals(et_str3)) {
                        Toast.makeText(MyActivity.this, "변경될 비밀번호가 맞지않습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        Call<Boolean> observ = RetrofitService.getInstance().getRetrofitRequest().change_pw(id, et_str1, et_str2);
                        observ.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    boolean v = response.body();

                                    if (v) {
                                        Toast.makeText(MyActivity.this, "변경 완료", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(MyActivity.this, "현재 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                    }
                }
                dialog_change_pw.dismiss();
            }
        });

        WindowManager.LayoutParams wm = dialog_change_pw.getWindow().getAttributes();
        wm.copyFrom(dialog_change_pw.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width - (width / 4);  //화면 너비의 절반

        dialog_change_pw.show();
    }

    @OnClick(R.id.rl_memberPic)
    public void changePic() {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비

        final Dialog_Pic dialog_pic = new Dialog_Pic(this);

        dialog_pic.setCallBack(new Dialog_Pic.CallBack() {
            @Override
            public void onClickBt1() {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select picture"), 1000);
                dialog_pic.dismiss();
            }

            @Override
            public void onClickBt2() {
                Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().delPic(id);
                observ.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        setMemberInfo(id);

                        ChangePro change = new ChangePro();
                        bus.post(change);
                        dialog_pic.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

        WindowManager.LayoutParams wm = dialog_pic.getWindow().getAttributes();
        wm.copyFrom(dialog_pic.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width - (width / 4);  //화면 너비의 절반

        dialog_pic.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1000:
                    imgUri = data.getData();
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                        Context context = this;

                        File storage = context.getCacheDir();
                        String fileName = "pic_" + id + ".jpg";
                        final File tempFile = new File(storage, fileName);
                        tempFile.createNewFile();
                        FileOutputStream out = new FileOutputStream(tempFile);
                        bm.compress(Bitmap.CompressFormat.JPEG, 90, out);

                        final MultipartBody.Part filePart = MultipartBody.Part.createFormData
                                ("file", tempFile.getName(), RequestBody.create(MediaType.parse("image/*"), tempFile));
                        final RequestBody member_id = RequestBody.create(MediaType.parse("text/plain"), id);
                        Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().inputMemberPic(member_id, filePart);
                        observ.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    tempFile.delete();

                                    ChangePro change = new ChangePro();
                                    bus.post(change);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.d("ksg", "fail");
                                t.printStackTrace();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }
    }


    public void setMemberInfo(String id) {
        Call<ArrayList<String>> observ = RetrofitService.getInstance().getRetrofitRequest().get_memberInfo(id);
        observ.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful()) {
                    ArrayList<String> items = response.body();
                    tv_member_nick.setText(items.get(0).toString());

                    if (items.get(1) == null || items.get(1).equals("")) {
                        Drawable memberpic_null = getResources().getDrawable(R.drawable.memberpic_null);
                        iv_member_pic.setImageDrawable(memberpic_null);
                    } else {
                        String url = "http://10.0.2.2:8090/sss/resources/profilepic/";
                        GlideApp.with(MyActivity.this)
                                .load(url + items.get(1)).centerCrop()
                                .into(iv_member_pic);
                    }

                    if (items.get(2) == null || items.get(2).equals("")) {
                        tv_intro.setText("소개가 없습니다.");
                    } else {
                        tv_intro.setText(items.get(2));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
            }
        });
    }


    @Subscribe
    public void changePro(ChangePro event) {
        setMemberInfo(id);
    }

    @OnClick(R.id.bt_change_nick)
    public void nick_change_do() {
        AlertDialog.Builder change_ad = new AlertDialog.Builder(MyActivity.this);
        change_ad.setTitle("닉네임 변경");
        final EditText et = new EditText(MyActivity.this);
        change_ad.setView(et);
        change_ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nick = et.getText().toString();
                if (nick.length() == 0) {
                    Toast.makeText(MyActivity.this, "최소 2글자 이상이여야 합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (nick.length() < 2 || nick.length() > 10 || !nick.matches("[0-9|a-z|A-Z|가-힣|]*")) {
                        Toast.makeText(MyActivity.this, "2~10자 한글, 영문 대 소문자, 숫자를 사용하세요", Toast.LENGTH_SHORT).show();
                    } else {
                        Call<Boolean> observ = RetrofitService.getInstance().getRetrofitRequest().change_nick(id, nick);
                        observ.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    if (response.body()) {
                                        setMemberInfo(id);

                                        ChangePro change = new ChangePro();
                                        bus.post(change);
                                    } else {
                                        Toast.makeText(MyActivity.this, "중복된 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                    }
                }

            }
        });
        change_ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        change_ad.show();
    }

    @OnClick(R.id.bt_change_intro)
    public void intro_change_do() {
        AlertDialog.Builder change_intro_ad = new AlertDialog.Builder(MyActivity.this);
        change_intro_ad.setTitle("소개 변경");
        final EditText et = new EditText(MyActivity.this);
        change_intro_ad.setView(et);
        change_intro_ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String str = et.getText().toString();

                Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().change_intro(id, str);
                observ.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            setMemberInfo(id);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            }
        });
        change_intro_ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        change_intro_ad.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

}




