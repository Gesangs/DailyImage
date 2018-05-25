package com.zhihuishu.innovationcourse;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends ActionBarActivity {

    private static final int change = 1;
    private static final int error = -1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == change) {
                String js = (String) msg.obj;
                try {
//                    解析json数据，提取图片url
                    JSONObject jsonObj = new JSONObject (js);
                    JSONArray images = jsonObj.optJSONArray ("images");
                    ImageAdapter imageAdapter = new ImageAdapter(MainActivity.this, R.layout.image_item, Image.getAllImages(images));
                    ListView listView = (ListView) findViewById(R.id.image_listView);
                    listView.setAdapter(imageAdapter);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }else if (msg.what == error){
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getImageUrlList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    获取图片url列表（来源于必应每日一图）
    public void getImageUrlList() {
        new Thread() {
            private HttpURLConnection conn;
            @Override
            public void run() {
                try {
                    URL url = new URL("http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=30");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStreamReader is = new InputStreamReader(conn.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(is);
                        String result = "";
                        String readLine = null;
                        while ((readLine = bufferedReader.readLine()) != null) {
                            result = readLine;
                        }
                        Message msg = new Message();
                        msg.what = change;
                        msg.obj = result;
                        handler.sendMessage(msg);
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = error;
                    handler.sendMessage(msg);
                }
                conn.disconnect();
            }
        }.start();
    }
}
