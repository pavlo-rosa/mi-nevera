package com.minevera.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.minevera.R;
import com.minevera.services.ExpiredDatesService;

/**
 * Created by Pablo on 12/04/2016.
 */

public class PreferencesFragment extends PreferenceFragment implements  SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "PreferencesFragment";
    private static final String KEY_lIST = "list";
    private static final String KEY_CHECK_NOT = "allNotification";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Cargamos el layout*/
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*Ender el escuchador de eventos por si alguna key de preferencias cambia*/
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        /*Enter el escuchador de eventos por si alguna key de preferencias cambia*/
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    /*Modificar Preferencias*/
    public static boolean getBoolean(Context context, final String key, final  boolean defaultValue){
        SharedPreferences shaPref = PreferenceManager.getDefaultSharedPreferences(context);
        return shaPref.getBoolean(key, defaultValue);
    }

    public static String getKeyCheckNot() {
        return KEY_CHECK_NOT;
    }

    public static String getKeylist() {
        return KEY_lIST;
    }

    public static void checkDesactivateNotifications(Context context){
        SharedPreferences shaPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean result = shaPref.getBoolean(KEY_CHECK_NOT, true);
        if(!result){
            context.stopService(new Intent(context, ExpiredDatesService.class));
        }
    }

}
