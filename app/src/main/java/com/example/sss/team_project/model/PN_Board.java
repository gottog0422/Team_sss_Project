package com.example.sss.team_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PN_Board {
    private Long pre_id;
    private Long next_id;

    private String pre_title;
    private String next_title;

}
