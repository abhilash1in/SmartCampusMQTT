package com.abhilash.smartcampusmqttlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import static com.abhilash.smartcampusmqttlib.SmartXLibConstants.*;
import android.os.RemoteException;
import com.abhilash.smartcampusmqttlib.HelperClass.CommonUtils;

public class ServiceAdapter {
    static Messenger messenger = null;
    static ServiceAdapter serviceAdapterinstance ;
    Messenger receiverMessenger;
    static boolean bound = false;
    Context callerContext = null;
    // TODO: 14/02/16  initialise it separately and send a call to BGS to update its storage
    String applicationId = null; 
    private ServiceAdapter (Context context) {
        callerContext = context;
    }
    public static ServiceAdapter getServiceAdapterinstance (Context context) {
        if (serviceAdapterinstance != null) {
            return serviceAdapterinstance;
        }
        serviceAdapterinstance = new ServiceAdapter(context);
        return serviceAdapterinstance;
    }

    public void bindToService () {
        if (serviceConnected() == true) {
            CommonUtils.showToast(callerContext, "Service already connected");
            return;
        }
        ComponentName componentName = new ComponentName("com.example.shashankshekhar.servicedemo",
                "com.example.shashankshekhar.servicedemo.FirstService");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        Boolean isConnected = callerContext.bindService(intent, serviceConnection, Context.BIND_IMPORTANT);
        receiverMessenger = new Messenger(new IncomingHandler(callerContext));

    }

    public void unbindFromService() {
        if (serviceConnected() == false) {
            CommonUtils.showToast(callerContext,"Already Disconnected");
        }
        callerContext.unbindService(serviceConnection);
        bound = false;
        messenger = null;
    }

    public Boolean serviceConnected() {
        if (messenger==null || bound == false) {
            return false;
        }
        return true;
    }

    public void publishGlobal (String topicName,String eventName,String dataString) {
        if (checkConnectivity() == false) {
            return;
        }
        Message messageToPublish = Message.obtain(null,PUBLISH_MESSAGE);
        Bundle bundleToPublish = new Bundle();
        bundleToPublish.putString("topicName",topicName);
        bundleToPublish.putString("eventName", eventName);
        bundleToPublish.putString("dataString", dataString);
        messageToPublish.setData(bundleToPublish);
        messageToPublish.replyTo = receiverMessenger;
        try {
            messenger.send(messageToPublish);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("remote Exception,Could not send message");
        }

    }

    public String subscribeToTopic (String topicName) {
        // TODO: 12/11/15 this should return a subscribe id to the caller hence the String return type
        if (checkConnectivity() == false) {
            return null;
        }
        CommonUtils.printLog("call to subscribe made from application");
        Message messageToSubscribe = Message.obtain(null,SUBSCRIBE_TO_TOPIC);
        Bundle bundle = new Bundle();
        bundle.putString("topicName", topicName);
        messageToSubscribe.setData(bundle);
        messageToSubscribe.replyTo = receiverMessenger;
        try {
            messenger.send(messageToSubscribe);
        }  catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("exception while trying to subscribe");
        }
        return null;
    }
    public void unsubscribeFromTopic (String topicName) {
        if (checkConnectivity() == false) {
            return;
        }
        Message unsubscribe = Message.obtain(null,UNSUBSCRIBE_TO_TOPIC);
        Bundle bundle = new Bundle();
        bundle.putString("topicName", topicName);
        unsubscribe.setData(bundle);
        unsubscribe.replyTo= receiverMessenger;
        try {
            messenger.send(unsubscribe);
        }  catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("exception while sending message for unsubscribe");
        }
    }
    private Boolean checkConnectivity() {
        if (serviceConnected() == false) {
            CommonUtils.printLog("service not connected with client app ..returning");
            if (callerContext != null) {
                CommonUtils.showToast(callerContext,"Service is not connected");
            }
            return false;
        }
        if (CommonUtils.isNetworkAvailable(callerContext) == false) {
            CommonUtils.printLog("network not available..returning");
            if (callerContext != null) {
                CommonUtils.showToast(callerContext,"No Network");
            }
            return false;
        }
        return true;
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            bound = true;
            CommonUtils.printLog("both flag are set");
            CommonUtils.showToast(callerContext,"Connected to Service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            messenger = null;
            CommonUtils.printLog("service disconnected from water app");
            CommonUtils.showToast(callerContext,"Service Disconnected");
        }
    };
}

class IncomingHandler extends Handler {
    static final int MQTT_CONNECTED =1;
    static final int UNABLE_TO_CONNECT =2;
    static final int NO_NETWORK_AVAILABLE =4;
    static final int MQTT_CONNECTION_IN_PROGRESS = 5;
    static final int MQTT_NOT_CONNECTED = 6;

    static final int TOPIC_PUBLISHED = 7;
    static final int ERROR_IN_PUBLISHING = 8;

    Context applicationContext;
    IncomingHandler(Context context) {
        this.applicationContext = context;
    }
    @Override
    public void handleMessage (Message message) {
        switch (message.what) {
            case MQTT_CONNECTED://
                CommonUtils.printLog("mqtt connected");
                CommonUtils.showToast(applicationContext, "Connected!!");
                break;
            case UNABLE_TO_CONNECT:
                CommonUtils.printLog("unable to connect");
                CommonUtils.showToast(applicationContext,"could not connect");
                break;
            case NO_NETWORK_AVAILABLE:
                CommonUtils.showToast(applicationContext,"No Network!!");
                break;
            case MQTT_CONNECTION_IN_PROGRESS:
                CommonUtils.showToast(applicationContext,"Connection in progress!!");
                break;
            case MQTT_NOT_CONNECTED:
                CommonUtils.showToast(applicationContext,"Mqtt is not connected");
                break;
            case TOPIC_PUBLISHED:
                CommonUtils.showToast(applicationContext,"Topic Published");
                break;
            case ERROR_IN_PUBLISHING:
                CommonUtils.showToast(applicationContext,"Error in publishing");
                break;
            default:

        }

    }
}

