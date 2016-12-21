package com.thy.activecampus;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.thy.activecampus.common.MyConstants;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Jin on 7/29.
 */

public class App extends Application {
    //SDK的初始化

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(MyConstants.LOCALHOST);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
