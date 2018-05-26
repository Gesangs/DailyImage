package com.zhihuishu.innovationcourse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Image {

    private String imgUrl;
    private String desc;
    private String time;

    public Image(String imgUrl, String desc, String time) {
        this.imgUrl = imgUrl;
        this.desc = desc;
        this.time = time;
    }
    public String getImgUrl() { return imgUrl; }
    public String getDesc() { return desc; }
    public String getTime() { return time; }
    public static List<Image> getAllImages(JSONArray arr) {
        List<Image> teachers = new ArrayList<Image>();

        for(int i =0; i < arr.length(); i++) {
            JSONObject obj = arr.optJSONObject (i);
            String str = obj.optString ("copyright").split ("\\(")[0].split ("（")[0];
            String url = "http://s.cn.bing.net" + obj.optString ("url");

//            将"YYYYMMDD"格式的时间转化为"YYYY-MM-DD"
            String time = obj.optString ("enddate");
            StringBuilder sb = new StringBuilder (time);
            sb.insert (6, "-").insert (4, "-").toString ();
            time = sb.toString ();
            str = str + "（" + time + "）";
            teachers.add (new Image (url, str, time));
        }
        return teachers;
    }
}
