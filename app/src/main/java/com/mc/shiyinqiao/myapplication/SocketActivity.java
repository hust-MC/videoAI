package com.mc.shiyinqiao.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SocketActivity extends Activity {

    private TextView textView = null;
    public static Bitmap sBitmap;
    private Canvas canvas = new Canvas();
    MyView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_socket);
//        myView = new MyView(this);//this 要在构造器完成才有
//        layout.addView(myView);
//        myView.setPosition(10,20,10,280,250,20,250,280);
//        myView.draw(canvas);

        Button button = (Button) findViewById(R.id.download);
        textView = (TextView) findViewById(R.id.print);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downLoad("http://172.24.198.34/mcqq.jpg", getLocalClassName() + "/cc.jpg");
                textView.setText(Environment.getExternalStorageDirectory().toString() + "下载完成");

            }
        });


    }

    public static void downLoad(final String path, final String FileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        FileOutputStream fileOutputStream = null;//文件输出

                        if (is != null) {
                            FileUtils fileutils = new FileUtils();//文件保存
                            fileOutputStream = new FileOutputStream(fileutils.creatFile(FileName));
                            byte[] buf = new byte[1024];
                            int ch;
                            while (((ch = is.read(buf)) != -1)) {
                                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
                loadImage();

            }
        }).start();
    }

    private static class FileUtils {//创建文件保存路径
        private String path = Environment.getExternalStorageDirectory().toString() + "/model";

        public FileUtils() {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }

        public File creatFile(String FileName) {
            return new File(path, FileName);
        }
    }

    private static void loadImage() {
        String path = Environment.getExternalStorageDirectory().toString() + "/model";
        try {
            sBitmap = BitmapFactory.decodeStream(new FileInputStream(new File(path, "mcqq.jpg")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}


