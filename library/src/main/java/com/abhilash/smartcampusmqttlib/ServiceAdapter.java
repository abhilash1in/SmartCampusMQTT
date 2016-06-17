package com.abhilash.smartcampusmqttlib;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import android.os.RemoteException;
import android.view.View;

import com.abhilash.smartcampusmqttlib.HelperClass.CommonUtils;

public class ServiceAdapter implements SmartXLibConstants {

    Context callerContext = null;
    // TODO: 14/02/16  initialise it separately and send a call to BGS to update its storage
    /*
    each application that uses BG service will have its own id.
     */
    String applicationId = null;

    public ServiceAdapter(Context context) {
        callerContext = context;
    }

    public void startAndBindToService(SCServiceConnector connector) {
        if (serviceConnected() == true) {
            CommonUtils.showToast(callerContext, "Service already connected");
            return;
        }
        ComponentName componentName = new ComponentName(SERVICE_PACKAGE_NAME,
                SERVICE_CLASS_NAME);
        Intent intent = new Intent();
        intent.setComponent(componentName);
        callerContext.startService(intent);
        Boolean isConnected = callerContext.bindService(intent, connector, Context.BIND_IMPORTANT);
    }

    public void unbindFromService(SCServiceConnector connector) {
        if (serviceConnected() == false) {
            CommonUtils.showToast(callerContext, "Already Disconnected");
        }
        callerContext.unbindService(connector);

    }

    public Boolean serviceConnected() {
        return SCServiceConnector.isServiceConnected();
    }

    public void publishGlobal(String topicName, String eventName, String dataString,Messenger replyMessenger) {
        if (checkConnectivity() == false) {
            return;
        }
        Message messageToPublish = Message.obtain(null, PUBLISH_MESSAGE);
        Bundle bundleToPublish = new Bundle();
        // TODO: 31/05/16 move these strings as constants
        bundleToPublish.putString("topicName", topicName);
        bundleToPublish.putString("eventName", eventName);
        bundleToPublish.putString("dataString", dataString);
        messageToPublish.setData(bundleToPublish);
        messageToPublish.replyTo = replyMessenger;
        try {
            SCServiceConnector.getMessenger().send(messageToPublish);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("remote Exception,Could not send message");
        }

    }

    public String subscribeToTopic(String topicName,Messenger replyMessenger) {
        // TODO: 12/11/15 this should return a subscribe id to the caller hence the String return type
        if (checkConnectivity() == false) {
            return null;
        }
        CommonUtils.printLog("call to subscribe made from application");
        Message messageToSubscribe = Message.obtain(null, SUBSCRIBE_TO_TOPIC);
        Bundle bundle = new Bundle();
        bundle.putString("topicName", topicName);
        messageToSubscribe.setData(bundle);
        messageToSubscribe.replyTo = replyMessenger;
        try {
            SCServiceConnector.getMessenger().send(messageToSubscribe);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("exception while trying to subscribe");
        }
        return null;
    }

    public void unsubscribeFromTopic(String topicName,Messenger replyMessenger) {
        if (checkConnectivity() == false) {
            return;
        }
        Message unsubscribe = Message.obtain(null, UNSUBSCRIBE_TO_TOPIC);
        Bundle bundle = new Bundle();
        bundle.putString("topicName", topicName);
        unsubscribe.setData(bundle);
        unsubscribe.replyTo = replyMessenger;
        try {
            SCServiceConnector.getMessenger().send(unsubscribe);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("exception while sending message for unsubscribe");
        }
    }

    private Boolean checkConnectivity() {
        if (serviceConnected() == false) {
            CommonUtils.printLog("service not connected with client app ..returning");
            if (callerContext != null) {
                CommonUtils.showToast(callerContext, "Service is not connected");
            }
            return false;
        }
        return true;
    }
    public void checkMqttConnection (Messenger replyMessenger) {
        if (!serviceConnected()) {
            CommonUtils.printLog("service not connected .. returning");
//            CommonUtils.showToast(getApplicationContext(), "Service not running");
            return;
        }
        Message message = Message.obtain(null,CHECK_MQTT_CONNECTION);
        message.replyTo = replyMessenger;
        try {
            SCServiceConnector.getMessenger().send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("remote Exception,Could not send message");
        }
    }
    /*
    write down methods for connecting and disconnecting to mqtt
     */

    public void connectMqtt(Messenger replyMessenger) {
        if (!serviceConnected()) {
            CommonUtils.printLog("service not connected .. returning from service adapter");
            return;
        }
        Message message = Message.obtain(null, CONNECT_MQTT);
        message.replyTo = replyMessenger;
        try {
            SCServiceConnector.getMessenger().send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("remote Exception,Could not send message");
        }
    }
    public void disconnectMqtt (Messenger replyMessenger) {
        if (!serviceConnected()) {
            CommonUtils.printLog("service not connected .. returning from service adapter");
            return;
        }
        Message message = Message.obtain(null, DISCONNECT_MQTT);
        message.replyTo = replyMessenger;
        try {
            SCServiceConnector.getMessenger().send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("remote Exception,Could not send message");
        }
    }
}

