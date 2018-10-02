package com.example.sss.team_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Messages {
    public Long id;
    public String w_id;
    public String r_id;
    public String content;
    public String m_date;
}
