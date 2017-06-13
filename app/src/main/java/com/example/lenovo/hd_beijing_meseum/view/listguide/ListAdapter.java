package com.example.lenovo.hd_beijing_meseum.view.listguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.model.Exhibition;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by lenovo on 2016/11/2.
 */

public class ListAdapter extends BaseAdapter {
    private List<Exhibition> list;
    private Context context;
    private String path;

    public ListAdapter(Context context, List<Exhibition> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder holder = null;
        if (convertView == null) {
            holder = new ListViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview, null);
            holder.textView = (TextView) convertView.findViewById(R.id.name_listviewItem);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_listview);
            convertView.setTag(holder);
        } else {
            holder = (ListViewHolder) convertView.getTag();
        }
        holder.textView.setText((position + 1) + "-" + list.get(position).getName());
        path = Constant.getDefaultFileDir() + "CHINESE" + "/" + list.get(position).getFileNo() + "/" + list.get(position).getFileNo() + ".png";
        Glide.with(context).load(path).placeholder(R.mipmap.down).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        return convertView;
    }

    public static class ListViewHolder {
        private TextView textView;
        private ImageView imageView;

    }
}
