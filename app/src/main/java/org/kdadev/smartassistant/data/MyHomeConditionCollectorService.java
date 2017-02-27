package org.kdadev.smartassistant.data;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

/**
 * Created by Albert on 25/2/2017.
 */

public class MyHomeConditionCollectorService extends Service {
    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 10000); // 1 second

    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 100000); // 10 seconds
        }
    };

    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");
        Random rn = new Random();
        int n = 100 -10 +1;
        int temp = rn.nextInt() % n;
        int humid = rn.nextInt() % n;
        int bright = rn.nextInt() % n;
        intent.putExtra("temp", temp);
        intent.putExtra("humid", humid);
        intent.putExtra("bright",bright);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }
}
