package com.minevera.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.backend.registration.Registration;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.minevera.R;

import java.io.IOException;


/**
 * Created by Pablo on 08/03/2016.
 */
public class RegistrationIntentService extends IntentService {
    private static Registration regService = null;
    private static final String TAG = "RegIntentService";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    public void onHandleIntent(Intent intent) {

        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);

            sendRegistrationToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sendRegistrationToServer(String token) {
        Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setRootUrl("https://dsminevera.appspot.com/_ah/api/");
        Registration regService = builder.build();
        try {
            regService.register(token).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
