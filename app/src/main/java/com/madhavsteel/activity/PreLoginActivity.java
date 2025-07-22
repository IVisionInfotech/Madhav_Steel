package com.madhavsteel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.madhavsteel.R;
import com.madhavsteel.utils.PermissionUtils;

import java.util.ArrayList;

public class PreLoginActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    private static long back;
    private Context context;
    private CardView cvLogin, cvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        context = PreLoginActivity.this;

        init();
    }

    private void init() {

        cvLogin = findViewById(R.id.cvLogin);
        cvRegister = findViewById(R.id.cvRegister);

        cvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityForResult(context, RegisterActivity.class, 2);
            }
        });
        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityForResult(context, LoginActivity.class, 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 || requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(24);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }
}
