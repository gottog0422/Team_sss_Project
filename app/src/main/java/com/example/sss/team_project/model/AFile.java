package com.example.sss.team_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AFile {
    private Long id;
    private String File_name;
    private Long Board_id;

}
