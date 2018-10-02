package com.example.sss.team_project;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.sss.team_project.adapter.BoardAdapter;
import com.example.sss.team_project.model.Board;
import com.example.sss.team_project.model.BoardList;
import com.example.sss.team_project.retrofit.RetrofitService;
import com.example.sss.team_project.utills.PreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardActivity extends AppCompatActivity {
    @BindView(R.id.bt_back)
    Button bt_back;
    @BindView(R.id.bt_search)
    Button bt_search;
    @BindView(R.id.bt_clear)
    Button bt_clear;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.lv_board)
    ListView lv_board;
    @BindView(R.id.bt_option)
    Button bt_option;
    @BindView(R.id.ll_write)
    LinearLayout ll_write;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_favorit)
    LinearLayout ll_favorit;
    @BindView(R.id.ll_chat)
    LinearLayout ll_chat;

    int WRITE_CODE = 100;
    int DETAIL_CODE = 1000;

    LayoutInflater mInflater;
    BoardAdapter boardAdapter;
    ArrayList<BoardList> board_items = new ArrayList<>();

    int page = 1;
    boolean list_redundancy = false;
    boolean search_mode = false;
    int type;
    int search_type = 0;
    String id;


    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");
    long mNow;
    Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);

        id = PreferenceUtil.getInstance(BoardActivity.this).getStringExtra("id");

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        final String date = mFormat.format(mDate);


        type = getIntent().getIntExtra("type", 0);
        set_header_Title();

        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lv_board.addFooterView(mInflater.inflate(R.layout.board_footer, null));

        lv_board.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        boardAdapter = new BoardAdapter(board_items, date);
        lv_board.setAdapter(boardAdapter);

        loadBoardList();

    }

    @OnItemClick(R.id.lv_board)
    public void board_itemClick(AdapterView<?> parent, final int i) {
        Board item = board_items.get(i).getBoard();
        Intent intent = new Intent(BoardActivity.this, DetailBoardActivity.class);
        intent.putExtra("board_id", item.getId());
        startActivityForResult(intent, DETAIL_CODE);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    public void loadBoardList() {
        Call<ArrayList<BoardList>> observ = RetrofitService.getInstance().getRetrofitRequest().getBoardList(type, 1);
        observ.enqueue(new Callback<ArrayList<BoardList>>() {
            @Override
            public void onResponse(Call<ArrayList<BoardList>> call, Response<ArrayList<BoardList>> response) {
                if (response.isSuccessful()) {
                    board_items.clear();
                    page = 2;
                    board_items.addAll(response.body());
                    boardAdapter.notifyDataSetChanged();
                    list_redundancy = true;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BoardList>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    public void addBoardList() {
        Call<ArrayList<BoardList>> observ = RetrofitService.getInstance().getRetrofitRequest().getBoardList(type, page);
        observ.enqueue(new Callback<ArrayList<BoardList>>() {
            @Override
            public void onResponse(Call<ArrayList<BoardList>> call, Response<ArrayList<BoardList>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() == 0) {
                        list_redundancy = false;
                    } else {
                        board_items.addAll(response.body());
                        boardAdapter.notifyDataSetChanged();
                        page++;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BoardList>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.ll_chat)
    public void go_chat() {
        Intent intent = new Intent(BoardActivity.this, MessageActivity.class);
        intent.putExtra("type", 0);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }


    @OnClick(R.id.bt_back)
    public void back() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);

        onBackPressed();
    }

    @OnClick(R.id.bt_search)
    public void start_search() {
        search_mode = true;
        bt_search.setVisibility(View.INVISIBLE);
        tv_title.setVisibility(View.INVISIBLE);

        bt_clear.setVisibility(View.VISIBLE);
        et_search.setVisibility(View.VISIBLE);
        bt_option.setVisibility(View.VISIBLE);
        et_search.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @OnEditorAction(R.id.et_search)
    public boolean search() {
        String text = et_search.getText().toString();

        if (text.equals("") || text == null) {

            return true;
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);

            list_redundancy = false;
            board_items.clear();
            boardAdapter.notifyDataSetChanged();

            String str_search = et_search.getText().toString();
            Call<ArrayList<BoardList>> observ = RetrofitService.getInstance().getRetrofitRequest().searchBoard(type, search_type, str_search);
            observ.enqueue(new Callback<ArrayList<BoardList>>() {
                @Override
                public void onResponse(Call<ArrayList<BoardList>> call, Response<ArrayList<BoardList>> response) {
                    if (response.isSuccessful()) {
                        board_items.addAll(response.body());
                        boardAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<BoardList>> call, Throwable t) {

                }
            });

            return true;
        }
    }


    @OnClick(R.id.bt_clear)
    public void et_clear() {
        et_search.setText("");
    }

    //검색 옵션
    @OnClick(R.id.bt_option)
    public void option() {
        PopupMenu p = new PopupMenu(getApplicationContext(), bt_option);
        getMenuInflater().inflate(R.menu.search_menu, p.getMenu());
        p.show();

        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.search_sc:
                        search_type = 0;
                        break;
                    case R.id.search_s:
                        search_type = 1;
                        break;
                    case R.id.search_i:
                        search_type = 2;
                        break;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.ll_home)
    public void ll_home() {
        Intent intent = new Intent(BoardActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }


    @Override
    public void onBackPressed() {
        if (search_mode) {
            et_clear();

            search_mode = false;
            bt_search.setVisibility(View.VISIBLE);
            tv_title.setVisibility(View.VISIBLE);

            bt_option.setVisibility(View.INVISIBLE);
            bt_clear.setVisibility(View.INVISIBLE);
            et_search.setVisibility(View.INVISIBLE);

            loadBoardList();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_not_move, R.anim.anim_right_out);
        }
    }

    public void set_header_Title() {
        if (type == 1) {
            tv_title.setText("자유게시판");
        } else if (type == 2) {
            tv_title.setText("분양 게시판");
        }
    }

    @OnClick(R.id.ll_favorit)
    public void getHitBoard() {
        board_items.clear();
        boardAdapter.notifyDataSetChanged();
        Call<ArrayList<BoardList>> observ = RetrofitService.getInstance().getRetrofitRequest().hitBoad(id, type);

        observ.enqueue(new Callback<ArrayList<BoardList>>() {
            @Override
            public void onResponse(Call<ArrayList<BoardList>> call, Response<ArrayList<BoardList>> response) {
                if (response.isSuccessful()) {
                    board_items.addAll(response.body());
                    boardAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BoardList>> call, Throwable t) {

            }
        });

    }


    @OnClick(R.id.ll_write)
    public void writeBoard() {
        Intent intent = new Intent(BoardActivity.this, WriteBoardActivity.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, WRITE_CODE);
        BoardActivity.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
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