package com.mc.shiyinqiao.myapplication;

import android.graphics.Bitmap;

/**
 * Created by shiyinqiao on 2018/4/17.
 */

public class Fruit {
    private int index;
    private Bitmap imageBitmap;
    private double location;
    private  double confident;
    public  Fruit(int index, Bitmap imageBitmap, double location, double confident){
        this.index = index;
        this.imageBitmap=imageBitmap;
        this.location=location;
        this.confident=confident;
    }


    public int getIndex() {
        return index;
    }
    public  Bitmap getImageBitmap(){
        return imageBitmap;
    }
    public  double getLocation(){
        return location;
    }
    public  double getConfident(){
        return confident;
    }

}
