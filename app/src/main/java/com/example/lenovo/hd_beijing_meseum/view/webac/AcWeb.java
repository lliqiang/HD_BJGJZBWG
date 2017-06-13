package com.example.lenovo.hd_beijing_meseum.view.webac;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;

public class AcWeb extends BaseActivity implements View.OnClickListener{
    private ImageView toBack;
    private WebView webView;
    private TextView title;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_web);
        initView();
        url= Constant.getDefaultFileDir()+"CHINESE"+"/"+"1304"+"/"+"1304.html";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.requestFocus();
        webView.loadUrl("file:///"+url);
        initListner();

    }
    private void initView() {
        toBack= (ImageView) findViewById(R.id.toback);
        webView= (WebView) findViewById(R.id.webview_AcWebview);
        title= (TextView) findViewById(R.id.title);
        title.setText("场馆活动");
    }
    private void initListner() {
        toBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        finish();
    }
}
