package com.minevera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.backend.groupHomeApi.model.GroupHome;
import com.backend.groupHomeApi.model.Product;
import com.backend.groupHomeApi.model.ProductBuy;
import com.backend.groupHomeApi.model.ProductDate;
import com.backend.groupHomeApi.model.ProductHome;
import com.backend.groupHomeApi.model.User;
import com.minevera.activities.NavigationDrawerActivity;
import com.minevera.activities.RegisterActivity;
import com.minevera.asynctasks.GroupHomeActionTask;
import com.minevera.asynctasks.GroupHomeAsyncTask;
import com.minevera.asynctasks.SearchUserAsyncTask;
import com.minevera.auxiliars.ComparatorDates;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pablo on 20/03/2016.
 */
public class Controller {

    private static String TAG = "Controller";
    public static String urlDS = "https://minevera-1288.appspot.com/_ah/api/";
    public static GroupHome gh;
    public static User user;

    /***
     * Recibe la petición del SignIn para buscar el usuario
     * @param userX
     * @param context
     */
    public static void searchUser(User userX, Context context){
        /*Iniciamos sesion con un usuario*/
        user = userX;
        /*Buscamos este usuario en la BBDD*/
        findUser();
        if(gh == null){
            /*El usuario no está registrado*/
            Intent i = new Intent(context, RegisterActivity.class);
            context.startActivity(i);
        }else{
            /*El usuario está registrado y se ha cargado su GH*/
            Intent ii = new Intent(context, NavigationDrawerActivity.class);
            context.startActivity(ii);
        }
    }



    public static void createGH(Activity activity, String password) {
        /*Creamos el grupo de hogar en local con los valores iniciales*/
        createGroupHome(password);
        /*Insertamos en la base de datos*/
        loadActionTask(GroupHomeActionTask.CREATE_GH);
        /*Pasamos a la siguiente actividad correspondiente*/
        Intent i = new Intent(activity, NavigationDrawerActivity.class);
        activity.startActivity(i);
    }

    public static void addUserInGH(String id, String password, Activity activity) {
       /*Añadimos los valores que usaremos para buscar y verificar al usuario para insertarlos en el GH*/
        Controller.gh = new GroupHome();
        Controller.gh.setId(Long.valueOf(id));
        Controller.gh.setPassword(password);
        /*Consultamos e insertamos en la base de datos*/
        loadActionTask(GroupHomeActionTask.ADD_USER);

        if(Controller.gh == null){
        /*Quiere decir que o no existe el grupo de hogar o la contraseña no es correcta*/
            Toast.makeText(activity, R.string.ToastJoinGHIncorrect, Toast.LENGTH_SHORT).show();
        }else {
            /*Pasamos a la siguiente actividad correspondiente*/
            Intent i = new Intent(activity, NavigationDrawerActivity.class);
            activity.startActivity(i);
        }
    }

    public static void loadActionTask(GroupHomeActionTask action){
        GroupHomeAsyncTask asyncTask = new GroupHomeAsyncTask();
        try {
            asyncTask.execute(action).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public static int findByIDHOME(String id, List<ProductHome> list){
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getNameProduct().equals(id)){
                return i;
            }
        }
        return -1;
    }



    /***********************************************METODOS PUBLICOS AUXILIARES*******************************************************/


    public static void showSoftKeyboard(EditText txtEdit, Activity activity) {
        txtEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtEdit, InputMethodManager.SHOW_IMPLICIT);

    }

    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    private static void createGroupHome(String password) {
        GroupHome newGH = new GroupHome();

        List<User> listUser = new ArrayList<User>();
        listUser.add(Controller.user);

        newGH.setUsersList(listUser);
        newGH.setShoppingList(new ArrayList<ProductBuy>());

        newGH.setPassword(password);
        Controller.gh = newGH;

    }




    public static void addProduct(Product myProduct, int number, String date) {
        boolean exist = checkIfExistProduct(myProduct.getNameProduct());
        if(!exist){
            /*Sino existe el producto lo añadimos a nuestra listaProduct(por primera vez)*/
            gh.getProductsList().add(myProduct);
//            actionTaskAddProduct(myProduct);
        }
        /*Lo añadimos al homelist y actualizamos*/
        ProductHome ph = addProductHome(myProduct, number,  date);
        //gh.getHomeList().add(myProduct);
//        actionTaskAddProductHome(ph);
        loadActionTask(GroupHomeActionTask.UPDATE_GH);
    }

    private static boolean checkIfExistProduct(String product) {
        boolean exist = false;
        if(gh.getProductsList() == null) {
            gh.setProductsList(new ArrayList<Product>());
        }
        for (int i = 0; i < gh.getProductsList().size(); i++) {
            if (product.equals(gh.getProductsList().get(i).getNameProduct())){
                exist = true;
                break;
            }
        }
        return exist;
    }

    private static ProductHome addProductHome(Product myProduct, int number, String dateExpired) {

        ProductDate dateP = getProductDate(number, dateExpired);

        //loadActionTask(GroupHomeActionTask.LOAD_GH);
        ProductHome  ph = searchProductHome(myProduct.getNameProduct());

        if(ph==null) {
            //Si el producto no existe, entonces lo creamos
            ph = new ProductHome();
            ph.setNameProduct(myProduct.getNameProduct());
            ph.setImage(myProduct.getImage());
            ph.setHabitual(myProduct.getHabitual());
            ph.setStockQuantity(myProduct.getStockQuantity());
            ph.setKindQuantity(myProduct.getKindQuantity());
            List<ProductDate> newP = new ArrayList<ProductDate>();
            newP.add(dateP);
            ph.setInfoUnits(newP);
            //Añadir a la lista ProductHome
            gh.getHomeList().add(ph);


        }else {
            /*Si el producto si existe, entonces le añadimos a su ProductHome las unidades correspondientes*/
            Log.i(TAG, ph.getNameProduct());
            /*Encontramos la posición del elemento*/
            int i = gh.getHomeList().indexOf(ph);
            /*Lo modificamos y lo reemplazamos*/
            ph.getInfoUnits().add(dateP);
            Collections.sort(ph.getInfoUnits(), new ComparatorDates());
            gh.getHomeList().set(i,ph);
        }
        return ph;
    }

    @NonNull
    private static ProductDate getProductDate(int number, String dateExpired) {
        ProductDate dateP = new ProductDate();
        dateP.setNumber(number);
        dateP.setDateExpired(dateExpired);
        return dateP;
    }



    private static ProductHome  searchProductHome(String nameProduct) {
        try {
            for (int i = 0; i < gh.getHomeList().size(); i++) {
                if (gh.getHomeList().get(i).getNameProduct().equals(nameProduct)) {
                    return gh.getHomeList().get(i);
                }
            }
        }catch (NullPointerException e){
            gh.setHomeList(new ArrayList<ProductHome>());
            return null;
        }
        return null;
    }

    public static void addShop(ProductBuy myProduct) {
       ProductBuy product = searchProductBuy(myProduct.getNameProduct());
        if(product==null) {
            gh.getShoppingList().add(myProduct);
        }else {
            updateProductBuy(myProduct);
        }
        loadActionTask(GroupHomeActionTask.UPDATE_GH);
    }

    private static ProductBuy  searchProductBuy(String nameProduct) {
        try {
            for (int i = 0; i < gh.getShoppingList().size(); i++) {
                if (gh.getShoppingList().get(i).getNameProduct().equals(nameProduct)) {
                    return gh.getShoppingList().get(i);
                }
            }
        }catch (NullPointerException e){
            gh.setShoppingList(new ArrayList<ProductBuy>());
            return null;
        }
        return null;
    }

    private static void  updateProductBuy(ProductBuy myProduct) {
        for (int i = 0; i < gh.getShoppingList().size(); i++) {
            if (gh.getShoppingList().get(i).getNameProduct().equals(myProduct.getNameProduct())) {
                int a = gh.getShoppingList().get(i).getUnits()+myProduct.getUnits();
                gh.getShoppingList().get(i).setUnits(a);

            }
        }
    }

    public static void removeShop(int position) {
       gh.getShoppingList().remove(position);
       loadActionTask(GroupHomeActionTask.UPDATE_GH);
    }


/*************************************************************** CALL ASYNCTASK  *******************************************************/
    /***
     * Busca al usuario en la base de datos
     */
    private static void findUser() {
        SearchUserAsyncTask asynckTask = new SearchUserAsyncTask();
        try {
            asynckTask.execute(user.getEmail()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
