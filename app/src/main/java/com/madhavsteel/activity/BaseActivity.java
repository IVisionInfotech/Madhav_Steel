package com.madhavsteel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.madhavsteel.utils.Session;
import com.madhavsteel.utils.TopExceptionHandler;

public class BaseActivity extends AppCompatActivity {

    private Context context;
    public Session session;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        session = new Session(context);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler("/mnt/sdcard/", context));
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public void showProgressDialog(Context context, String msg) {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage(msg);
        if (!((Activity) context).isFinishing()) {
            pDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    public void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .create()
                .show();
    }

    public void goToActivity(Context context, Class aClass) {
        startActivity(new Intent(context, aClass));
    }

    public void goToActivityForResult(Context context, Class aClass, int resultCode) {
        Intent intent = new Intent(context, aClass);
        startActivityForResult(intent, resultCode);
    }

    public void setResultOfActivity(int resultCode) {
        Intent intent = new Intent();
        setResult(resultCode, intent);
        intent.putExtra("result",resultCode);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void setCancelResultOfActivity(int resultCode) {
        Intent intent = new Intent();
        setResult(resultCode, intent);
        intent.putExtra("result",resultCode);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    public void textGradient(final TextView view) {
        TextPaint paint = view.getPaint();
        float width = paint.measureText("Tianjin, China");
        Shader textShader = new LinearGradient(0, 0, width, view.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        view.getPaint().setShader(textShader);
    }
}
