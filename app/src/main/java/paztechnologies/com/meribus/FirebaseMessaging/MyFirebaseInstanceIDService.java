package paztechnologies.com.meribus.FirebaseMessaging;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Admin on 4/12/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MYFIREBASEINSTANCEID";

    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshToken);
        storeToken(refreshToken);
    }

    private void storeToken(String token) {
        //we will save the token in sharedpreferences later
        SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }
}
