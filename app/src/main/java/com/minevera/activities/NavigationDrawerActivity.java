package com.minevera.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backend.groupHomeApi.model.Product;
import com.backend.groupHomeApi.model.ProductHome;
import com.backend.groupHomeApi.model.User;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.asynctasks.QueryProductAsyncTask;
import com.minevera.asynctasks.QueryProductAsyncTaskI;
import com.minevera.fragments.ExpiredListFragment;
import com.minevera.fragments.HomeListFragment;
import com.minevera.fragments.MyGroupHomeFragment;
import com.minevera.fragments.NumberPickerItemFragment;
import com.minevera.fragments.PreferencesFragment;
import com.minevera.fragments.ShoppingListFragment;
import com.minevera.services.ExpiredDatesService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//ok
public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ShoppingListFragment.OnListFragmentInteractionListener,
        MyGroupHomeFragment.OnListFragmentInteractionListener,
        HomeListFragment.OnListFragmentInteractionListener,
        ExpiredListFragment.OnListFragmentInteractionListener,
        NumberPickerItemFragment.OnFragmentInteractionListener,
        QueryProductAsyncTaskI {

    private static String TAG = NavigationDrawerActivity.class.getName();
    private NavigationView navigationView;
    private ImageView mPhoto;
    private TextView mName;
    private TextView mEmail;

    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationView(toolbar);
        manageService();
        redirectView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initNavigationView(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);

        mName = (TextView) hView.findViewById(R.id.ndNameUser);
        mEmail = (TextView) hView.findViewById(R.id.ndEmailUser);
        mPhoto = (ImageView) hView.findViewById(R.id.ndImageProfile);

        mName.setText(Controller.user.getName());
        mEmail.setText(Controller.user.getEmail());

        Picasso.with(hView.getContext()).load(Controller.user.getPhoto()).into(mPhoto);
    }


    private void manageService() {
    /*Si esta habilitado arrancamos el servicio*/
        Log.i(TAG, "" + PreferencesFragment.getBoolean(this, PreferencesFragment.getKeyCheckNot(), true));
        if(PreferencesFragment.getBoolean(this,PreferencesFragment.getKeyCheckNot(),true) && !isMyServiceRunning(ExpiredDatesService.class)){
            Intent intent = new Intent(getBaseContext(), ExpiredDatesService.class);
            startService(intent);
        }else if(isMyServiceRunning(ExpiredDatesService.class)){
            PreferencesFragment.checkDesactivateNotifications(getBaseContext());
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                /*Log.i("isMyServiceRunning", "true");*/
                return true;
            }
        }
        /*Log.i("isMyServiceRunning", "false");*/
        return false;
    }
    private void redirectView() {
        String menuFragment = getIntent().getStringExtra("FragmentListExpired");
        if(menuFragment != null){
            goToExpiredList();
        }else{
            goToHome();
        }
    }

    private void goToExpiredList() {
        getIntent().removeExtra("FragmentListExpired");
        Fragment fragment = new ExpiredListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_navegation_drawerID, fragment).commit();
    }


    private void goToHome() {
        Fragment fragment = new HomeListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_navegation_drawerID, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menú de la barra superior
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, PreferencesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Menú de barra lateral
        int id = item.getItemId();
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        if (id == R.id.nav_myInv) {
            //nav_myInv
            fragment = new HomeListFragment();
            fragmentTransaction = true;
        } else if (id == R.id.nav_shoppinglist) {
            // Handle the camera action
            fragment = new ShoppingListFragment();
            fragmentTransaction = true;
        } else if (id == R.id.nav_myGH) {
            fragment = new MyGroupHomeFragment();
            fragmentTransaction = true;
        }else if (id == R.id.nav_expiredList) {
            fragment = new ExpiredListFragment();
            fragmentTransaction = true;
        }else if (id == R.id.nav_closeSesion) {
            Intent i = new Intent(this, SignInActivity.class);
            Bundle b = new Bundle();
            b.putBoolean("close", true);
            i.putExtras(b);
            startActivity(i);
        }else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, PreferencesActivity.class));
        }
        if(fragmentTransaction){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_navegation_drawerID, fragment).commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.i(TAG, "Cancelled scan");
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                barcode = null;
            } else {
                Log.i(TAG, "Scanned: "+result.getContents());
                barcode = result.getContents();
                searchProductInOpenFoodFacts(result.getContents());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private  void searchProductInOpenFoodFacts(String barcode){
        QueryProductAsyncTask asyncTask = new QueryProductAsyncTask();
        asyncTask.delegate = this;
        try {
            asyncTask.execute(barcode).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishprocess(Product result) {
        Intent iInsertProductText = new Intent(this, InsertProductHome3Activity.class);
        if(result != null){
            /*Si se ha encontrado el producto, completar datos necesarios*/
            constructIntentToInsterProduct(result, iInsertProductText);
        }else{
            /*No se ha encontrado el producto en la base de datos, encotonces procedemos a buscar en productList*/
            Product product = searchProductInGroupHome(barcode);
            if(product != null){
                constructIntentToInsterProduct(product, iInsertProductText);
            }else {
                /*Sino encontramos nada procedemos a crear el objeto*/
                iInsertProductText = new Intent(this, InsertProductHome2Activity.class);
                iInsertProductText.putExtra("resultOnlyBarcode",barcode);
            }
        }
        startActivity(iInsertProductText);

    }

    private void constructIntentToInsterProduct(Product result, Intent iInsertProductText) {

        iInsertProductText.putExtra("resultOK",true);
        iInsertProductText.putExtra("resultNameProduct",result.getNameProduct());
        iInsertProductText.putExtra("resultBarcode",result.getBarcode());
        iInsertProductText.putExtra("resultImage",result.getImage());


    }

    private  Product searchProductInGroupHome(String barcode) {
        try {
            for (int i = 0; i < Controller.gh.getProductsList().size(); i++) {
                try {
                    if (Controller.gh.getProductsList().get(i).getBarcode().equals(Long.valueOf(barcode))) {
                        Log.i(TAG, "Encontrado!");
                        return Controller.gh.getProductsList().get(i);
                    }
                }catch (NullPointerException e){}
            }
        }catch (NullPointerException e){
            Controller.gh.setProductsList(new ArrayList<Product>());
            return null;
        }
        return null;
    }

    @Override
    public void onListFragmentInteraction(ProductHome item) {

    }

    @Override
    public void onListFragmentInteraction() {
    }
    @Override
    public void onListFragmentInteraction(User item) {

    }

    @Override
    public void onFragmentInteraction(int newUnits) {

    }
}
