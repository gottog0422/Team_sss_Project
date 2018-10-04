package com.example.sss.team_project;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.team_project.adapter.WriteContentAdapter;
import com.example.sss.team_project.model.DetailContent;
import com.example.sss.team_project.model.WriteContent;
import com.example.sss.team_project.retrofit.RetrofitService;
import com.example.sss.team_project.utills.PreferenceUtil;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteBoardActivity extends AppCompatActivity {
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.lv_content)
    ListView lv_content;
    @BindView(R.id.bt_write)
    Button bt_write;
    @BindView(R.id.bt_back)
    Button bt_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_add_write)
    LinearLayout ll_add_write;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_get_post)
    LinearLayout ll_get_post;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    int type;
    String id;
    Uri imgUri = null;

    ArrayList<MultipartBody.Part> fileparts = new ArrayList<>();
    ArrayList<File> temp = new ArrayList<>();
    ArrayList<WriteContent> items_content = new ArrayList<>();

    ArrayList<DetailContent> items_mody = new ArrayList<>();
    WriteContentAdapter contentAdapter;
    boolean mody;
    long board_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra("type", 0);
        mody = getIntent().getBooleanExtra("b_modify", false);

        id = PreferenceUtil.getInstance(WriteBoardActivity.this).getStringExtra("id");


        if (mody) {
            items_mody = (ArrayList<DetailContent>) getIntent().getSerializableExtra("content_item");
            et_title.setText(getIntent().getStringExtra("title"));
            board_id = getIntent().getLongExtra("board_id", -1);
            set_modifyData();
        } else {
            WriteContent item = new WriteContent();
            items_content.add(item);
        }


        contentAdapter = new WriteContentAdapter(items_content, this);
        lv_content.setAdapter(contentAdapter);
    }


    @OnClick(R.id.bt_write)
    public void writeBoard() {
        String t1 = et_title.getText().toString().replaceAll(" ", "");

        bt_write.setFocusableInTouchMode(true);
        bt_write.requestFocus();

        if (t1.equals("")) {
            Toast.makeText(WriteBoardActivity.this, "제목을 다시 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            String content = "";

            for (int i = 0; items_content.size() > i; i++) {
                if (items_content != null) {
                    if ((items_content.get(i).getItem_str() != null && !items_content.get(i).getItem_str().equals(""))
                            || items_content.get(i).getItem_iv() != null)
                        content += items_content.get(i).getItem_str() + "#cut";
                }
            }
            String t2 = content.replaceAll("#cut", "");
            String t3 = t2.replaceAll(" ", "");

            for (int i = 0; i < items_content.size(); i++) {
                if (items_content.get(i).getItem_iv() != null) {
                    t3 += items_content.get(i).getItem_iv().toString();
                }
            }
            if (t3.equals("")) {
                Toast.makeText(WriteBoardActivity.this, "내용을 다시 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < items_content.size(); i++) {
                    items_content.get(i).getItem_iv();

                    if (items_content.get(i).getItem_iv() != null) {
                        try {
                            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), items_content.get(i).getItem_iv());
                            Context context = this;

                            File storage = context.getCacheDir();
                            String fileName = "board_" + "_" + i + ".jpg";
                            File tempFile = new File(storage, fileName);
                            tempFile.createNewFile();
                            FileOutputStream out = new FileOutputStream(tempFile);
                            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);

                            Log.d("ksg", "filename = " + fileName);
                            MultipartBody.Part file = MultipartBody.Part.createFormData
                                    ("file_" + i, tempFile.getName(), RequestBody.create(MediaType.parse("image/*"), tempFile));

                            fileparts.add(file);
                            temp.add(tempFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mNow = System.currentTimeMillis();
                mDate = new Date(mNow);
                final String date = mFormat.format(mDate);

                String title = et_title.getText().toString();
                RequestBody body_title = RequestBody.create(MediaType.parse("text/plane"), title);
                RequestBody body_content = RequestBody.create(MediaType.parse("text/plane"), content);
                RequestBody body_date = RequestBody.create(MediaType.parse("text/plane"), date);
                RequestBody body_id = RequestBody.create(MediaType.parse("text/plane"), id);
                RequestBody body_type = RequestBody.create(MediaType.parse("text/plane"), String.valueOf(type));

                if (mody) {
                    RequestBody body_board_id = RequestBody.create(MediaType.parse("text/plane"), String.valueOf(board_id));
                    Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().modyBoard(body_board_id, body_title, body_content, fileparts);
                    observ.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Intent intent = getIntent();
                            intent.putExtra("modyOK", true);
                            setResult(RESULT_OK, intent);

                            finish();
                            overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });

                } else {
                    Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().writeBoard(body_id, body_type, body_title, body_content, body_date, fileparts);
                    observ.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Intent return_intent = new Intent();
                                setResult(RESULT_OK, return_intent);

                                for (int i = temp.size() - 1; i >= 0; i--) {
                                    if (temp.get(i).exists()) {
                                        temp.get(i).delete();
                                    }
                                }
                                temp.clear();
                                fileparts.clear();
                                finish();
                                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }

            }
        }
    }

    @OnClick(R.id.ll_get_post)
    public void getPhoto() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(WriteBoardActivity.this);
        dialog.setTitle("사진 업로드").setMessage("업로드 이미지 선택")
                .setPositiveButton("앨범에서 선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, 1000);
                    }
                })
                .setNeutralButton("사진촬영", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        PermissionListener permissionlistener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ex) {
                                        // Error occurred while creating the File
                                    }

                                    if (photoFile != null) {
                                        imgUri = FileProvider.getUriForFile(WriteBoardActivity.this, getPackageName(), photoFile);

                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                                        startActivityForResult(takePictureIntent, 2000);
                                    }
                                }
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            }
                        };


                        TedPermission.with(WriteBoardActivity.this)
                                .setPermissionListener(permissionlistener)
                                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .check();

                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).create().show();
    }

    @OnClick(R.id.ll_add_write)
    public void add_context() {
        WriteContent item = new WriteContent();
        items_content.add(item);

        contentAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.ll_home)
    public void ll_home() {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(WriteBoardActivity.this);
        dialog.setTitle("글 작성 취소").setMessage("정말 취소 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(WriteBoardActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case 1000: {
                //앨범에서 선택
                imgUri = data.getData();
                WriteContent item = new WriteContent();
                item.setItem_str(null);
                item.setItem_iv(imgUri);
                items_content.add(item);

                contentAdapter.notifyDataSetChanged();
                break;
            }

            case 2000: {
                //사진 촬영 후 가져오기
                WriteContent item = new WriteContent();
                item.setItem_str(null);
                item.setItem_iv(imgUri);
                items_content.add(item);

                contentAdapter.notifyDataSetChanged();

                break;
            }
        }

    }

    @OnClick(R.id.bt_back)
    public void go_back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(WriteBoardActivity.this);
        dialog.setTitle("글 작성 취소").setMessage("정말 취소 하시겠습니까?")
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

    public void set_modifyData() {


        for (int i = 0; i < items_mody.size(); i++) {
            WriteContent item = new WriteContent();
            if (items_mody.get(i).getItem_str() != null) {
                item.setItem_str(items_mody.get(i).getItem_str());

                items_content.add(item);

            }
//            if (items_mody.get(i).getItem_iv() != null) {
//                String url = "http://10.0.2.2:8090/sss/resources/upload/";
//                String temp_uri = url + items_mody.get(i).getItem_iv();
//
//                Uri uri = Uri.parse(temp_uri);
//                item.setItem_iv(uri);
//            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        return image;
    }
}



