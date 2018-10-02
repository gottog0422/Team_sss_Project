package com.example.sss.team_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Board {
    private Long id;
    private Integer type;
    private String title;
    private String content;
    private Integer hit;
    private String writer_id;
    private String mdate;
    private Integer count;
}
