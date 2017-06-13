package com.example.lenovo.hd_beijing_meseum.view.webac;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;

public class TicketInfoWeb extends BaseActivity implements View.OnClickListener{
    private ImageView toBack;
    private WebView webView;
    private TextView title;
private String url;
    private String path;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_info_web);

        initView();
        url= Constant.getDefaultFileDir()+"CHINESE"+"/"+"1302"+"/"+"1302.html";
//        path=Constant.getDefaultFileDir()+"CHINESE"+"/"+"1302"+"/"+"1302.png";
//        Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.requestFocus();
        webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("file:///"+url);
        initListner();
    }
    private void initListner() {
        toBack.setOnClickListener(this);
    }

    private void initView() {
        toBack= (ImageView) findViewById(R.id.toback);
        imageView= (ImageView) findViewById(R.id.ticket_info);
        webView= (WebView) findViewById(R.id.webview_ticketInfo);
        title= (TextView) findViewById(R.id.title);
        title.setText("票务信息");
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
