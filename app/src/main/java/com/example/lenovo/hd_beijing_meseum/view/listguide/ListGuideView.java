package com.example.lenovo.hd_beijing_meseum.view.listguide;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.MainActivity;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.model.Exhibition;
import com.example.lenovo.hd_beijing_meseum.model.ResponseBody;
import com.example.lenovo.hd_beijing_meseum.serveinterface.HeartInterface;
import com.example.lenovo.hd_beijing_meseum.serveinterface.ScanInterface;
import com.example.lenovo.hd_beijing_meseum.utils.CommonUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceDB;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.HdApplication;
import com.example.lenovo.hd_beijing_meseum.utils.ILoadListener;
import com.example.lenovo.hd_beijing_meseum.utils.ToastUtils;
import com.example.lenovo.hd_beijing_meseum.view.play.Play;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListGuideView extends BaseActivity implements View.OnClickListener {
    private ImageView toBack;
    private TextView title;
    private ListView listView;
    private List<Exhibition> list;
    private ListAdapter adapter;
    private String fileNo;
    private Intent intent;
    private int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_guide);
        initView();

        list = getIntent().getParcelableArrayListExtra("array");
        num=getIntent().getIntExtra("num",8);
        if (num==1){
            title.setText("拜殿");
        }else if (num==2){
            title.setText("太岁西殿");
        }else if (num==3){
            title.setText("太岁东殿");
        }

        adapter = new ListAdapter(ListGuideView.this, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initListner();
    }


    private void initListner() {
        toBack.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Exhibition exhibition = list.get(position);
                fileNo = list.get(position).getFileNo();
                intent = new Intent(ListGuideView.this, Play.class);
                intent.putExtra("exhibition", exhibition);
                retrofitCount(exhibition.getFileNo(),1);
                startActivity(intent);


            }
        });
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
                ResponseBody responseBody = response.body();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }





    /**
     * 判断当前语种某展品资源是否存在
     *
     * @return
     */
    public static boolean isExhibitExist(String fileNo) {
        File file = new File(Constant.getDefaultFileDir() + "CHINESE" + "/" +
                fileNo);
        return file.exists();
    }

    private void initView() {
        toBack = (ImageView) findViewById(R.id.toback);
        title = (TextView) findViewById(R.id.title);
        listView = (ListView) findViewById(R.id.listview);

    }

    @Override
    public void onClick(View v) {
        finish();
    }


}
