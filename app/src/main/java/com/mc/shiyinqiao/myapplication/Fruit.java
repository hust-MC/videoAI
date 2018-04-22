package com.mc.shiyinqiao.myapplication;

import android.graphics.Bitmap;

/**
 * Created by shiyinqiao on 2018/4/17.
 */

public class Fruit {
    private int index;
    private Bitmap imageBitmap;
    private String label;
    private  String confident;
    public  Fruit(int index, Bitmap imageBitmap, String label, String confident){
        this.index = index;
        this.imageBitmap=imageBitmap;
        this.label =label;
        this.confident=confident;
    }


    public int getIndex() {
        return index;
    }
    public  Bitmap getImageBitmap(){
        return imageBitmap;
    }
    public  String getLabel(){
        return label;
    }
    public  String getConfident(){
        return confident;
    }

}
