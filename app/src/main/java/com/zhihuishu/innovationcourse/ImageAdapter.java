package com.zhihuishu.innovationcourse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class ImageAdapter extends ArrayAdapter<Image> {

    public ImageAdapter(Context context, int resource, List<Image> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Image image = getItem(position);
        final View oneImageView = LayoutInflater.from(getContext()).inflate(R.layout.image_item, parent, false);

        final SmartImageView imageView = (SmartImageView) oneImageView.findViewById(R.id.small_imageView);
        TextView textView = (TextView) oneImageView.findViewById(R.id.img_desc_textView);

        imageView.setImageUrl(image.getImgUrl());
        textView.setText(image.getDesc());

        oneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ImageDetailActivity.class);

                imageView.setDrawingCacheEnabled(true);

                intent.putExtra ("image_imgUrl", image.getImgUrl());

                intent.putExtra("image_desc", image.getDesc());

                intent.putExtra ("image_time", image.getTime ());

                imageView.setDrawingCacheEnabled(false);


                getContext().startActivity(intent);
            }
        });

        return oneImageView;
    }
}






