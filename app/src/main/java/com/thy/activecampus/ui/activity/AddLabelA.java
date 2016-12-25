package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thy.activecampus.R;
import com.thy.activecampus.adapter.PicsAdapter;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.Image;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_add_label)
public class AddLabelA extends BaseA {

    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;
    @ViewById(R.id.et_content)
    EditText etContent;
    @ViewById(R.id.lvPics)
    RecyclerView lvPics;
    @ViewById(R.id.tvNotice)
    TextView tvNotice;
    @ViewById(R.id.ivAdd)
    ImageView ivAdd;

    public ArrayList<Image> images = new ArrayList<>();
    private ArrayList<String> imgPaths = new ArrayList<>();

    private int REQUEST_CODE_PICKER = 2000;


    PicsAdapter adapter;

    List<String> tags = new ArrayList<>();
    public LabelReqImpl request = LabelReqImpl.getInstance();

    public ACache cache = null;

    @AfterViews
    public void initViews() {
        getArgs();
        init();
    }

    private void getArgs() {
        Intent intent = getIntent();
        String tag = intent.getStringExtra("title");
        tags.add(tag);
    }

    private void init() {
        lvPics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new PicsAdapter(this, images);
        adapter.setACallback(new PicsAdapter.AddCallBack() {
            @Override
            public void onAddClick(int position) {
                start();
            }

            @Override
            public void onCancleClick(int position) {
                images.remove(position);
                imgPaths.remove(position);
                adapter.notifyDataSetChanged();
                tvNotice.setText("已选择" + images.size() + "张图片");
            }
        });
        lvPics.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Image> paths;
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            paths = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            for (int i = 0; i < paths.size(); i++) {
                images.add(paths.get(i));
                imgPaths.add(paths.get(i).getPath());
            }
            if (images.size()>0){
                ivAdd.setVisibility(View.GONE);
                lvPics.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }else{
                ivAdd.setVisibility(View.VISIBLE);
                lvPics.setVisibility(View.GONE);
            }

        }
    }

    @Click(R.id.back)
    void back() {
        this.finish();
    }

    @Click(R.id.ivAdd)
    void add(){
        start();
    }

    public boolean isCommit() {
        if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Click(R.id.tv_pub)
    void publish() {
        if (isCommit()) {
            progressBar.setVisibility(View.VISIBLE);
            cache = ACache.get(this, MyConstants.USER_INFO);
            User user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
            LabelM model = new LabelM();
            model.setUserId(user.get_id());
            model.setName(user.getName());
            model.setMotto(user.getMotto());
            model.setSchool(user.getSchool());
            model.setSex(user.getSex());
            model.setContent(etContent.getText().toString());
            model.setPicUrls(imgPaths);
            model.setHead(user.getThumb());
            model.setTags(tags);

            request.post(MyConstants.BASE_URL, model, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    imgPaths.clear();
                    Log.v("SEND", "发表失败:" + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    imgPaths.clear();
                    Log.v("SEND", "发表成功:" + response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AddLabelA.this.finish();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }




    @Click(R.id.iv_select_pics)
    void photo() {
        start();
    }

    // Recomended builder
    public void start() {
        ImagePicker.create(this)
                .folderMode(true) // set folder mode (false by default)
                .folderTitle("文件夹") // folder selection title
                .imageTitle("选择图片") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(11) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .origin(images) // original selected images, used in multi mode
                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }


}

