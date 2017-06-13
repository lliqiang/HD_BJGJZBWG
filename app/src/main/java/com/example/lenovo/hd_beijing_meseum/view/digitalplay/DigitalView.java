package com.example.lenovo.hd_beijing_meseum.view.digitalplay;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.model.Exhibition;
import com.example.lenovo.hd_beijing_meseum.model.ResponseBody;
import com.example.lenovo.hd_beijing_meseum.serveinterface.ScanInterface;
import com.example.lenovo.hd_beijing_meseum.utils.CommonUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceDB;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.ILoadListener;
import com.example.lenovo.hd_beijing_meseum.view.GuideService;
import com.example.lenovo.hd_beijing_meseum.view.listguide.ListGuideView;
import com.example.lenovo.hd_beijing_meseum.view.play.Play;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DigitalView extends BaseActivity implements View.OnClickListener {
    private EditText editText;
    private TextView title;
    private ImageView searchImg;
    private RecyclerView recyclerView;
    private List<Exhibition> exhibitList = new ArrayList<>();
    private ImageView toBack;
    private CommonAdapter<Exhibition> adapter;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_view);
        initView();
        initData();
        toBack.setOnClickListener(this);
        setListner();
    }

    private void setListner() {
        toBack.setOnClickListener(this);

    }

    /**
     * 输入的为讲解资源编号
     *
     * @param input
     */
    private void loadResByDe(String input) {
        Cursor cursor = HResourceDB.getInstance().loadResByDE(input);
        if (cursor != null) {
            exhibitList.clear();
            while (cursor.moveToNext()) {
                Exhibition exhibition = Exhibition.Cursor2Model(cursor);
                exhibitList.add(exhibition);
            }
            if (exhibitList.isEmpty()) {

                recyclerView.setVisibility(View.GONE);

            } else {
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
            }
            cursor.close();
        }
    }

    /**
     * 输入的为关键字
     *
     * @param input
     */
    private void loadResByKeyWord(String input) {
        Cursor cursor = HResourceDB.getInstance().loadResListByKeyWord(input);
        if (cursor != null) {
            exhibitList.clear();
            while (cursor.moveToNext()) {
                Exhibition exhibition = Exhibition.Cursor2Model(cursor);
                exhibitList.add(exhibition);
            }
            if (exhibitList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
            } else {
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
            }
            cursor.close();
        }
    }

    /**
     * 初始化展品数据，默认显示所有展品
     */
    private void initData() {
        Cursor cursor = HResourceDB.getInstance().loadAllRes();
        if (cursor != null) {
            exhibitList.clear();
            while (cursor.moveToNext()) {
                Exhibition exhibit = Exhibition.Cursor2Model(cursor);
                exhibitList.add(exhibit);
            }
            cursor.close();

            if (exhibitList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
            } else {
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.searchcontent);
        title = (TextView) findViewById(R.id.title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_search);
        toBack = (ImageView) findViewById(R.id.toback);
        searchImg = (ImageView) findViewById(R.id.searchImg);
        title.setText("数字点播");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (!TextUtils.isEmpty(s)) {
                    if (input.trim().length() < 6 && input.matches("[0-9]+")) {
                        loadResByDe(input);
                    } else {
                        loadResByKeyWord(input);
                    }
                } else {
                    initData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        recyclerView.setLayoutManager(new LinearLayoutManager(DigitalView.this));
        recyclerView.setAdapter(adapter = new CommonAdapter<Exhibition>(DigitalView.this, R.layout.item_listview, exhibitList) {
            @Override
            public void convert(ViewHolder holder, Exhibition exhibition) {
                holder.setText(R.id.name_listviewItem, exhibition.getName());
                ImageView imageView = holder.getView(R.id.image_listview);
                path = Constant.getDefaultFileDir()  + "CHINESE" + "/" + exhibition.getFileNo() + "/" + exhibition.getFileNo() + ".png";
                Glide.with(DigitalView.this).load(path).placeholder(R.mipmap
                        .down).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener<Exhibition>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Exhibition exhibition, int position) {

                    editText.clearFocus();
                    StartPlay(exhibition);
                    editText.clearFocus();


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
        Intent intent=new Intent(DigitalView.this, Play.class);
        intent.putExtra("exhibition",exhibition);
        retrofitCount(exhibition.getFileNo(),2);
        startActivity(intent);
    }
    private void retrofitCount(String fileNo,int type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.200.234.14:8091/bjgdjzbwg/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();
        ScanInterface scanInterface = retrofit.create(ScanInterface.class);
        //第五步：通过实体调用请求方法，获取Call对象
        Call<ResponseBody> call = scanInterface.getResult(fileNo, type,getSharedPreferences("sp",MODE_PRIVATE).getString("AND","and"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
