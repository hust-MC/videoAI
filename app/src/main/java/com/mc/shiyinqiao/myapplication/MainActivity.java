package com.mc.shiyinqiao.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.shiyinqiao.myapplication.tensorflow.Classifier;
import com.mc.shiyinqiao.myapplication.tensorflow.TensorFlowObjectDetectionAPIModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * 取帧间隔
     */
    private static final long GET_FRAME_INTERVAL = 3000;
    /**
     * 获取帧的命令
     */
    private static final int WHAT_GET_FRAME = 1;
    private int mScreenWidth;
    private int mScreenHeight;
    private float ratioX, ratioY;
    TextureView mVideoView;
    MediaPlayer mPlayer = new MediaPlayer();
    private static final String TAG = "TAG";
    private List<Fruit> fruitList = new ArrayList<Fruit>();
    private String mVideoUrl = "";
    private static final String VIDEO_FILE_SCHEMA = "file";
    private static final String VIDEO_CONTENT_SCHEMA = "content";
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    public Bitmap listBitmap;
    private FruitAdapter adapter;
    ListView mListView;
    private Classifier detector;
    private MyView mBoundBox;
    /** 保留3位小数 */
    private final DecimalFormat df = new DecimalFormat("#.000");

//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//
//            Message message=new Message();
//            message.what=1;
//            handler.sendEmptyMessageDelayed(1,1000);
//            //   handler.postDelayed(runnable, 10000);
//        }
//    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_GET_FRAME:
                    getFrameInThread();//线程
                    break;
            }
            sendEmptyMessageDelayed(1, GET_FRAME_INTERVAL);
        }

    };
    private boolean mIsPaused;
    private FrameLayout mVideoHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoHolder = findViewById(R.id.video_holder);
        mListView = (ListView) findViewById(R.id.list_item);
        Button buttondown = (Button) findViewById(R.id.downModel);
        ImageView adImageView = (ImageView) findViewById(R.id.ad_imageview);
        TextView adName = findViewById(R.id.ad_name);
        TextView adUrl = findViewById(R.id.ad_url);
        LinearLayout adViewLayout = findViewById(R.id.adViewLayout);
        mBoundBox = findViewById((R.id.bounding_box));

        //     LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.adview_layout, null);

        // 初始化检测器
        try {
            detector = TensorFlowObjectDetectionAPIModel.create(
                    getAssets(), Classifier.MODEL_FILE, Classifier.LABELS_FILE, Classifier.INPUT_SIZE);
        } catch (IOException e) {
            Log.e("MC_LOG", "文件不存在！！");
        }

        initFruits();//初始化

        adapter = new FruitAdapter(MainActivity.this, R.layout.fruit_item, fruitList);

        mListView.setAdapter(adapter);


        handleIntent();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();
        mVideoView = findViewById(R.id.video);
        //线程？？？？就一次？？
        //没有用？？？？


        handler.sendEmptyMessage(1);
        // 这里是开广播的方法
        //        Intent intent = new Intent(this, AutoUpdatedService.class);
        //        startService(intent);

        //定时取视频帧
        switch (2) {
            //左下——右下，右上——左上？ 2->1 4->3
            case 1:
                FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                lp1.gravity = Gravity.LEFT|Gravity.TOP;
                adViewLayout.setLayoutParams(lp1);
                break;
            case 2:
                FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                lp2.gravity = Gravity.RIGHT|Gravity.TOP;
                adViewLayout.setLayoutParams(lp2);
                break;
            case 3:
                FrameLayout.LayoutParams lp3 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                lp3.gravity = Gravity.LEFT| Gravity.BOTTOM;
                adViewLayout.setLayoutParams(lp3);

            case 4:
                FrameLayout.LayoutParams lp4 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                lp4.gravity = Gravity.RIGHT | Gravity.BOTTOM;

                adViewLayout.setLayoutParams(lp4);
        }

        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsPaused) {
                    mPlayer.start();
                    mIsPaused = false;
                } else {
                    mPlayer.pause();
                    mIsPaused = true;
                }
            }
        });
        // 一定要在setOnclickListener之后
        mVideoView.setClickable(false);
        buttondown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SocketActivity.class));

            }
        });

        mVideoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, final int width, int height) {
                Log.d("MC", "textureAvailable");
                Surface videoSurface = new Surface(surface);
                mPlayer.setSurface(videoSurface);


                PlayerListener listener = new PlayerListener();
                // 注册OnPrepared回调，告诉播放器Prepare好之后该干嘛
                mPlayer.setOnPreparedListener(listener);
                mPlayer.setOnCompletionListener(listener);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                Log.d("MC", "textureAvailable");

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                Log.d("MC", "textureAvailable");

                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }

        });
    }


    class PlayerListener implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            int width = mScreenWidth;
            int height = mp.getVideoHeight() * width / mp.getVideoWidth();//mp播放器知道宽高
            //针对竖屏视频
            if (height >= width) {
                height = 7 * height / 10;
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            mVideoView.setLayoutParams(params);

            LinearLayout.LayoutParams holderParam = new LinearLayout.LayoutParams(width, height);
            mVideoHolder.setLayoutParams(holderParam);

            mPlayer.start();
            mVideoView.setClickable(true);
        }

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            handler.removeMessages(WHAT_GET_FRAME);
        }
    }

    private void getFrameInThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFrame();
            }
        }).start();//线程里 runnable的
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null)
            mPlayer.release();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.getScheme() != null && intent.getData() != null) {
            if (intent.getScheme().equals(VIDEO_FILE_SCHEMA)) {// video file schema
                mVideoUrl = intent.getData().getPath();
            }

            if (intent.getScheme().equals(VIDEO_CONTENT_SCHEMA)) {
                mVideoUrl = getFilePathFromMediaStore(intent.getData());
            }
        }
        try {
            mPlayer.setDataSource(mVideoUrl);//播放器状态机
            mPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getFilePathFromMediaStore(Uri dataUri) {
        if (dataUri == null) {
            return null;
        }

        Cursor cursor = null;
        final String column = MediaStore.Video.Media.DATA;
        final String[] projection = {column};
        int columnIdx;

        try {
            cursor = getContentResolver().query(dataUri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                columnIdx = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIdx);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }


    private void getFrame() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Accept-Encoding", "gzip,deflate,sdch");
            params.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;" +
                    "q=0.8");
            mediaMetadataRetriever.setDataSource(mVideoUrl, params);
            //获取图片
            //这里是取整个视频的缩略图
            //      listBitmap = ThumbnailUtils.createVideoThumbnail(mVideoUrl, MediaStore.Video.Thumbnails
            // .MICRO_KIND);

            listBitmap = mediaMetadataRetriever.getFrameAtTime(mPlayer.getCurrentPosition() * 1000,
                    MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);//为什么只有一帧
            Classifier.Recognition result = null;
            try {
                Bitmap bitmap = Bitmap.createScaledBitmap(
                        listBitmap, Classifier.INPUT_SIZE, Classifier.INPUT_SIZE, false);
                result = detector.recognizeImage(bitmap).get(0);
            } catch (Exception e) {
                Log.e("MC_LOG", "detect fail");
            }
            //将图片保存到相册里
            fruitList.add(new Fruit(
                    fruitList.size() + 1, listBitmap, result.getTitle(), df.format(result.getConfidence())));


            final Classifier.Recognition finalResult = result;
            runOnUiThread((new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    // 平滑滑动到列表底部
                    mListView.smoothScrollToPosition(fruitList.size() - 1);
                    mBoundBox.setPosition(finalResult.getLocation());
                    mBoundBox.draw(new Canvas());
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mediaMetadataRetriever.release();
            if (listBitmap != null) {
//                listBitmap.recycle();
            }

        }

    }

    private void saveBitmapGallery(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory().toString() + "/model";

        File fileDir = new File(path);

        if (!fileDir.exists()) {
            fileDir.mkdirs();//文件夹
        }
        //文件图片
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(fileDir, fileName);//(File parent, String child)
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos);//fos 输出数据流，//把位图输出到指定的文件中

            listBitmap = BitmapFactory.decodeStream(new FileInputStream(new File(path, fileName)));
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    private class getFrameMessage extends Handler {
//        public void handleMessage(Message msg) {
//            if (msg.what == GET_FRAME_MESSAGE) {
//                handler.postDelayed(runnable, 1000);
//            }
//
//        }
//
//        @Override
//        public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
//            Message msg1=Message.obtain();
//            msg.what=what;
//            return super.sendMessageAtTime(msg, uptimeMillis);
//        }
//    }

    private void initFruits() {
//        Fruit apple = new Fruit("apple", SocketActivity.sBitmap, 0.5, 0.02);
//        fruitList.add(apple);
//        Fruit banana = new Fruit("Banana", SocketActivity.sBitmap, 1, 2);
//        fruitList.add(banana);
//        Fruit orange = new Fruit("orange", SocketActivity.sBitmap, 0.3, 0.2);
//        fruitList.add(orange);
        //   Fruit watermelon = new Fruit("Watermelon", Bitmap.createBitmap(R.mipmap.ic_launcher), 0.5, 0.9);
//        fruitList.add(watermelon);
//        Fruit pear = new Fruit("Pear", R.mipmap.ic_launcher, 0.5, 0.8);
//        fruitList.add(pear);
//        Fruit grape = new Fruit("Grape", R.mipmap.ic_launcher, 0.2, 0.1);
//        fruitList.add(grape);
//        Fruit pineapple = new Fruit("Pineapple", R.mipmap.ic_launcher, 0.1, 0.9);
//        fruitList.add(pineapple);
//        Fruit strawberry = new Fruit("Strawberry", R.mipmap.ic_launcher, 0.88, 0.22);
//        fruitList.add(strawberry);
//        Fruit cherry = new Fruit("Cherry", R.mipmap.ic_launcher, 0.92, 0.36);
//        fruitList.add(cherry);
//        Fruit mango = new Fruit("Mango", R.mipmap.ic_launcher, 0.87, 0.25);
//        fruitList.add(mango);

    }


}
