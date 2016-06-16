package com.abhilash.smartcampusmqttlib;

import android.os.Handler;
import android.os.Message;


/**
 * Created by shashankshekhar on 02/02/16.
 */
public class IncomingMessageHandler extends Handler {
    static final int MQTT_DISCONNECETD = 1;
    static final int MQTT_CONNECTED = 2;
    @Override
    public void handleMessage (Message message) {

    }
}
