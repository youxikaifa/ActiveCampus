package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thy.activecampus.R;
import com.thy.activecampus.adapter.SelectImgAdapter;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.Image;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.widget.DynamicTagFlowLayout;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_add_label)
public class AddLabelA extends BaseA implements TextWatcher {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    @ViewById(R.id.progress)
    ProgressBar progressBar;
    @ViewById(R.id.content)
    EditText et_content;
    @ViewById(R.id.publish)
    TextView tv_publish;
    @ViewById(R.id.gvUploadPic)
    GridView gvUpload;
    @ViewById(R.id.fl_tags)
    DynamicTagFlowLayout tagFlowLayout;
    @ViewById(R.id.et_title)
    EditText etTitle;
    List<Integer> checkTags = new ArrayList<>();


    List<String> selectTags = new ArrayList<>();
    List<String> tags = new ArrayList<>();
    private String[] proj;
    public LabelReqImpl request = LabelReqImpl.getInstance();
    private ArrayList<Image> images;
    private ArrayList<String> imgPaths = new ArrayList<>();

    private int REQUEST_CODE_PICKER = 2000;
    public SelectImgAdapter imgAdapter;
    public ACache cache = null;

    @AfterViews
    public void initViews() {
        init();
    }

    private void init() {
        proj = new String[]{MediaStore.MediaColumns.DATA};
        images = new ArrayList<>();
        images.add(new Image(0, "add", "", false)); //初始化add按钮
        imgAdapter = new SelectImgAdapter(this, images);
        gvUpload.setAdapter(imgAdapter);
        gvUpload.setOnItemClickListener(onItemClickListener);
        gvUpload.setOnItemLongClickListener(onItemLongClickListener);

        et_content.addTextChangedListener(this);

        initData();
        tagFlowLayout.setTags(tags);

        tagFlowLayout.setOnTagItemClickListener(new DynamicTagFlowLayout.OnTagItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                TextView tv = (TextView) v;
                if (checkTags.contains(position)) {
                    tv.setBackgroundResource(R.drawable.xml_tag_bg);
                    checkTags.remove((Integer) position);
                } else {
                    tv.setBackgroundResource(R.drawable.xml_tag_check_bg);
                    checkTags.add((Integer) position);
                }
            }
        });


    }


    public List<String> getSelectTags(List<Integer> t) {
        List<String> tag = new ArrayList<>();
        for (int i = 0; i < t.size(); i++) {
            switch (t.get(i)) {
                case 0:
                    tag.add("游戏");
                    break;
                case 1:
                    tag.add("运动");
                    break;
                case 2:
                    tag.add("讲座");
                    break;
                case 3:
                    tag.add("旅游");
                    break;
                case 4:
                    tag.add("聚会");
                    break;
                case 5:
                    tag.add("KTV");
                    break;
                case 6:
                    tag.add("比赛");
                    break;
                case 7:
                    tag.add("个人秀");
                    break;
                case 8:
                    tag.add("其他");
                    break;
                default:
                    break;
            }
        }
        return tag;
    }

    /**
     * 点击添加
     */
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                start();
            }
        }
    };

    /**
     * 长按删除
     */
    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Object imgPath = parent.getItemAtPosition(position);
            if (imgPath != null && position != 0) {
                images.remove(imgPath);
                imgAdapter.notifyDataSetChanged();
            }
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Image> paths;
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            paths = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            imgPaths.clear();
            images.clear();
            images.add(new Image(0, "add", "", false));
            for (int i = 0; i < paths.size(); i++) {
                images.add(paths.get(i));
                imgPaths.add(paths.get(i).getPath());
            }


            imgAdapter.notifyDataSetChanged();
        }
    }

    @Click(R.id.back)
    void back() {
        this.finish();
    }

    public void isClickSend() {
        int content = et_content.getText().toString().length();
        if (content > 0) {
            tv_publish.setEnabled(true);
        }
    }

    @Click(R.id.publish)
    void publish() {
        progressBar.setVisibility(View.VISIBLE);
        cache = ACache.get(this, MyConstants.USER_INFO);
        User user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
        LabelM model = new LabelM();
        model.setUserId(user.get_id());
        model.setName(user.getName());
        model.setSex(user.getSex());
        model.setContent(et_content.getText().toString());
        model.setPicUrls(imgPaths);
        model.setHead(user.getThumb());
        model.setTags(getSelectTags(checkTags));
        model.setTitle(etTitle.getText().toString());
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


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        isClickSend();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Click(R.id.select_photo)
    void photo() {

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

    private void initData() {

        tags.add("游戏");
        tags.add("运动");
        tags.add("讲座");
        tags.add("旅游");
        tags.add("聚会");
        tags.add("KTV");
        tags.add("比赛");
        tags.add("个人秀");
        tags.add("其他");

    }


}

