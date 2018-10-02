package com.example.sss.team_project.weather;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WeatherData {
    w_cloud clouds;
    w_main main;
    w_wind wind;
    ArrayList<w_weather> weather;
}
