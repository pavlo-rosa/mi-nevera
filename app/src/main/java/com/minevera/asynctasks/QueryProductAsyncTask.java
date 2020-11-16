package com.minevera.asynctasks;

import android.os.AsyncTask;

import com.backend.groupHomeApi.model.Product;
import com.backend.groupHomeApi.model.ProductHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Pablo on 31/03/2016.
 */
//ok
public class QueryProductAsyncTask extends AsyncTask<String, Void, Product> {

    private final String TAG = QueryProductAsyncTask.class.getName();

    public QueryProductAsyncTaskI delegate;
    @Override
    protected Product doInBackground(String... params) {
       Product resultProduct = new Product();
        String url2 = "http://world.openfoodfacts.org/api/v0/product/"+params[0]+".json";
        URL url = null;
        try {
            url = new URL(url2);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        // read the response
        System.out.println("Response Code: " + conn.getResponseCode());
        InputStream in = new BufferedInputStream(conn.getInputStream());

        String resultQuery = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
        //Log.i(TAG, result);
        /*Obtenemos del JSON todo lo que necesitamos*/

            JSONObject object = new JSONObject(resultQuery); //Creamos un objeto JSON a partir de la cadena
            String find = object.getString("status_verbose");
            if(find.equals("product found")){
                object = object.optJSONObject("product");
                resultProduct.setBarcode(Long.valueOf(params[0]).longValue());
                if(!object.getString("quantity").equalsIgnoreCase("")){
                    resultProduct.setNameProduct(object.getString("product_name") + " - " + object.getString("quantity"));
                }else{
                    resultProduct.setNameProduct(object.getString("product_name"));
                }
                resultProduct.setImage(object.getString("image_small_url"));
//                resultProduct.setQuantity(object.getString("quantity"));
            }else{
                return null;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultProduct;
    }// protected Void doInBackground(String... params)

   @Override
    protected void onPostExecute(Product s) {
        super.onPostExecute(s);
        delegate.finishprocess(s);
    }

}
