package com.thy.activecampus.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Jin on 7/29.
 */

public class ChatMsgListView extends ListView {

    public DataChangeListener listener;

    public ChatMsgListView(Context context) {
        super(context);
    }

    public ChatMsgListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatMsgListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void handleDataChanged() {
        super.handleDataChanged();
        if (listener != null){
            listener.onSucceed();
        }

    }

    public void setDataChangedListener(DataChangeListener dataChangedListener){
        this.listener = dataChangedListener;
    }

    public interface DataChangeListener{
        void onSucceed();
    }
}
