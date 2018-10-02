package com.example.sss.team_project;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sss.team_project.adapter.HomeRankAdapter;
import com.example.sss.team_project.bus.BusProvider;
import com.example.sss.team_project.event.ChangePro;
import com.example.sss.team_project.model.Board;
import com.example.sss.team_project.retrofit.RetrofitService;
import com.example.sss.team_project.utills.PreferenceUtil;
import com.example.sss.team_project.utills.Utils;
import com.example.sss.team_project.weather.WeatherData;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.bt_menu)
    Button bt_menu;
    @BindView(R.id.iv_weather)
    ImageView iv_weather;
    @BindView(R.id.tv_weather_info1)
    TextView tv_weather_info1;
    @BindView(R.id.tv_weather_info2)
    TextView tv_weather_info2;
    @BindView(R.id.iv_member_pic)
    ImageView iv_member_pic;
    @BindView(R.id.tv_member_nick)
    TextView tv_member_nick;
    @BindView(R.id.rl_logout)
    RelativeLayout rl_logout;
    @BindView(R.id.rl_setting)
    RelativeLayout rl_setting;
    @BindView(R.id.rl_freeBoard)
    RelativeLayout rl_freeBoard;
    @BindView(R.id.rl_my)
    RelativeLayout rl_my;
    @BindView(R.id.rl_parcelBoard)
    RelativeLayout rl_parcelBoard;
    @BindView(R.id.bt_freeboard)
    Button bt_freeboard;
    @BindView(R.id.bt_weather_refresh)
    Button bt_weather_refresh;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.bt_parcelBoard)
    Button bt_paracelBoard;
    @BindView(R.id.lv_rank)
    ListView lv_rank;
    @BindView(R.id.rl_w_message)
    RelativeLayout rl_w_message;
    @BindView(R.id.no_rank)
    TextView no_rank;
    @BindView(R.id.rl_picBoard)
    RelativeLayout rl_picBoard;

    @BindView(R.id.iv_0)
    ImageView iv_0;
    @BindView(R.id.iv_1)
    ImageView iv_1;
    @BindView(R.id.iv_2)
    ImageView iv_2;
    String id;
    Bus bus = BusProvider.getInstance().getBus();
    LocationManager lm;

    ArrayList<Board> items = new ArrayList<>();
    HomeRankAdapter homeRankAdapter;

    final static int RANKCODE = 8540;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        bus.register(this);

        id = PreferenceUtil.getInstance(HomeActivity.this).getStringExtra("id");

        setWeather();
        setMemberInfo(id);
        setRankBoard();
        getHomePic();

    }

    public class WeatherTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String Json = Utils.getStringFromServer(strings[0]);
            return Json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();

            WeatherData weather = gson.fromJson(s, WeatherData.class);

            double temp = weather.getMain().getTemp() - 273.15;
            String t_temp = String.format("%.1f", temp);
            double wind = weather.getWind().getSpeed();
            String t_wind = String.format("%.1f", wind);
            double humidity = weather.getMain().getHumidity();
            String t_humidity = String.format("%.0f", humidity);
            String cloud = weather.getClouds().getAll().toString();

            GlideApp.with(HomeActivity.this).load("http://openweathermap.org/img/w/"
                    + weather.getWeather().get(0).getIcon() + ".png").into(iv_weather);
            tv_weather_info1.setText("온도 : " + t_temp + "°c" + "\n풍량 : " + t_wind + "m/s");
            tv_weather_info2.setText("구름 : " + cloud + "%" + "\n습도 : " + t_humidity + "%");
        }
    }

    public void setWeather() {
        SharedPreferences pref = getSharedPreferences("weather", MODE_PRIVATE);
        String lon = pref.getString("lon", "");
        String lat = pref.getString("lat", "");

        if (lon.equals("") && lat.equals("")) {
            tv_weather_info1.setText("날씨 정보를 받기위해\n위치를 갱신해주세요!");
        } else {
            String api_key = "a3d6e9a73ea038bfe441a78a954e9f77";
            WeatherTask jason = new WeatherTask();

            jason.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + api_key);
        }
    }

    public void setRankBoard() {
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);

        SimpleDateFormat mFormat = new SimpleDateFormat("yy.MM.dd hh:mm");
        String date = mFormat.format(mDate);

        Call<ArrayList<Board>> observ = RetrofitService.getInstance().getRetrofitRequest().getRankBoard(date);
        observ.enqueue(new Callback<ArrayList<Board>>() {
            @Override
            public void onResponse(Call<ArrayList<Board>> call, Response<ArrayList<Board>> response) {
                if (response.isSuccessful()) {
                    items = response.body();
                    homeRankAdapter = new HomeRankAdapter(items);

                    lv_rank.setAdapter(homeRankAdapter);
                    if (items.size() == 0) {
                        no_rank.setVisibility(View.VISIBLE);
                    } else {
                        no_rank.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Board>> call, Throwable t) {

            }
        });
    }

    @OnItemClick(R.id.lv_rank)
    public void rank_itemClick(AdapterView<?> parent, final int i) {
        Board item = items.get(i);
        Intent intent = new Intent(HomeActivity.this, DetailBoardActivity.class);
        intent.putExtra("board_id", item.getId());
        startActivityForResult(intent, RANKCODE);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    public void setMemberInfo(String id) {
        Call<ArrayList<String>> observ = RetrofitService.getInstance().getRetrofitRequest().get_memberInfo(id);
        observ.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful()) {
                    ArrayList<String> member_items = response.body();
                    tv_member_nick.setText(member_items.get(0).toString());

                    if (member_items.get(1) == null || member_items.get(1).equals("")) {
                        Drawable memberpic_null = getResources().getDrawable(R.drawable.memberpic_null);
                        iv_member_pic.setImageDrawable(memberpic_null);
                    } else {
                        String url = "http://10.0.2.2:8090/sss/resources/profilepic/";
                        GlideApp.with(HomeActivity.this)
                                .load(url + member_items.get(1)).centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(iv_member_pic);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
            }
        });
    }

    public void getHomePic() {
        Call<ArrayList<String>> observ = RetrofitService.getInstance().getRetrofitRequest().getHomePic();
        observ.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful()) {
                    ArrayList<String> picitem = response.body();
                    String url = "http://10.0.2.2:8090/sss/resources/upload/";
                    if (!picitem.get(0).equals("null")) {
                        GlideApp.with(HomeActivity.this)
                                .load(url + picitem.get(0)).centerCrop()
                                .into(iv_0);
                    }
                    if (!picitem.get(1).equals("null")) {
                        GlideApp.with(HomeActivity.this)
                                .load(url + picitem.get(1)).centerCrop()
                                .into(iv_1);
                    }
                    if (!picitem.get(2).equals("null")) {
                        GlideApp.with(HomeActivity.this)
                                .load(url + picitem.get(2)).centerCrop()
                                .into(iv_2);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
            }
        });
    }

    @OnClick(R.id.bt_menu)
    public void onclick_menu() {
        drawer_layout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.rl_logout)
    public void logout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
        dialog.setTitle(R.string.logout).setMessage("정말 로그아웃 하시겠습니까?")
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferenceUtil.getInstance(HomeActivity.this).removePreference("id");
                        Intent intent = new Intent(HomeActivity.this, LoginChoiceActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).create().show();
    }

    @OnClick(R.id.rl_setting)
    public void go_setting() {
        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
        startActivity(intent);
        HomeActivity.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);

    }

    @OnClick(R.id.rl_w_message)
    public void go_w_message() {
        Intent intent = new Intent(HomeActivity.this, MessageActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
        HomeActivity.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);

    }

    @OnClick(R.id.rl_r_message)
    public void go_r_message() {
        Intent intent = new Intent(HomeActivity.this, MessageActivity.class);
        intent.putExtra("type", 0);
        startActivity(intent);
        HomeActivity.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);

    }

    @OnClick(R.id.rl_picBoard)
    public void go_picBoard() {
        Intent intent = new Intent(HomeActivity.this, PicBoardActivity.class);
        startActivity(intent);
        HomeActivity.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    @OnClick(R.id.rl_my)
    public void go_my() {
        Intent intent = new Intent(HomeActivity.this, MyActivity.class);
        startActivity(intent);
        HomeActivity.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    @OnClick({R.id.bt_freeboard, R.id.rl_freeBoard})
    public void go_freeboard() {
        Intent intent = new Intent(HomeActivity.this, BoardActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
        HomeActivity.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    @OnClick({R.id.rl_parcelBoard, R.id.bt_parcelBoard})
    public void go_paracelboard() {
        Intent intent = new Intent(HomeActivity.this, BoardActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
        HomeActivity.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    @OnClick(R.id.bt_weather_refresh)
    public void weather_refresh() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Toast.makeText(HomeActivity.this, "기기 위치 기능을 사용 설정하세요", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                } else {
                    try {
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
                    } catch (SecurityException ex) {

                    }
                }

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한을 주지 않으시면 위치정보를 갱신 할 수 없습니다.\n\n[설정] > [권한] 에서 권한을 주세요!")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            double lon = location.getLongitude(); //경도
            double lat = location.getLatitude();   //위도

            SharedPreferences pref = getSharedPreferences("weather", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("lon", lon + "");
            editor.commit();
            editor.putString("lat", lat + "");
            editor.commit();

            setWeather();
            Toast.makeText(HomeActivity.this, "갱신 되었습니다!", Toast.LENGTH_SHORT).show();

            lm.removeUpdates(this);
        }

        public void onProviderDisabled(String provider) {

        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    @Subscribe
    public void changePro(ChangePro event) {
        setMemberInfo(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}