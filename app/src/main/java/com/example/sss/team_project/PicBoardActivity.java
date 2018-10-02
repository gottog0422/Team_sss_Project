package com.example.sss.team_project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.sss.team_project.adapter.PicBoardAdapter;
import com.example.sss.team_project.model.Board;
import com.example.sss.team_project.model.PicBoardList;
import com.example.sss.team_project.retrofit.RetrofitService;
import com.example.sss.team_project.utills.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PicBoardActivity extends AppCompatActivity {

    @BindView(R.id.gv_list)
    GridViewWithHeaderAndFooter gv_list;
    @BindView(R.id.bt_back)
    Button bt_back;
    @BindView(R.id.ll_favorit)
    LinearLayout ll_favorit;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_chat)
    LinearLayout ll_chat;
    @BindView(R.id.ll_write)
    LinearLayout ll_write;

    private String id;

    LayoutInflater mInflater;

    PicBoardAdapter picBoardAdapter;
    ArrayList<PicBoardList> board_items = new ArrayList<>();
    int page = 1;
    boolean list_redundancy = false;

    final static int WRITE_CODE = 3012;
    final static int DETAIL_CODE = 4013;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_board);
        ButterKnife.bind(this);

        id = PreferenceUtil.getInstance(PicBoardActivity.this).getStringExtra("id");

        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gv_list.addFooterView(mInflater.inflate(R.layout.board_footer, null));

        gv_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i + i1 == i2) {
                    if (list_redundancy) {
                        addBoardList();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }
        });


        picBoardAdapter = new PicBoardAdapter(board_items);
        gv_list.setAdapter(picBoardAdapter);

        loadBoardList();
    }

    public void loadBoardList() {
        Call<ArrayList<PicBoardList>> observ = RetrofitService.getInstance().getRetrofitRequest().getPicBoardList(1);
        observ.enqueue(new Callback<ArrayList<PicBoardList>>() {
            @Override
            public void onResponse(Call<ArrayList<PicBoardList>> call, Response<ArrayList<PicBoardList>> response) {
                if (response.isSuccessful()) {
                    board_items.clear();
                    page = 2;
                    board_items.addAll(response.body());
                    picBoardAdapter.notifyDataSetChanged();
                    list_redundancy = true;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PicBoardList>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @OnItemClick(R.id.gv_list)
    public void board_itemClick(AdapterView<?> parent, final int i) {
        Board item = board_items.get(i).getBoard();
        Intent intent = new Intent(PicBoardActivity.this, DetailBoardActivity.class);
        intent.putExtra("board_id", item.getId());
        startActivityForResult(intent, DETAIL_CODE);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }


    public void addBoardList() {
        Call<ArrayList<PicBoardList>> observ = RetrofitService.getInstance().getRetrofitRequest().getPicBoardList(page);
        observ.enqueue(new Callback<ArrayList<PicBoardList>>() {
            @Override
            public void onResponse(Call<ArrayList<PicBoardList>> call, Response<ArrayList<PicBoardList>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() == 0) {
                        list_redundancy = false;
                    } else {
                        board_items.addAll(response.body());
                        picBoardAdapter.notifyDataSetChanged();
                        page++;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PicBoardList>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.bt_back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.ll_chat)
    public void go_chat() {
        Intent intent = new Intent(PicBoardActivity.this, MessageActivity.class);
        intent.putExtra("type", 0);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    @OnClick(R.id.ll_favorit)
    public void getHitBoard() {
        board_items.clear();
        picBoardAdapter.notifyDataSetChanged();
        Call<ArrayList<PicBoardList>> observ = RetrofitService.getInstance().getRetrofitRequest().hitPicBoard(id);

        observ.enqueue(new Callback<ArrayList<PicBoardList>>() {
            @Override
            public void onResponse(Call<ArrayList<PicBoardList>> call, Response<ArrayList<PicBoardList>> response) {
                if (response.isSuccessful()) {
                    board_items.addAll(response.body());
                    picBoardAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PicBoardList>> call, Throwable t) {

            }
        });

    }
    @OnClick(R.id.ll_home)
    public void ll_home() {
        Intent intent = new Intent(PicBoardActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    @OnClick(R.id.ll_write)
    public void writeBoard() {
        Intent intent = new Intent(PicBoardActivity.this, WriteBoardActivity.class);
        intent.putExtra("type", 3);
        startActivityForResult(intent, WRITE_CODE);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == WRITE_CODE) {
                page = 1;
                list_redundancy = false;
                loadBoardList();
            } else if (requestCode == DETAIL_CODE) {
                if (data.getBooleanExtra("delOK", true)) {
                    page = 1;
                    list_redundancy = false;
                    loadBoardList();
                }
            }
        }
    }
}
