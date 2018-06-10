package com.zhihuishu.innovationcourse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.image.SmartImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageDetailActivity extends ActionBarActivity {

    private static final int change = 1;
    private static final int error = -1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == change) {
                Bitmap bitmap = (Bitmap) msg.obj;
                String time = getIntent ().getStringExtra ("image_time");
                saveImage (bitmap, time);
            }else if (msg.what == error){
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_image_detail);

        final String url = getIntent ().getStringExtra ("image_imgUrl");
        String desc = getIntent ().getStringExtra ("image_desc");

        final SmartImageView imageView = (SmartImageView) findViewById (R.id.large_imageView);
        TextView textView = (TextView) findViewById (R.id.image_desc_textView);

        imageView.setImageUrl (url);
        textView.setText (desc);

//        长按图片保存
        imageView.setOnLongClickListener (new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText (ImageDetailActivity.this, "正在保存", Toast.LENGTH_SHORT).show ();
                downLoadImage (url);
                return true;
            }
        });

//        单击图片关闭当前页
        imageView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });
    }

//    下载高清大图，完成后通知主线程，保存到本地
    public void downLoadImage(final String u) {
        new Thread() {
            private HttpURLConnection conn;
            private Bitmap bitmap;
            @Override
            public void run() {
                try {
                    URL url = new URL(u);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream (is);
                        Message msg = new Message();
                        msg.what = change;
                        msg.obj = bitmap;
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

//    保存图片到本地
    private void saveImage(Bitmap bitmap, String name){
        String fileName ;
        File file ;
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + name +".jpeg";
        file = new File(fileName);

        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out))
            {
                out.flush();
                out.close();
                // 插入图库
                MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), name + ".jpeg", null);
            }
        }
        catch (FileNotFoundException e)
        {
            Toast.makeText (this, "保存失败", Toast.LENGTH_SHORT).show ();
            e.printStackTrace();
            return;
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        // 发送广播，通知刷新图库的显示
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        Toast.makeText (this, "保存成功", Toast.LENGTH_SHORT).show ();

    }
}
