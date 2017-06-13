package com.example.lenovo.hd_beijing_meseum.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;

public class IntroWeb extends BaseActivity implements View.OnClickListener{
    private ImageView toBackImg;
    private ImageView imageView;
    private WebView webView;
    private TextView titleText;
 private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_web);
        initView();
        url= Constant.getDefaultFileDir()+"CHINESE"+"/"+"1001"+"/"+"1001.html";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.requestFocus();
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("file:///"+url);
        toBackImg.setOnClickListener(this);
    }

    private void initView() {
        toBackImg = (ImageView) findViewById(R.id.toback);
        titleText= (TextView) findViewById(R.id.title);
        webView= (WebView) findViewById(R.id.intro_webview);
        titleText.setText("场馆简介");
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
