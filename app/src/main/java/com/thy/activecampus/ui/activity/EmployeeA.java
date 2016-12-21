package com.thy.activecampus.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.thy.activecampus.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_employee)
public class EmployeeA extends AppCompatActivity {
    @ViewById(R.id.web_view)
    WebView webView;

    @AfterViews
    public void initView(){
        webView.loadUrl("http://mob.job1001.com/SearchResult.php?key=%C7%F8%D3%F2%CF%FA%CA%DB%BE%AD%C0%ED");
    }
}
