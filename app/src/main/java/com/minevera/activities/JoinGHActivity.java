package com.minevera.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.minevera.Controller;
import com.minevera.R;

/**
 * A login screen that offers login via id/password.
 */
public class JoinGHActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = JoinGHActivity.class.getName();
    private EditText mIdGHView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_gh);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.buttonJoinGH).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mIdGHView = (EditText) findViewById(R.id.IDgh);
        mPasswordView = (EditText) findViewById(R.id.passwordIDgh);

        String id = mIdGHView.getText().toString();
        String password = mPasswordView.getText().toString();

        checkDates(id, password);
    }

    private void checkDates(String id, String password) {
        if (id.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.ToastFieldsAreEmpty, Toast.LENGTH_SHORT).show();
        } else {
            Controller.addUserInGH(id, password, this);
        }
    }
}

