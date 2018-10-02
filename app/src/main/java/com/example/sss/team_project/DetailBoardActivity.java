package com.example.sss.team_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sss.team_project.adapter.DetailContentAdapter;
import com.example.sss.team_project.model.AFile;
import com.example.sss.team_project.model.Board;
import com.example.sss.team_project.model.BoardFile;
import com.example.sss.team_project.model.CommentFile;
import com.example.sss.team_project.model.DetailContent;
import com.example.sss.team_project.model.PN_Board;
import com.example.sss.team_project.retrofit.RetrofitService;
import com.example.sss.team_project.utills.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBoardActivity extends AppCompatActivity {
    @BindView(R.id.lv_detail)
    ListView lv_detail;
    @BindView(R.id.bt_back)
    Button bt_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rl_commentbox)
    RelativeLayout rl_commentbox;
    @BindView(R.id.comment_count)
    TextView comment_count;
    @BindView(R.id.iv_favorite)
    ImageView iv_favorite;
    @BindView(R.id.tv_favorite_count)
    TextView tv_favorite_count;

    @BindView(R.id.bt_writer_option)
    ImageView bt_writer_option;

    //header
    TextView tv_header_title;
    TextView tv_header_writer_nick;
    TextView tv_header_date;
    TextView tv_header_count;
    ImageView iv_header_writer_pic;

    //footer
    TextView tv_preTitle;
    TextView tv_nextTitle;
    RelativeLayout rl_next_board;
    RelativeLayout rl_pre_board;

    String id;
    int type;
    Long board_id;

    boolean hit = false;

    DetailContentAdapter contentAdapter;

    BoardFile boardFile = new BoardFile();
    Board board = new Board();
    PN_Board pn_board = new PN_Board();

    ArrayList<DetailContent> content_item = new ArrayList<>();
    int file_num = 0;
    int hit_count = 0;

    final static int READCOMMENT = 9952;
    final static int MODIFY = 8989;
    Drawable pic_hit;
    Drawable pic_none_hit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_board);
        ButterKnife.bind(this);

        pic_hit = getResources().getDrawable(R.drawable.ic_favorite);
        pic_none_hit = getResources().getDrawable(R.drawable.ic_favorite_border);


        id = PreferenceUtil.getInstance(this).getStringExtra("id");

        Intent getintent = getIntent();
        board_id = getintent.getLongExtra("board_id", 0);

        //header
        View header = getLayoutInflater().inflate(R.layout.detailboard_header, null, false);

        tv_header_title = header.findViewById(R.id.tv_header_title);
        tv_header_writer_nick = header.findViewById(R.id.tv_header_writer_nick);
        tv_header_count = header.findViewById(R.id.tv_header_count);
        tv_header_date = header.findViewById(R.id.tv_header_date);
        iv_header_writer_pic = header.findViewById(R.id.iv_header_writer_pic);

        //footer
        View footer = getLayoutInflater().inflate(R.layout.detailboard_footer, null, false);

        rl_next_board = footer.findViewById(R.id.rl_next_board);
        tv_nextTitle = footer.findViewById(R.id.tv_nextTitle);

        rl_pre_board = footer.findViewById(R.id.rl_pre_board);
        tv_preTitle = footer.findViewById(R.id.tv_preTitle);


        contentAdapter = new DetailContentAdapter(content_item);
        lv_detail.setAdapter(contentAdapter);
        lv_detail.addHeaderView(header);
        lv_detail.addFooterView(footer);

        getBoard();

        rl_next_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rl_next_board.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(DetailBoardActivity.this, DetailBoardActivity.class);
                    intent.putExtra("board_id", pn_board.getNext_id());
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);

                }
            }
        });

        rl_pre_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rl_pre_board.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(DetailBoardActivity.this, DetailBoardActivity.class);
                    intent.putExtra("board_id", pn_board.getPre_id());
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
                }
            }
        });

    }

    public void getBoard() {
        Call<BoardFile> observ = RetrofitService.getInstance().getRetrofitRequest().getBoardInfo(board_id, id);
        observ.enqueue(new Callback<BoardFile>() {
            @Override
            public void onResponse(Call<BoardFile> call, Response<BoardFile> response) {
                if (response.isSuccessful()) {
                    content_item.clear();
                    boardFile = response.body();
                    board = boardFile.getBoard();
                    pn_board = boardFile.getPn_board();

                    hit_count = board.getHit();
                    tv_favorite_count.setText(hit_count+"");

                    String writer_pic = boardFile.getWriter_pic();

                    type = board.getType();

                    set_header_Title();
                    set_headerData();
                    set_footerData();
                    set_commentCount();
                    getHit();

                    if (writer_pic == null || writer_pic.equals("")) {
                        Drawable memberpic_null = getResources().getDrawable(R.drawable.memberpic_null);
                        iv_header_writer_pic.setImageDrawable(memberpic_null);
                    } else {
                        String url = "http://10.0.2.2:8090/sss/resources/profilepic/";
                        GlideApp.with(DetailBoardActivity.this)
                                .load(url + writer_pic).centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(iv_header_writer_pic);
                    }

                    String tmp_str = null;
                    tmp_str = board.getContent();
                    String[] ary = null;
                    ary = tmp_str.split("#cut");
                    for (int i = 0; i < ary.length; i++) {
                        DetailContent detailContent = new DetailContent();

                        if (!ary[i].equals("null")) {
                            detailContent.setItem_str(ary[i]);
                        } else {
                            detailContent.setItem_iv(boardFile.getFiles().get(file_num).getFile_name());
                            file_num++;
                        }
                        content_item.add(detailContent);
                    }

                    contentAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onFailure(Call<BoardFile> call, Throwable t) {

            }
        });

    }

    public void set_header_Title() {
        if (type == 1) {
            tv_title.setText("자유게시판");
        } else if (type == 2) {
            tv_title.setText("분양 게시판");
        }

        if (id.equals(board.getWriter_id())) {
            bt_writer_option.setVisibility(View.VISIBLE);
        } else {
            bt_writer_option.setVisibility(View.GONE);
        }
    }

    public void getHit() {
        Call<Boolean> observ = RetrofitService.getInstance().getRetrofitRequest().getHit(board_id, id);
        observ.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    hit = response.body();
                    if (hit) {
                        iv_favorite.setImageDrawable(pic_hit);
                    } else {
                        iv_favorite.setImageDrawable(pic_none_hit);
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public void set_commentCount() {
        comment_count.setText(String.valueOf(boardFile.getComment_count()));
    }

    public void set_headerData() {
        tv_header_title.setText(board.getTitle());
        tv_header_writer_nick.setText(boardFile.getWriter_nick());
        tv_header_count.setText(String.valueOf(board.getCount()));
        tv_header_date.setText(board.getMdate());
    }

    public void set_footerData() {
        if (pn_board.getPre_id() != null) {
            rl_pre_board.setVisibility(View.VISIBLE);
            tv_preTitle.setText(pn_board.getPre_title());

        } else {
            rl_pre_board.setVisibility(View.GONE);
        }

        if (pn_board.getNext_id() != null) {
            rl_next_board.setVisibility(View.VISIBLE);
            tv_nextTitle.setText(pn_board.getNext_title());

        } else {
            rl_next_board.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.rl_commentbox)
    public void go_comment() {
        Intent intent = new Intent(DetailBoardActivity.this, CommentActivity.class);
        intent.putExtra("board_id", board_id);
        intent.putExtra("board_writer_id", board.getWriter_id());
        startActivityForResult(intent, READCOMMENT);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }


    @OnClick(R.id.bt_writer_option)
    public void writer_option() {
        PopupMenu p = new PopupMenu(getApplicationContext(), bt_writer_option);
        getMenuInflater().inflate(R.menu.writer_menu, p.getMenu());
        p.show();

        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.del:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(DetailBoardActivity.this);
                        dialog.setTitle("게시물 삭제").setMessage("정말 삭제 하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int a) {
                                        Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().delBoard(board_id);
                                        observ.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                Intent intent = getIntent();
                                                intent.putExtra("delOK", true);
                                                setResult(RESULT_OK, intent);
                                                finish();
                                                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
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

                        break;
                    case R.id.modify:

                        Intent intent = new Intent(DetailBoardActivity.this, WriteBoardActivity.class);

                        intent.putExtra("b_modify", true);
                        intent.putExtra("content_item", content_item);
                        intent.putExtra("title", board.getTitle());
                        intent.putExtra("type", type);
                        intent.putExtra("board_id", board_id);

                        startActivityForResult(intent, MODIFY);

                        break;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.iv_favorite)
    public void setHit() {
        Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().setHit(board_id, id, !hit);
        observ.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
               if (response.isSuccessful()){
                   if (hit) {
                       hit = false;
                       iv_favorite.setImageDrawable(pic_none_hit);

                       hit_count--;
                       tv_favorite_count.setText(hit_count+"");
                   } else {
                       hit = true;
                       iv_favorite.setImageDrawable(pic_hit);

                       hit_count++;
                       tv_favorite_count.setText(hit_count+"");
                   }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.bt_back)
    public void back() {
        onBackPressed();
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
            if (requestCode == MODIFY) {
                if (data.getBooleanExtra("modyOK", true)) {
                    content_item.clear();
                    contentAdapter.notifyDataSetChanged();
                    file_num = 0;

                    getBoard();
                }
            }
        }
    }
}
