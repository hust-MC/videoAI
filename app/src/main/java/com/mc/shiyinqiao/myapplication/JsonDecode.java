package com.mc.shiyinqiao.myapplication;

import android.graphics.RectF;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;

/**
 * Created by shiyinqiao on 2018/4/24.
 */

public class JsonDecode {
    //    String jsontest = "{\n" + "\"id\":2, \"name\":\"金鱼\", \n" + "\t\"price\":12.3, \n" + "\"imagePath\":\"http://blog.csdn.net/qq_29269233/L05_Server/images/f1.jpg\"\n" +
//            "}\n";
//    String jsontest = "{\n" + "\"j_name\":ss, \"j_conficence\":\"0.987\", \n" + "\t\"j_time\":\"2018090987\", \n" + "\"j_imgurl\":\"http://blog.csdn.net/qq_29269233/L05_Server/images/f1.jpg\"\n" + "\"j_adurl\":\"http://blog.csdn.net/qq_29269233/L05_Server/images/f1.jpg\"\n" + "}\n";

    public static String decode(String jsontest) {
        VideoInfo videoinfo = null;
        try {
            JSONObject jsonObject1 = new JSONObject(jsontest);
//            JSONObject jsonObjectAdd=new JSONObject();
//            jsonObjectAdd.put("name", "qq");
//            jsonObjectAdd.put("confidence", "0.94");
//            jsonObjectAdd.put("time", "2015111");
//            jsonObjectAdd.put("img", "http");
//            jsonObjectAdd.put("adurl", "heep");
//            jsonObjectAdd.toString();
//            jsonObjectAdd.toString()

            String jname = jsonObject1.optString("j_name");
            String jconfidence = jsonObject1.optString("j_confidence");
            String jtime = jsonObject1.optString("j_time");
            String jimageurl = jsonObject1.optString("j_imgurl");
            String jadurl = jsonObject1.optString("j_adurl");
            //封装
            videoinfo = new VideoInfo(jname, jconfidence, jtime, jimageurl, jadurl);
            Log.d("syq", videoinfo.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        return (videoinfo.getName() + videoinfo.getConfidence() + videoinfo.getTime()) + videoinfo.getImageurl() + videoinfo.getAdurl() + "";
        return videoinfo.getName() +"/n"+ videoinfo.getConfidence() +"/n"+ videoinfo.getTime() + "/n"+videoinfo.getImageurl() + videoinfo.getAdurl() + "";
    }

    private static class VideoInfo {
        private String name;
        private String confidence;
        private String time;
        private String imageurl;
        private String adurl;

        public VideoInfo(String jname, String jconfidence, String jtime, String jimageurl, String jadurl) {
            name = jname;
            confidence = jconfidence;
            time = jtime;
            imageurl = jimageurl;
            adurl = jadurl;

        }

        public String getName() {
            return name;
        }

        public String getConfidence() {
            return confidence;
        }

        public String getTime() {
            return time;
        }

        public String getImageurl() {
            return imageurl;
        }

        public String getAdurl() {
            return adurl;
        }
    }
}

