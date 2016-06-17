package com.abhilash.smartcampusmqttlib;

import android.content.ComponentName;
import android.renderscript.ScriptIntrinsicYuvToRGB;

/**
 * Created by shashankshekhar on 27/10/15.
 */

public interface  SmartXLibConstants {
     String MY_TAG = "S-WATER";

    String SOLAR_DATA_TOPIC_NAME = "solarDataMQTT";
    String EVENT_NAME = "WATER_LEAKAGE";
    String SERVICE_PACKAGE_NAME = "com.example.shashankshekhar.servicedemo";
    String SERVICE_CLASS_NAME = "com.example.shashankshekhar.servicedemo.FirstService";

    /*
    these are the messages that go from library to the background mqtt service. These messeges are telling the
    service to perform some action.
     */
    int PUBLISH_MESSAGE = 3;
    int SUBSCRIBE_TO_TOPIC = 4;
    int UNSUBSCRIBE_TO_TOPIC = 5;
    int CHECK_MQTT_CONNECTION = 7;
    int CONNECT_MQTT = 8;
    int DISCONNECT_MQTT = 9;

    /*
        these are the messeges that are received from the service as a result of the above actions. BG Service sends
        these messeges to the app. The app should handle these accordingly.
     */

    int MQTT_CONNECTED =1;
    int UNABLE_TO_CONNECT =2;
    int NO_NETWORK_AVAILABLE =4;
    int MQTT_CONNECTION_IN_PROGRESS = 5;
    int MQTT_NOT_CONNECTED = 6;
    int DISCONNECT_SUCCESS= 11;

    // publish status
    int TOPIC_PUBLISHED = 7;
    int ERROR_IN_PUBLISHING = 8;

    // subscription status
    int SUBSCRIPTION_SUCCESS = 9;
    int SUBSCRIPTION_ERROR = 10;

    // unsubscription status
    int UNSUBSCRIPTION_SUCCESS = 12;
    int UNSUBSCRIPTION_ERROR = 13;

}
