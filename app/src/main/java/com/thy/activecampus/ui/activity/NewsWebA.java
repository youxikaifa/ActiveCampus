package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.thy.activecampus.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_webview_news)
public class NewsWebA extends AppCompatActivity {

    @ViewById(R.id.web_view)
    WebView webView;
    String url;

    @AfterViews
    public void initViews(){
        getArgments();
        webView.loadUrl(url);
    }

    private void getArgments() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
    }


}
