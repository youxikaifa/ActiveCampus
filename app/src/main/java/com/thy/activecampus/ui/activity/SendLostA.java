package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thy.activecampus.R;
import com.thy.activecampus.adapter.SelectImgAdapter;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.base.SelectPicureBaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.Image;
import com.thy.activecampus.model.Losing;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.view.ImageGridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */

@EActivity(R.layout.activity_send_lost)
public class SendLostA extends BaseA implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final LabelReqImpl request = LabelReqImpl.getInstance();
    private int REQUEST_CODE_PICKER = 2000;

    @ViewById(R.id.et_title)
    EditText etTitle;
    @ViewById(R.id.view_lost)
    ImageView viewLost;
    @ViewById(R.id.view_get)
    ImageView viewGet;
    @ViewById(R.id.et_phone)
    EditText etPhone;
    @ViewById(R.id.et_addr)
    EditText etAddr;
    @ViewById(R.id.et_remark)
    EditText etRemark;
    @ViewById(R.id.gvAddPic)
    ImageGridView gvAddPic;

    private ArrayList<String> imgPaths = new ArrayList<>();
    private ArrayList<Image> images = new ArrayList<>();

    public SelectImgAdapter imgAdapter;
    public ACache cache = null;
    int type = 0;
    User user;

    @AfterViews
    public void initViews() {
        viewLost.setImageResource(R.drawable.xml_cirle_solid);
        images.add(new Image(0, "add", "", false)); //初始化add按钮
        imgAdapter = new SelectImgAdapter(this, images);
        gvAddPic.setAdapter(imgAdapter);
        gvAddPic.setOnItemClickListener(this);
        gvAddPic.setOnItemLongClickListener(this);

        initLocalData();
    }

    @Background
    public void initLocalData() {
        cache = ACache.get(this, MyConstants.USER_INFO);
        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
    }


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


    @Click(R.id.rl_back)
    void back() {
        this.finish();
    }

    @Click(R.id.tv_commit)
    void commit() {
        if (isCommit()) {
            Losing losing = new Losing();
            losing.setName(user.getName());
            losing.setHead(user.getThumb());
            losing.setTitle(etTitle.getText().toString());
            losing.setType(type);
            losing.setContact(etPhone.getText().toString());
            losing.setAddr(etAddr.getText().toString());
            losing.setThumbs(imgPaths);
            losing.setRemark(etRemark.getText().toString());

            request.findlost(MyConstants.BASE_URL, losing, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    imgPaths.clear();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    imgPaths.clear();
                    Log.v("SEND", "发表成功:" + response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SendLostA.this.finish();
//                        progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }

    }

    @Click(R.id.rl_get)
    void rlget() {
        viewGet.setImageResource(R.drawable.xml_cirle_solid);
        viewLost.setImageDrawable(null);
        type = 1;
    }

    @Click(R.id.rl_lost)
    void rlLost() {
        viewLost.setImageResource(R.drawable.xml_cirle_solid);
        viewGet.setImageDrawable(null);
        type = 0;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            start();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Object imgPath = parent.getItemAtPosition(position);
        if (imgPath != null && position != 0) {
            images.remove(imgPath);
            imgAdapter.notifyDataSetChanged();
        }
        return true;
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

    public boolean isCommit() {
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            Toast.makeText(this, "请输入联系电话", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etAddr.getText().toString().trim())) {
            Toast.makeText(this, "请输入地址", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
