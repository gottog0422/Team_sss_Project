package com.example.sss.team_project.model;

import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WriteContent {
    private String item_str;
    private Uri item_iv;
}
