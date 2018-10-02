package com.example.sss.team_project.model;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardFile {
    private Board board;
    private ArrayList<AFile> files;
    private String writer_pic;
    private String writer_nick;
    private PN_Board pn_board;
    private Integer comment_count;



}
