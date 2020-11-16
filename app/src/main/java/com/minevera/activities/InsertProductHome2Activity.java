package com.minevera.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backend.groupHomeApi.model.Product;
import com.backend.groupHomeApi.model.ProductHome;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.fragments.DateFragmentText;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Pablo on 04/04/2016.
 */
//ok
public class InsertProductHome2Activity extends AppCompatActivity implements View.OnClickListener, DateFragmentText.OnFragmentInteractionListener{
    private  final String TAG = "InsertProductHome2";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private AutoCompleteTextView mProduct;
    private NumberPicker mNumberQuantity, mNumberStock;
    private Spinner mKindQuantity;
    private TextView mKindStock;
    private ImageView mImageView;
    private String[] kinds;
    private LinearLayout mLayout,mIncludeLayout,mLayoutBarcode;
    private Boolean habitual;
    private Bitmap imageBitmap;
    private String date;
    private TextView mtextDateExp,mBarcode;
    private RadioGroup mRadioGroup;
    private ArrayAdapter<String> mAdapterAutocompleteText, mAdapSpinnerKindQuantaty;
    private RadioButton mRadioButton;
    private Product myProduct;
    private FloatingActionButton mAddImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertproduct_text);
        getSupportActionBar().setTitle("Añadir Producto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initElements();
        checkIntent();
    }

    private void checkIntent() {
        mBarcode = (TextView) findViewById(R.id.npt_imagetextViewBarcode);
        mLayoutBarcode = (LinearLayout) findViewById(R.id.npt_areaBarcode);
        if(getIntent().hasExtra("resultOnlyBarcode")) {
            Toast.makeText(this, R.string.productNoFound, Toast.LENGTH_LONG).show();
            Long barcode = Long.parseLong(getIntent().getStringExtra("resultOnlyBarcode"));
            myProduct.setBarcode(barcode);
            mLayoutBarcode.setVisibility(View.VISIBLE);
            mBarcode.setText(myProduct.getBarcode().toString());
            getIntent().removeExtra("resultOnlyBarcode");
        }
    }

    private void initElements() {
        myProduct = new Product();
        kinds = getResources().getStringArray(R.array.ipt_kinds);
        mIncludeLayout = (LinearLayout) findViewById(R.id.npt_includeNewProduct);

        mProduct = (AutoCompleteTextView) findViewById(R.id.ipt_nameProduct);


        Controller.showSoftKeyboard(mProduct, this);

        mNumberQuantity = (NumberPicker) findViewById(R.id.ipt_numberQuantiy);
        mNumberQuantity.setMinValue(1);
        mNumberQuantity.setMaxValue(1000);

        mLayout = (LinearLayout) findViewById(R.id.ipt_layput);
        mKindQuantity = (Spinner) findViewById(R.id.ipt_kindQuantity);

        findViewById(R.id.ipt_calendarButton).setOnClickListener(this);

        mtextDateExp = (TextView) findViewById(R.id.ipt_calendarText);
        date = "01/01/9999";
        initializeSpinnerQuantity();
        /*Comprobar que el elemento es nuevo*/
        initializeNewProduct();
    }


    private void initializeSpinnerQuantity() {

        mAdapSpinnerKindQuantaty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kinds);
        mKindQuantity.setAdapter(mAdapSpinnerKindQuantaty);
        mKindQuantity.setSelection(0);
        mKindQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mKindStock.setText(mKindQuantity.getSelectedItem().toString());
                }catch (NullPointerException e){ }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeNewProduct(){

        final View v = (View) findViewById(R.id.npt_allStock);
        mImageView = (ImageView) findViewById(R.id.ipt_imageView);
        mKindStock = (TextView) findViewById(R.id.npt_kindStock);
        mAddImage = (FloatingActionButton) findViewById(R.id.npt_imageButtonF);
        mAddImage.setOnClickListener(this);
//        findViewById(R.id.npt_imageButton).setOnClickListener(this);
        habitual = false;
        v.setVisibility(View.GONE);
        mRadioGroup = (RadioGroup) findViewById(R.id.npt_radioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mRadioButton = (RadioButton) findViewById(checkedId);
                RadioButton radioB = (RadioButton) findViewById(R.id.npt_radioButtonTemp);
                Log.i(TAG, group.getCheckedRadioButtonId() + "--" + checkedId + "--" + mRadioButton.getId());
                if ((radioB.getText()).equals(mRadioButton.getText())) {
                    habitual = false;
                    v.setVisibility(View.GONE);
                } else {
                    habitual = true;
                    v.setVisibility(View.VISIBLE);
                    mNumberStock = (NumberPicker) findViewById(R.id.npt_numberStock);
                    mNumberStock.setMinValue(1);
                    mNumberStock.setMaxValue(1000);
                    mKindStock.setText(mKindQuantity.getSelectedItem().toString());
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        /*Solo sera el R.id.ipt_add*/
        switch (v.getId()){
            case R.id.npt_imageButtonF: dispatchTakePictureIntent();
                break;
            case R.id.ipt_calendarButton: putDataExpired();
                break;
        }
    }

    private void putDataExpired() {
        FragmentManager fm = getSupportFragmentManager();
        DateFragmentText dateFragment = new DateFragmentText();
        dateFragment.show(fm, "fragment_date_text");
    }

    private void addProduct() {
        String name = mProduct.getText().toString();
        int number = mNumberQuantity.getValue();
        if (name.isEmpty() || (mKindQuantity.getSelectedItem().toString()).isEmpty()) {
            Snackbar.make(mLayout, R.string.ToastFieldsAreEmpty, Snackbar.LENGTH_SHORT).show();
        } else if(checkName(name,mBarcode.getText().toString() )){
            myProduct.setNameProduct(name);
            myProduct.setHabitual(habitual);
            myProduct.setKindQuantity(mKindQuantity.getSelectedItem().toString());
            if(imageBitmap != null){
                myProduct.setImage(Controller.BitMapToString(imageBitmap));
            }
            if(mLayoutBarcode.getVisibility() == View.VISIBLE){
                myProduct.setBarcode(Long.parseLong(mBarcode.getText().toString()));
            }
            if(habitual){
                myProduct.setStockQuantity(mNumberStock.getValue());
            }
            Controller.addProduct(myProduct, number, date);
            finish();
        }
    }

    private boolean checkName(String name, String s){
        Long barcode;
        if(s.equalsIgnoreCase("No establecido")){
            barcode=Long.parseLong("-1");
        }else{
            barcode= Long.parseLong(s);
        }
        for (int i = 0; i < Controller.gh.getProductsList().size(); i++) {
            if (name.equalsIgnoreCase(Controller.gh.getProductsList().get(i).getNameProduct()) && !Controller.gh.getProductsList().get(i).getBarcode().equals(barcode)) {
                //Barcode escaneado != Barcode Producto Almacenado
                Snackbar.make(mLayout, R.string.ToastNameExist, Snackbar.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insertproducttext, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            addProduct();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String result) {
        if(!result.equals("none")){
            date = result;
            mtextDateExp.setText(date);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            findViewById(R.id.ipt_imagetextView).setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }
}
