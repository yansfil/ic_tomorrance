package com.tomorrance.yonsei.tomo.Notification;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.microsoft.windowsazure.messaging.NotificationHub;

/**
 * Created by hoyeonlee on 2018. 3. 19..
 */

public class MyInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String FCM_token) {
        // TODO: Implement this method to send token to your app server.
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        NotificationHub hub = new NotificationHub(NotificationSettings.HubName,
                NotificationSettings.HubListenConnectionString, this);
        Log.d(TAG, "Attempting a new registration with NH using FCM token : " + FCM_token);
        Log.d(TAG, "ANDROID_DEVICE_ID " + android_id);
        try{
            String regID = hub.register(FCM_token,android_id).getRegistrationId();
            Log.d(TAG, "Notification HUB _ REG_ID : " + regID);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
