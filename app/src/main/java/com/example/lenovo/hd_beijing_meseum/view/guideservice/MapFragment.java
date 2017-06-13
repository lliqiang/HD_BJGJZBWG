package com.example.lenovo.hd_beijing_meseum.view.guideservice;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.model.Exhibition;
import com.example.lenovo.hd_beijing_meseum.model.Location;
import com.example.lenovo.hd_beijing_meseum.model.ResponseBody;
import com.example.lenovo.hd_beijing_meseum.serveinterface.ScanInterface;
import com.example.lenovo.hd_beijing_meseum.tileview.BitmapProviderFile;
import com.example.lenovo.hd_beijing_meseum.tileview.HCallOut;
import com.example.lenovo.hd_beijing_meseum.tileview.TileViewUtil;
import com.example.lenovo.hd_beijing_meseum.utils.BitmapUtils;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceDB;
import com.example.lenovo.hd_beijing_meseum.view.collect.CollectDetail;
import com.example.lenovo.hd_beijing_meseum.view.play.Play;
import com.qozix.tileview.TileView;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class MapFragment extends SupportFragment {

    TileView tileView;
    ImageView movingImg;
    private Intent intent;
    private List<View> markList;
    int floor = 1;
    double startX = -1;
    double startY = -1;
    double tempX;
    double tempY;

//    final int TILE_VIEW_WIDTH = 7580;
//    final int TILE_VIEW_HEIGHT = 2560;
    final int MAX_SCALE = 3;
    final int RECEIVED_LOCATION = 1;
    Location mLocation;
    List<Location> locationList = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVED_LOCATION:
                    if (mLocation != null && mLocation.getFloor() == floor) {
                        double locX = mLocation.getX();
                        double locY = mLocation.getY();
                        refreshMap(locX, locY);
                    }
                    break;
            }
        }
    };


    public static MapFragment getInstance(int floor) {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("floor", floor);
        fragment.setArguments(bundle);
        return fragment;
    }


    public static MapFragment getInstance(int floor, double x, double y) {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("floor", floor);
        bundle.putDouble("x", x);
        bundle.putDouble("y", y);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        floor = getArguments().getInt("floor");
        tempX = getArguments().getFloat("x");
        tempY = getArguments().getFloat("y");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        tileView = new TileView(getActivity());
        tileView.setTransitionsEnabled(false);
        if (floor==1){
            tileView.setSize(7580, 2560);
            tileView.defineBounds(0, 0, 7580, 2560);
        }else {
            tileView.setSize(4945,2560);
            tileView.defineBounds(0, 0, 4945, 2560);
        }


        tileView.setMarkerAnchorPoints(-0.5f, -0.5f);
        tileView.setScale(0);
        tileView.setScaleLimits(0, MAX_SCALE);
        markList = new ArrayList<>();

        tileView.setBitmapProvider(new BitmapProviderFile());
        String baseMapPath = Constant.getDefaultFileDir() + "CHINESE" + "/" + "map" + "/" + floor;
        tileView.addDetailLevel(1.000f, baseMapPath + "/1000/%d_%d.png");
        tileView.addDetailLevel(0.500f, baseMapPath + "/500/%d_%d.png");
        tileView.addDetailLevel(0.250f, baseMapPath + "/250/%d_%d.png");
        tileView.addDetailLevel(0.125f, baseMapPath + "/125/%d_%d.png");

        /**
         * 添加底图
         */
       ImageView downSample = new ImageView(getContext());
      downSample.setImageBitmap(BitmapUtils.loadBitmapFromFile(baseMapPath + "/img.png"));
      tileView.addView(downSample, 0);


        addMarks(tileView);

        if (tempX != 0 && tempY != 0) {
            TileViewUtil.firstFix(tileView, movingImg, tempX, tempY);
        }

        return tileView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();

    }


    /**
     * 根据坐标刷新地图
     *
     * @param locX
     * @param locY
     */
    private void refreshMap(double locX, double locY) {
        if (startX == -1) {
            TileViewUtil.firstFix(tileView, movingImg, locX, locY);
        } else {
            TileViewUtil.move(tileView, movingImg, startX, startY, locX, locY);
        }
        startX = locX;
        startY = locY;
    }


    /**
     * 添加Marker
     *
     * @param tileView
     */
    private void addMarks(final TileView tileView) {

        Bitmap bitmapLocationNormal = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.ex_play, 100, 100);
        final Bitmap bitmapLocationActive = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.guan_play, 100, 100);

        getLocationByFloor(floor);

      for (Location location : locationList) {
            final ImageView marker = new ImageView(getActivity());
            marker.setTag(location);
                if (location.getFileType()==0){
                    marker.setImageBitmap(bitmapLocationNormal);
                }else if (location.getFileType()==1){
                    marker.setImageBitmap(bitmapLocationActive);
                }


                marker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickMarker(view);
                    }
                });
                tileView.addMarker(marker, location.getX(), location.getY(), null, null);

        }
        movingImg = new ImageView(getActivity());
        movingImg.setImageBitmap(bitmapLocationActive);
    }


    /**
     * Marker点击监听
     *
     * @param view
     */
    private void clickMarker(View view) {
        final Location location = (Location) view.getTag();
        tileView.slideToAndCenter(location.getX(), location.getY());
        final HCallOut hCallOut = new HCallOut(view.getContext());

        tileView.addCallout(hCallOut, location.getX(), location.getY(), -0.5f, -1.0f);
        hCallOut.transitionIn();
        hCallOut.setTitle(location.getExhibitName());
        if (location.getFileType()==0){
            hCallOut.setIcon(R.mipmap.voice);
        }

        hCallOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = HResourceDB.getInstance().loadResByAutoNo(location.getAutoNo());
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        Exhibition exhibit = Exhibition.Cursor2Model(cursor);
                        if (exhibit != null) {
                            if (location.getFileType()==0){
                                 intent=new Intent(getActivity().getApplicationContext(), Play.class);
                            }else if (location.getFileType()==1){
                                 intent=new Intent(getActivity().getApplicationContext(), CollectDetail.class);
                            }

                            Bundle bundle=new Bundle();
                            bundle.putParcelable("exhibition",exhibit);
                            bundle.putBoolean("flag",true);
                            intent.putExtra("bundle",bundle);
                            retrofitCount(exhibit.getFileNo(),1);
                            getActivity().startActivity(intent);

                            //执行跳转到play的操作
                            break;
                        }
                    }
                    cursor.close();
                }
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
        Call<ResponseBody> call = scanInterface.getResult(fileNo, type,getActivity().getSharedPreferences("sp",MODE_PRIVATE).getString("AND","mull"));
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
     * 获取指定楼层的点位信息
     */
    private void getLocationByFloor( int floor) {
        Cursor cursor = HResourceDB.getInstance().loadLocationListByFloor( floor);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Location location = Location.cursorToLocationInfo(cursor);
                locationList.add(location);
            }
        }
        cursor.close();
    }


}
