package com.abhilash.smartcampusmqttlib.WebServices;




import com.abhilash.smartcampusmqttlib.HelperClass.CommonUtils;
import com.abhilash.smartcampusmqttlib.ServiceAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import cz.msebera.android.httpclient.Header;

/**
 * Created by shashankshekhar on 20/11/15.
 */
public class RestWebService {
    public static final String LOCAL_IP = "http://10.16.37.222/broker/mobile";
    public static final String CLOUD_IP = "http://smartx.cloudapp.net/broker/mobile";
    public void initiateRestCall () {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(LOCAL_IP, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                CommonUtils.printLog("on success called with" + Integer.toString(statusCode));
                // do a publish message at the end of this call with the received numbers

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                CommonUtils.printLog("on failure called with" + Integer.toString(statusCode));
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
