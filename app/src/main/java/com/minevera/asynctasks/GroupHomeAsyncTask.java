package com.minevera.asynctasks;


import android.os.AsyncTask;

import com.backend.groupHomeApi.GroupHomeApi;
import com.backend.groupHomeApi.model.ProductBuy;
import com.backend.groupHomeApi.model.ProductHome;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.minevera.Controller;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Pablo on 20/03/2016.
 */
//ok
public class GroupHomeAsyncTask extends AsyncTask<GroupHomeActionTask, Void, Boolean>{


    private static GroupHomeApi myApiService = null;

    @Override
    protected Boolean doInBackground(GroupHomeActionTask... params) {
        if(myApiService == null) {  // Inicializamos el servicio si no está corriendo
            GroupHomeApi.Builder builder = new GroupHomeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(Controller.urlDS);
            myApiService = builder.build();
        }

        try {
            switch (params[0]){
                case CREATE_GH:         Controller.gh = myApiService.insert(Controller.gh).execute();
                                        break;

                case LOAD_GH:           Controller.gh = myApiService.get(Controller.gh.getId()).execute();
                                        break;

                case UPDATE_GH:         Controller.gh = myApiService.update(Controller.gh.getId(), Controller.gh).execute();
                                        break;

                case ADD_USER:          /*Controller.gh solo contiene el id y contraseña que ha introducido en el formulario*/
                                        Controller.gh = myApiService.addUserInGroupHome(Controller.gh.getId(), Controller.gh.getPassword(), Controller.user).execute();
                                        break;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (Controller.gh.getHomeList() == null){
            Controller.gh.setHomeList(new ArrayList<ProductHome>());
        }
        if (Controller.gh.getShoppingList() == null){
            Controller.gh.setShoppingList(new ArrayList<ProductBuy>());
        }

    }
}
