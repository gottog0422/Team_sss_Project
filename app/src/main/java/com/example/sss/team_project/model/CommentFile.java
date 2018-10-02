package com.example.sss.team_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentFile {
    private Long id;
    private Long board_id;
    private String member_id;
    private String member_nick;
    private String comment;
    private String member_pic;
    private String mDate;
}
