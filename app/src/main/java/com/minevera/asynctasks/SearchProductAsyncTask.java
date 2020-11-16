package com.minevera.asynctasks;

import android.os.AsyncTask;

import com.backend.groupHomeApi.GroupHomeApi;
import com.backend.groupHomeApi.model.ProductHome;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.minevera.Controller;

import java.io.IOException;

/**
 * Created by Pablo on 20/04/2016.
 */
public class SearchProductAsyncTask extends AsyncTask<String, Void, ProductHome> {

    private static GroupHomeApi myApiService = null;


    @Override
    protected ProductHome doInBackground(String... params) {
        if (myApiService == null) {  // Inicializamos el servicio si no está corriendo
            GroupHomeApi.Builder builder = new GroupHomeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(Controller.urlDS);
            myApiService = builder.build();
        }
        ProductHome productHome = null;
        try {
            productHome = myApiService.searchProductHome(Controller.gh.getId(), params[0]).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  productHome;
    }

}