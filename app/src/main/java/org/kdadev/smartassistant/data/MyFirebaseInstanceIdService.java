package org.kdadev.smartassistant.data;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Asus X550ZE on 2/25/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

    }

    private void sendTokenRegisToServer(String token) {
        //Buat kirim token ke server
    }
}
