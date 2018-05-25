package com.zhihuishu.innovationcourse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Image {

    private String imgUrl;
    private String desc;

    public Image(String imgUrl, String desc) {
        this.imgUrl = imgUrl;
        this.desc = desc;
    }
    public String getImgUrl() { return imgUrl; }
    public String getDesc() { return desc; }
    public static List<Image> getAllImages(JSONArray arr) {
        List<Image> teachers = new ArrayList<Image>();

        for(int i =0; i < arr.length(); i++) {
            JSONObject obj = arr.optJSONObject (i);
            String str = obj.optString ("copyright").split ("\\(")[0].split ("（")[0];
            String url = "http://s.cn.bing.net" + obj.optString ("url");
            teachers.add (new Image (url, str));
        }
        return teachers;
    }
}
