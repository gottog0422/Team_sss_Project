package com.example.sss.team_project.retrofit;


import com.example.sss.team_project.model.Board;
import com.example.sss.team_project.model.BoardFile;
import com.example.sss.team_project.model.BoardList;
import com.example.sss.team_project.model.CommentFile;
import com.example.sss.team_project.model.MessageFile;
import com.example.sss.team_project.model.PicBoardList;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitRequest {

    @GET("login.do")
    Call<Boolean> login(@Query("member_id") String member_id, @Query("member_pw") String member_pw);

    @GET("checkoverlap.do")
    Call<ArrayList<Integer>> checkoverlap(@Query("member_id") String member_id, @Query("member_nick") String member_nick);

    @FormUrlEncoded
    @POST("inputMember.do")
    Call<Void> inputMember(@Field("member_id") String member_id, @Field("member_pw") String member_pw, @Field("member_nick") String member_nick);

    @FormUrlEncoded
    @POST("writeComment.do")
    Call<Void> writeComment(@Field("board_id") Long board_id, @Field("member_id") String member_id, @Field("comment") String comment, @Field("mdate") String mdate);


    @GET("getComment.do")
    Call<ArrayList<CommentFile>> getComment(@Query("board_id") Long board_id);

    @GET("delComment.do")
    Call<Void> delComment(@Query("comment_id") Long comment_id);

    @FormUrlEncoded
    @POST("modifyComment.do")
    Call<Void> modifyComment(@Field("comment_id") Long comment_id, @Field("comment") String comment);

    @GET("getmemberInfo.do")
    Call<ArrayList<String>> get_memberInfo(@Query("member_id") String member_id);

    @Multipart
    @POST("inputMemberPic.do")
    Call<Void> inputMemberPic(@Part("member_id") RequestBody member_id, @Part MultipartBody.Part photo);

    @Multipart
    @POST("writeBoard.do")
    Call<Void> writeBoard(@Part("member_id") RequestBody member_id, @Part("type") RequestBody type,
                          @Part("title") RequestBody title, @Part("content") RequestBody content, @Part("date") RequestBody date,
                          @Part ArrayList<MultipartBody.Part> photo);

    @Multipart
    @POST("modyBoard.do")
    Call<Void> modyBoard(@Part("board_id") RequestBody board_id, @Part("title") RequestBody title,
                         @Part("content") RequestBody content, @Part ArrayList<MultipartBody.Part> photo);

    @GET("getBoardList.do")
    Call<ArrayList<BoardList>> getBoardList(@Query("type") Integer type, @Query("page") Integer page);

    @GET("getPicBoardList.do")
    Call<ArrayList<PicBoardList>> getPicBoardList(@Query("page") Integer page);

    @GET("getDetailBoard.do")
    Call<BoardFile> getBoardInfo(@Query("board_id") Long board_id, @Query("member_id") String id);

    @GET("searchBoard.do")
    Call<ArrayList<BoardList>> searchBoard(@Query("type") Integer type, @Query("search_type") Integer search_type, @Query("str_search") String str_search);

    @GET("getRankBoard.do")
    Call<ArrayList<Board>> getRankBoard(@Query("date") String date);

    @GET("delBoard.do")
    Call<Void> delBoard(@Query("board_id") Long board_id);

    @GET("delPic.do")
    Call<Void> delPic(@Query("member_id") String member_id);

    @FormUrlEncoded
    @POST("setHit.do")
    Call<Void> setHit(@Field("board_id") Long board_id, @Field("member_id") String member_id, @Field("hit") boolean hit);

    @GET("getHit.do")
    Call<Boolean> getHit(@Query("board_id") Long board_id, @Query("member_id") String member_id);

    @GET("hitBoard.do")
    Call<ArrayList<BoardList>> hitBoad(@Query("member_id") String member_id, @Query("type") Integer type);

    @GET("hitPicBoard.do")
    Call<ArrayList<PicBoardList>> hitPicBoard(@Query("member_id")String member_id);

    @FormUrlEncoded
    @POST("change_nick.do")
    Call<Boolean> change_nick(@Field("member_id") String member_id, @Field("change_nick") String change_nick);

    @FormUrlEncoded
    @POST("change_intro.do")
    Call<Void> change_intro(@Field("member_id") String member_id, @Field("change_intro") String change_intro);

    @FormUrlEncoded
    @POST("change_pw.do")
    Call<Boolean> change_pw(@Field("member_id") String member_id, @Field("now_pw") String now_pw, @Field("change_pw") String change_pw);

    @GET("getMessage.do")
    Call<ArrayList<MessageFile>> getMessage(@Query("member_id") String member_id, @Query("type") Integer type);

    @GET("delMessage.do")
    Call<Void> delMessage(@Query("message_id") Long message_id, @Query("type") Integer type);

    @GET("get_memberInfo_nick.do")
    Call<ArrayList<String>> get_memberInfo_nick(@Query("search_nick") String search_nick);

    @FormUrlEncoded
    @POST("send_message.do")
    Call<Void> send_message(@Field("writer_id") String user_id, @Field("reader_id") String reader_id, @Field("content") String content, @Field("m_date") String date);

    @GET("getHomePic.do")
    Call<ArrayList<String>> getHomePic();
}
