package com.minevera.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.minevera.Controller;
import com.minevera.R;


public class RegisterActivity extends AppCompatActivity  {

    private static String TAG = RegisterActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClickCreateGH(final View view) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_password_gh, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = initEditText(promptView);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkPassword(editText);

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alert.show();
    }

    public void onClickJoinGH(View view) {
        Intent i2 = new Intent(this, JoinGHActivity.class);
        startActivity(i2);
    }

    @NonNull
    private EditText initEditText(View promptView) {
        final EditText editText = (EditText) promptView.findViewById(R.id.dpedittext);
        editText.setSingleLine(true);
        editText.requestFocus();
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        return editText;
    }

    private void checkPassword(EditText editText) {
        if (editText.getText().toString().matches("")) {
            Toast.makeText(RegisterActivity.this, R.string.ToastPasswordIsEmpty, Toast.LENGTH_SHORT).show();
        } else {
            Controller.createGH(this, editText.getText().toString());
        }
    }

}
