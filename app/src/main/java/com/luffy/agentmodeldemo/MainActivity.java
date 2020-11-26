package com.luffy.agentmodeldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.luffy.agentmodeldemo.api.MyWeatherApi;
import com.luffy.agentmodeldemo.api.WeatherApi;
import com.luffy.agentmodeldemo.retrofit.MyRetrofit;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button mBtnRetrofitGet;
    private Button mBtnRetrofitPost;
    private Button mBtnMyRetrofitGet;
    private Button mBtnMyRetrofitPost;
    private WeatherApi weatherApi;
    private MyWeatherApi myWeatherApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://restapi.amap.com").build();
        weatherApi = retrofit.create(WeatherApi.class);

        MyRetrofit myRetrofit = new MyRetrofit.Builder().baseUrl("https://restapi.amap.com").build();
        myWeatherApi = myRetrofit.create(MyWeatherApi.class);
    }

    private void initViews() {
        mBtnRetrofitGet = findViewById(R.id.btn_retrofit_get);
        mBtnRetrofitPost = findViewById(R.id.btn_retrofit_post);
        mBtnMyRetrofitGet = findViewById(R.id.btn_my_retrofit_get);
        mBtnMyRetrofitPost = findViewById(R.id.btn_my_retrofit_post);

        mBtnRetrofitGet.setOnClickListener(this);
        mBtnRetrofitPost.setOnClickListener(this);
        mBtnMyRetrofitGet.setOnClickListener(this);
        mBtnMyRetrofitPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_retrofit_get:
                getWeatherInfo();
                break;
            case R.id.btn_retrofit_post:
                postWeatherInfo();
                break;
            case R.id.btn_my_retrofit_get:
                getWeatherInfoByMyRetrofit();
                break;
            case R.id.btn_my_retrofit_post:
                postWeatherInfoByMyRetrofit();
                break;
            default:
                break;
        }
    }

    /**
     * Retrofit的GET请求获取天气信息
     */
    private void getWeatherInfo() {
        Call<ResponseBody> call = weatherApi.getWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    try {
                        String string = body.string();
                        Log.i(TAG, "GET请求-------onResponse: " + string);
                    } catch (IOException e) {
                        Log.e(TAG, e.getCause().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getCause().getMessage());
            }
        });
    }

    /**
     * Retrofit的POST请求获取天气信息
     */
    private void postWeatherInfo() {
        Call<ResponseBody> call = weatherApi.postWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    try {
                        String string = body.string();
                        Log.i(TAG, "POST请求-------onResponse: " + string);
                    } catch (IOException e) {
                        Log.e(TAG, e.getCause().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * MyRetrofit的GTT请求获取天气信息
     */
    private void getWeatherInfoByMyRetrofit() {
        okhttp3.Call call = myWeatherApi.getWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b");
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "我的GET请求 ------ onFailure: " + e);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i(TAG, "我的GET请求 ------ onResponse: " + response.body().string());
                }
            }
        });

    }

    /**
     * MyRetrofit的POST请求获取天气信息
     */
    private void postWeatherInfoByMyRetrofit() {
        okhttp3.Call call = myWeatherApi.postWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b");
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i(TAG, "我的POST请求 ------ onResponse: " + response.body().string());
                }
            }
        });
    }
}
