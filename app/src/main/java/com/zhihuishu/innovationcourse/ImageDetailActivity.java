package com.zhihuishu.innovationcourse;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;


public class ImageDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_image_detail);

        String url = getIntent ().getStringExtra ("image_imgUrl");
        String desc = getIntent ().getStringExtra ("image_desc");

        SmartImageView imageView = (SmartImageView) findViewById (R.id.large_imageView);
        TextView textView = (TextView) findViewById (R.id.image_desc_textView);

        imageView.setImageUrl (url);
        textView.setText (desc);
    }

//    点击任意处关闭当前页
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            finish ();
        }
        return super.dispatchTouchEvent(ev);
    }

}
