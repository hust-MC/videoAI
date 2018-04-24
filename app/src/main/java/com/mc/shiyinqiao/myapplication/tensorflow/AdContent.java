package com.mc.shiyinqiao.myapplication.tensorflow;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shiyinqiao on 2018/4/23.
 */

public class AdContent {
    private String admapurl;
    private String admapname;
    private String poster;

    private static Map<String, String> adMap = new HashMap();
    // 定义广告跳转链接
    static {
        adMap.put("cat", "https://detail.tmall.com/item.htm?spm=a230r.1.14.84.1c324962kZFYEG&id=546644619506&ns=1&abbucket=13");
        adMap.put("dog", "https://item.taobao.com/item.htm?spm=a230r.1.14.68.24a73a91lQgWq2&id=565955437956&ns=1&abbucket=13#detail");
        adMap.put("apple", "https://detail.tmall.com/item.htm?spm=a230r.1.14.34.f0356126i1b4Ny&id=530995583913&ns=1&abbucket=13");
    }

    // 定义广告图片
    private static Map<String,String> adPicUrl =new HashMap();
    static {
        adPicUrl.put("cat","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523984368471&di=4a1885969f1a6327d2c04bb3eabb44f2&imgtype=0&src=http%3A%2F%2Fpic25.nipic.com%2F20121208%2F7994548_115320262106_2.jpg");
        adPicUrl.put("dog","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523985415055&di=dcd1c343f083dbcb278f409d31d16dc0&imgtype=0&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F120620%2F201981-12062013594895.jpg");
        adPicUrl.put("apple","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523985885579&di=9e46af1538b3c171dd0c2501aabe916d&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Ftransform%2F20150605%2FDSfW-crvvpkk7981499.jpg");

    }

    public static String getADUrl(String name) {
        if (adMap.containsKey(name)) {
            return adMap.get(name);
        }
        return "无链接匹配";
    }

    public static String getADPic(String name) {
        if (adPicUrl.containsKey(name)) {
            return adPicUrl.get(name);
        }
        return "无图片匹配";
    }
}
