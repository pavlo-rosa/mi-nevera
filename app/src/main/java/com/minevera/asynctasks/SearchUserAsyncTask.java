package com.minevera.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.backend.groupHomeApi.GroupHomeApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.minevera.Controller;
import com.minevera.activities.SignInActivity;

import java.io.IOException;

/**
 * Created by Pablo on 22/03/2016.
 */
//ok
public class SearchUserAsyncTask extends AsyncTask<String, Void, Void> {

    private static GroupHomeApi myApiService = null;

    @Override
    protected Void doInBackground(String... params) {
        if (myApiService == null) {  // Inicializamos el servicio si no está corriendo
            GroupHomeApi.Builder builder = new GroupHomeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(Controller.urlDS);
            myApiService = builder.build();
        }
        try {
            Controller.gh = myApiService.searchUser(params[0]).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
