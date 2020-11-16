package com.minevera.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backend.groupHomeApi.model.Product;
import com.backend.groupHomeApi.model.ProductBuy;
import com.minevera.Controller;
import com.minevera.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
//ok
public class InsertShoppingTextActivity extends AppCompatActivity {

    private Spinner mSpinner;
    private NumberPicker mNumber;
    private AutoCompleteTextView mNameProduct;
    private ImageView mImage;
    private TextView mTextNoImage;
    private ProductBuy myProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_shopping_text);
        getSupportActionBar().setTitle("Añadir al Carrito");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_insertproducttext, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        myProduct = new ProductBuy();
        mImage = (ImageView) findViewById(R.id.ispt_imageView);
        mTextNoImage = (TextView) findViewById(R.id.ispt_imagetextView);
        mNumber = (NumberPicker) findViewById(R.id.ispt_numberQuantiy);
        mSpinner = (Spinner) findViewById(R.id.ispt_kindQuantity);

        mNumber.setMinValue(1);
        mNumber.setMaxValue(1000);
        loadProductAutoCompleteTextView();
        initializeSpinner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {
            checkDates();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkDates() {
        String name = mNameProduct.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, R.string.ToastFieldsAreEmpty, Toast.LENGTH_SHORT).show();
        } else {
            myProduct.setNameProduct(name);
            myProduct.setUnits(mNumber.getValue());
            myProduct.setKindQuantity(mSpinner.getSelectedItem().toString());

            Controller.addShop(myProduct);
            finish();
        }
    }

    private void initializeSpinner() {
        String[] kinds = getResources().getStringArray(R.array.ipt_kinds);
        ArrayAdapter<String> mAdapSpinnerKindQuantaty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kinds);
        mSpinner.setAdapter(mAdapSpinnerKindQuantaty);
    }


    private void loadProductAutoCompleteTextView() {
        mNameProduct = (AutoCompleteTextView) findViewById(R.id.ispt_nameProduct);
        mNameProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                boolean find = false;
                for (int i = 0; i < Controller.gh.getProductsList().size(); i++){
                    if(s.toString().equals(Controller.gh.getProductsList().get(i).getNameProduct())){
                        autoSelectSpinnerKing(s.toString(),i);
                        //El elemento exsite
                        mImage.setVisibility(View.VISIBLE);
                        mTextNoImage.setVisibility(View.GONE);
                        myProduct.setImage(Controller.gh.getProductsList().get(i).getImage());

                        printImage(Controller.gh.getProductsList().get(i));
                        mSpinner.setEnabled(false);
                        mSpinner.setClickable(false);
                        find = true;
                        break;
                    }
                }
                if (!find){
                   /*El elemento introducido no existe*/
                    mImage.setVisibility(View.GONE);
                    mTextNoImage.setVisibility(View.VISIBLE);
                    myProduct.setImage(null);
                    mSpinner.setSelection(0);
                    mSpinner.setEnabled(true);
                    mSpinner.setClickable(true);
                }

            }

            private void autoSelectSpinnerKing(String kind, int i) {
                if (Controller.gh.getProductsList().get(i).getKindQuantity().equals("Unidades")){
                    mSpinner.setSelection(0);
                } else if (Controller.gh.getProductsList().get(i).getKindQuantity().equals("Kilogramos")) {
                    mSpinner.setSelection(1);
                } else if (Controller.gh.getProductsList().get(i).getKindQuantity().equals("Gramos")) {
                    mSpinner.setSelection(2);
                } else if (Controller.gh.getProductsList().get(i).getKindQuantity().equals("Litros")) {
                    mSpinner.setSelection(3);
                }
            }
        });
        List<String> products = new ArrayList<String>();
        try {
            for (int i = 0; i < Controller.gh.getProductsList().size(); i++){
                products.add(Controller.gh.getProductsList().get(i).getNameProduct());
            }
        }catch (NullPointerException e){
            Controller.gh.setProductsList(new ArrayList<Product>());
            products = new ArrayList<String>();
        }
        ArrayAdapter<String> mAdapterAutocompleteText = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,products);
        mNameProduct.setAdapter(mAdapterAutocompleteText);
    }
    private void printImage(Product product) {
        try {
            if(product.getImage().contains("NeedStringToBitMap#")){
                String image = product.getImage().replace("NeedStringToBitMap#","");
                Bitmap bitmap = Controller.StringToBitMap(image);
                mImage.setImageBitmap(bitmap);
            }else {
                Picasso.with(this).load(product.getImage()).into(mImage);
            }
        }catch (NullPointerException e){
            // Log.i(TAG, mValues.get(position).getNameProduct()+" no tiene imagen");
        }
    }
}
