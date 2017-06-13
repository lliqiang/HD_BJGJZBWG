package com.example.lenovo.hd_beijing_meseum.view;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.model.Exhibition;
import com.example.lenovo.hd_beijing_meseum.model.ResponseBody;
import com.example.lenovo.hd_beijing_meseum.serveinterface.ScanInterface;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceDB;
import com.example.lenovo.hd_beijing_meseum.view.collect.CollectDetail;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CollectView extends BaseActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ImageView toback;
    private TextView title;
    private String path;
    private List<Exhibition> exhibitionList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private CommonAdapter<Exhibition> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_view);
        initView();
        initData();
        setListner();
    }

    private void setListner() {
        toback.setOnClickListener(this);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.collect_recyclerview);
        toback = (ImageView) findViewById(R.id.toback);
        title = (TextView) findViewById(R.id.title);
        title.setText("馆藏精品");
        layoutManager = new GridLayoutManager(CollectView.this, 2, OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter = new CommonAdapter<Exhibition>(CollectView.this, R.layout.item_gridview, exhibitionList) {
            @Override
            public void convert(ViewHolder holder, Exhibition exhibition) {
                holder.setText(R.id.name_collect, exhibition.getName());
                ImageView imageView = holder.getView(R.id.image_collect);
                path = Constant.getDefaultFileDir()  + "CHINESE" + "/" + exhibition.getFileNo() + "/" + exhibition.getFileNo() + ".png";
                Glide.with(CollectView.this).load(path).placeholder(R.mipmap
                        .down).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener<Exhibition>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Exhibition exhibition, int position) {

                StartPlay(exhibition);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Exhibition exhibition, int
                    position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void StartPlay(Exhibition exhibition) {
        Intent intent = new Intent(CollectView.this, CollectDetail.class);
        String fileNo=exhibition.getFileNo();

        retrofitCount(fileNo,1);
        intent.putExtra("exhibition", exhibition);
        startActivity(intent);
    }
    private void retrofitCount(String fileNo,int type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.200.234.14:8091/bjgdjzbwg/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();
        ScanInterface scanInterface = retrofit.create(ScanInterface.class);
        //第五步：通过实体调用请求方法，获取Call对象
        Call<ResponseBody> call = scanInterface.getResult(fileNo, type,getSharedPreferences("sp",MODE_PRIVATE).getString("AND","mull"));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void initData() {
        exhibitionList.clear();
        Cursor cursor = HResourceDB.getInstance().loadResByIsBoutique(1);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Exhibition exhibit = Exhibition.Cursor2Model(cursor);
                exhibitionList.add(exhibit);
            }
            adapter.notifyDataSetChanged();
            cursor.close();
        }

    }

    @Override
    public void onClick(View v) {
        finish();

    }
}
