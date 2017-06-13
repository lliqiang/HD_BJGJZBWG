package com.example.lenovo.hd_beijing_meseum.view.listguide;

import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.model.Exhibition;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceDB;

import java.util.ArrayList;
import java.util.List;

public class ListDetailGuide extends BaseActivity implements View.OnClickListener {

    private ImageView toBack;
    private ImageView BAIIIMG;
    private ImageView TSWESTIMG;
    private ImageView TAISUIEAST_palace;
    private ImageView BImg;

    private TextView title;
    private List<Exhibition> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_guide_view);
        initView();
        initListner();

    }

    private void getData(int UnitNo) {
        Cursor cursor = HResourceDB.getInstance().loadResListByUnitNo(UnitNo);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Exhibition exhibition = Exhibition.Cursor2Model(cursor);
                list.add(exhibition);

            }
            cursor.close();
        }
    }


    private void initView() {
        toBack = (ImageView) findViewById(R.id.toback);
        BAIIIMG = (ImageView) findViewById(R.id.Bai_palace);
        TSWESTIMG = (ImageView) findViewById(R.id.WEST_TAISUI_palace);
        TAISUIEAST_palace = (ImageView) findViewById(R.id.TAISUIEAST_palace);
        BImg = (ImageView) findViewById(R.id.bai_palace);
        title = (TextView) findViewById(R.id.title);
        title.setText("列表导览");
        list = new ArrayList<>();
    }

    private void initListner() {
        toBack.setOnClickListener(this);
        BAIIIMG.setOnClickListener(this);
        TSWESTIMG.setOnClickListener(this);
        TAISUIEAST_palace.setOnClickListener(this);
        BImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        list.clear();
        switch (v.getId()) {
            case R.id.toback:
                finish();
                break;
            case R.id.Bai_palace:

                getData(1);
                startList(1);
                break;
            case R.id.WEST_TAISUI_palace:

                getData(2);
                startList(2);
                break;
            case R.id.TAISUIEAST_palace:

                getData(3);
                startList(3);
                break;
            case R.id.bai_palace:
                Toast.makeText(ListDetailGuide.this, "正在开发中", Toast.LENGTH_SHORT).show();

                break;
        }

    }

    private void startList(int num) {

        Intent intent = new Intent(ListDetailGuide.this, ListGuideView.class);
        intent.putExtra("num",num);
        intent.putParcelableArrayListExtra("array", (ArrayList<? extends Parcelable>) list);
        startActivity(intent);
    }
}
