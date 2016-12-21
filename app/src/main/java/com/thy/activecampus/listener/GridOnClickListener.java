package com.thy.activecampus.listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.thy.activecampus.R;
import com.thy.activecampus.widget.PagerDialog;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class GridOnClickListener implements AdapterView.OnItemClickListener {

    private Context context;
    private List<String> uris;
    public PagerDialog dialog;

    public GridOnClickListener(Context context, List<String> uris) {
        this.context = context;
        this.uris = uris;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        dialog = new PagerDialog(context, uris, i);
        dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
        dialog.show();
    }
}
