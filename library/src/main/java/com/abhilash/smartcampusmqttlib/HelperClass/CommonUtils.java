package com.abhilash.smartcampusmqttlib.HelperClass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import com.abhilash.smartcampusmqttlib.SmartXLibConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shashankshekhar on 03/11/15.
 */
public class CommonUtils implements SmartXLibConstants {
    private static final String SMART_CAMPUS_FOLDER_NAME = "SmartCampus";
    private static final String SMART_CAMPUS_LOG_FILE_NAME = "SmartCampusLog.txt";

     // methods
    public static void writeDataToLogFile (String appName, String text) {
        File smartCampusDirectory = new File(Environment.getExternalStorageDirectory(),SMART_CAMPUS_FOLDER_NAME);
        if (smartCampusDirectory.exists() == false) {
            if (! smartCampusDirectory.mkdirs()) {
                Log.d(MY_TAG, "failed to create directory");
                return;
            }
        }
        File logFile = new File(smartCampusDirectory,SMART_CAMPUS_LOG_FILE_NAME);
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,true));
            buf.append(currentTime() + ":" + appName + " : " + text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            Log.i(MY_TAG,"file write failed");
            return;
        }
    }
    public static boolean deleteLogFile () {
        File smartCampusDirectory = new File(Environment.getExternalStorageDirectory(),SMART_CAMPUS_FOLDER_NAME);
        if (smartCampusDirectory.exists() == false) {
            return false;
        }
        File logFile = new File(smartCampusDirectory,SMART_CAMPUS_LOG_FILE_NAME);
        boolean isDeleted = logFile.delete();
        return isDeleted;
    }
    public static String currentTime () {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = simpleDateFormat.format(new Date());
            return currentTime;

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(MY_TAG,"could not get current time");
        }
        return null;
    }
    public static void printLog (String message) {
        Log.i(MY_TAG,message);
    }
    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    public static boolean checkMainThread () {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            // Current Thread is Main Thread.
            return true;
        }
        return false;
    }
    public static boolean isNetworkAvailable(Context appContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
