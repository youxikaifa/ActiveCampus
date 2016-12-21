package com.thy.activecampus.ui.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thy.activecampus.R;
import com.thy.activecampus.adapter.PicsAdapter;
import com.thy.activecampus.model.Image;
import com.thy.activecampus.ui.activity.ImagePickerActivity;
import com.thy.activecampus.ui.activity.PubAutoDyneA;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jin on 7/29.
 */

@EFragment(R.layout.fragment_select_pics)
public class SelectImgF extends Fragment {

    @ViewById(R.id.lvPics)
    RecyclerView lvPics;
    @ViewById(R.id.ivAdd)
    ImageView ivAdd;
    @ViewById(R.id.tvNotice)
    TextView tvNotice;


    PicsAdapter adapter;
    PubAutoDyneA activity;

    public LinearLayoutManager llManager;

    public ArrayList<Image> images = new ArrayList<>();
    private ArrayList<String> imgPaths = new ArrayList<>();
    private int REQUEST_CODE_PICKER = 2000;



    @AfterViews
    public void initViews(){

        activity = (PubAutoDyneA) getActivity();

        llManager= new LinearLayoutManager(getActivity());
        llManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        lvPics.setLayoutManager(llManager);
        adapter = new PicsAdapter(getActivity(),images);
        adapter.setACallback(new PicsAdapter.AddCallBack() {
            @Override
            public void onAddClick(int position) {
                selectPhoto();
            }

            @Override
            public void onCancleClick(int position) {
                images.remove(position);
                imgPaths.remove(position);
                adapter.notifyDataSetChanged();
                activity.onReloadImgs(images,imgPaths);
                tvNotice.setText("已选择"+images.size()+"张图片");
            }
        });
        lvPics.setAdapter(adapter);

    }

    @Click(R.id.ivAdd)
    void add(){
        selectPhoto();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Image> paths;
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            paths = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            imgPaths.clear();
            images.clear();
            for (int i = 0; i < paths.size(); i++) {
                images.add(paths.get(i));
                imgPaths.add(paths.get(i).getPath());
            }

            if (imgPaths.size()==0){
                ivAdd.setVisibility(View.VISIBLE);
                lvPics.setVisibility(View.GONE);
            }else{
                ivAdd.setVisibility(View.GONE);
                lvPics.setVisibility(View.VISIBLE);
            }
            tvNotice.setText("已选"+images.size()+"张图片");

            adapter.notifyDataSetChanged();

            activity.onReloadImgs(images,imgPaths);
        }
    }

    public void selectPhoto(){
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);

        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_MODE, true);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_MODE, ImagePickerActivity.MODE_MULTIPLE);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_LIMIT, 10);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SHOW_CAMERA, true);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES, images);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_TITLE, "Album");
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_TITLE, "Tap to select images");
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_DIRECTORY, "Camera");

        startActivityForResult(intent, REQUEST_CODE_PICKER);
    }
}
