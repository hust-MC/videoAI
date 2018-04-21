package com.mc.shiyinqiao.myapplication;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shiyinqiao on 2018/4/17.
 */

public class FruitAdapter extends ArrayAdapter {
    private final int resourceId;

    public FruitAdapter(@NonNull Context context, @LayoutRes int resource, List<Fruit> objects) {
        super(context, resource, objects);
        this.resourceId = resource;

    }

    class ViewHolder {
        ImageView fruitImage;
        TextView fruitname;
        TextView location;
        TextView confidence;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fruit fruit = (Fruit) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            viewHolder.fruitname = (TextView) view.findViewById(R.id.name);
            viewHolder.location = (TextView) view.findViewById(R.id.location);
            viewHolder.confidence = (TextView) view.findViewById(R.id.confidence);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


      //  viewHolder.fruitImage.setImageBitmap(MainActivity.listBitmap);
        viewHolder.fruitImage.setImageBitmap(fruit.getImageBitmap());
        viewHolder.fruitname.setText(fruit.getIndex() + "");
        viewHolder.location.setText((fruit.getLocation()) + "");
        viewHolder.confidence.setText(fruit.getConfident() + "");
        return view;
    }
}
