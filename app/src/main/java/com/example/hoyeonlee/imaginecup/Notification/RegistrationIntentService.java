package com.example.hoyeonlee.imaginecup.Notification;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.microsoft.windowsazure.messaging.NotificationHub;

/**
 * Created by hoyeonlee on 2018. 3. 19..
 */

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    private NotificationHub hub;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String resultString = null;
        String regID = null;
        String storedToken = null;
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        try {
            String FCM_token = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "FCM Registration Token: " + FCM_token);

            // Storing the registration ID that indicates whether the generated token has been
            // sent to your server. If it is not stored, send the token to your server,
            // otherwise your server should have already received the token.
            if (((regID=sharedPreferences.getString("registrationID", null)) == null)){

                NotificationHub hub = new NotificationHub(NotificationSettings.HubName,
                        NotificationSettings.HubListenConnectionString, this);
                Log.d(TAG, "Attempting a new registration with NH using FCM token : " + FCM_token);
                Log.d(TAG, "ANDROID_DEVICE_ID " + android_id);
                regID = hub.register(FCM_token,android_id).getRegistrationId();

                // If you want to use tags...
                // Refer to : https://azure.microsoft.com/en-us/documentation/articles/notification-hubs-routing-tag-expressions/
                // regID = hub.register(token, "tag1,tag2").getRegistrationId();

                resultString = "New NH Registration Successfully - RegId : " + android_id;
                Log.d(TAG, resultString);

                sharedPreferences.edit().putString("registrationID", regID ).apply();
                sharedPreferences.edit().putString("FCMtoken", FCM_token ).apply();
            }

            // Check if the token may have been compromised and needs refreshing.
            else if ((storedToken=sharedPreferences.getString("FCMtoken", "")) != FCM_token) {

                NotificationHub hub = new NotificationHub(NotificationSettings.HubName,
                        NotificationSettings.HubListenConnectionString, this);
                Log.d(TAG, "NH Registration refreshing with token : " + FCM_token);
                regID = hub.register(FCM_token,android_id).getRegistrationId();

                // If you want to use tags...
                // Refer to : https://azure.microsoft.com/en-us/documentation/articles/notification-hubs-routing-tag-expressions/
                // regID = hub.register(token, "tag1,tag2").getRegistrationId();

                resultString = "New NH Registration Successfully - RegId : " + android_id;
                Log.d(TAG, resultString);

                sharedPreferences.edit().putString("registrationID", regID ).apply();
                sharedPreferences.edit().putString("FCMtoken", FCM_token ).apply();
            }

            else {
                resultString = "Previously Registered Successfully - RegId : " + android_id;
            }
        } catch (Exception e) {
            Log.e(TAG, resultString="Failed to complete registration", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
        }

        // Notify UI that registration has completed.
        if (NotiTestActivity.isVisible) {
//            NotiTestActivity.notiTestActivity.ToastNotify(resultString);
        }
    }
}