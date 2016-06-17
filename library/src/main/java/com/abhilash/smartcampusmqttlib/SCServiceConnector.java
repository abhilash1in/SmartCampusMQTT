package com.abhilash.smartcampusmqttlib;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

import com.abhilash.smartcampusmqttlib.HelperClass.CommonUtils;
import com.abhilash.smartcampusmqttlib.Interfaces.ServiceCallback;
import com.abhilash.smartcampusmqttlib.Interfaces.ServiceStatusCallback;

/**
 * Created by shashankshekhar on 01/06/16.
 */
public class SCServiceConnector implements ServiceConnection {
    private static Messenger messenger;
    private static boolean bound;
    ServiceStatusCallback callback;
    public SCServiceConnector (ServiceStatusCallback callback1) {
        callback = callback1;
    }
    public static Messenger getMessenger () {
        return messenger;
    }
    public static boolean isBound() {
        return bound;
    }
    public static boolean isServiceConnected () {
        return (messenger!=null && bound);
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        messenger = new Messenger(service);
        bound = true;
        CommonUtils.printLog("service connected");
        callback.serviceConnected();
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
        messenger = null;
        CommonUtils.printLog("service DISconnected");
        callback.serviceDisconnected();

    }

}
