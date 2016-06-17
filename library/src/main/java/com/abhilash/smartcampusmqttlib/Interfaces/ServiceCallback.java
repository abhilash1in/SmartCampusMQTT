package com.abhilash.smartcampusmqttlib.Interfaces;

/**
 * Created by shashankshekhar on 26/02/16.
 */

public interface ServiceCallback {


//    void subscriptionSuccess(String subscriptionId);
//    void publishSuccess();
//    void publishError();
//    void subscriptionError();
    void messageReceivedFromService(int number);

}
