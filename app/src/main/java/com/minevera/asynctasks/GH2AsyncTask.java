package com.minevera.asynctasks;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.backend.groupHomeApi.GroupHomeApi;
import com.backend.groupHomeApi.model.GroupHome;
import com.backend.groupHomeApi.model.ProductHome;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.activities.NavigationDrawerActivity;
import com.minevera.fragments.PreferencesFragment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Pablo on 07/04/2016.
 */
//ok
public class GH2AsyncTask extends AsyncTask<Context, Void, List<ProductHome>> {

    private final String TAG = "GH2AsyncTask";
    private Context context;
    private static GroupHomeApi myApiService = null;

    @Override
    protected List<ProductHome> doInBackground(Context... params) {
        GroupHome aux = new GroupHome();
        context = params[0];
        if(myApiService == null) {  // Inicializamos el servicio si no está corriendo
            GroupHomeApi.Builder builder = new GroupHomeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(Controller.urlDS);
            myApiService = builder.build();
        }

        try {
            aux = myApiService.get(Controller.gh.getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aux.getHomeList();
    }



    @Override
    protected void onPostExecute(List<ProductHome> resultDB){
        super.onPostExecute(resultDB);
        if(resultDB == null){
            resultDB = new ArrayList<ProductHome>();
        }
        List<ProductHome> result = new ArrayList<ProductHome>();
        for (int i = 0; i < resultDB.size(); i++){
            for (int j = 0; j < resultDB.get(i).getInfoUnits().size(); j++) {
                if (isProductExpiredNot(resultDB.get(i).getInfoUnits().get(j).getDateExpired())) {
                    /*El producto: No consumido y Si expirado --> add result*/
                    result.add(resultDB.get(i));
                }
            }
        }
        if(!result.isEmpty()){
            for (int i = 0; i < result.size(); i++){
                Log.i(TAG, "Notificación: "+ result.get(i).getNameProduct() + " " + result.get(i).getInfoUnits().get(0).getDateExpired());
            }
            sendNotification();
        }

    }


    private boolean isProductExpiredNot(String expiredDate) {
        try {
            /**Obtenemos las fechas enviadas en el formato a comparar*/
            Date currentDate = new Date();
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            String systemDate=formateador.format(currentDate);
            /*14/04/2016*/
            Date dateSystem = getDateSystemPreference(formateador, systemDate);
            /*13/04/2016*/
            Date dateProduct = formateador.parse(expiredDate);
            /*La fecha transformada y la del producto han de ser iguales*/
            if ( dateSystem.before(dateProduct) ){
                //La Fecha 1 es menor
                return false;
            }else{
                if ( dateProduct.before(dateSystem) ){
                    //La Fecha 1 es Mayor
                    return false;
                }else{
                    //Las Fechas Son iguales
                    return true;
                }
            }
        } catch (ParseException e) {
        }
        return false;
    }

    private Date getDateSystemPreference(SimpleDateFormat formateador, String systemDate) throws ParseException {
        Date date =formateador.parse(systemDate);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String frecuency =  SP.getString(PreferencesFragment.getKeylist(), "0");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(frecuency));  // numero de días a añadir, o restar en caso de días<0

        return calendar.getTime();
    }

    private  void sendNotification(){
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, NavigationDrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("FragmentListExpired", "1");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        //PendingIntent contentIntent =  PendingIntent.getActivity(this, 0, new Intent(this, NavigationDrawerActivity.class), 0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.fridge_filled)
                .setContentTitle("Mi Nevera")
                .setContentText("Existen productos prosperos a caducar")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}