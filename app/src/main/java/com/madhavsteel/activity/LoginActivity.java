package com.madhavsteel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.madhavsteel.R;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.RealmController;
import com.madhavsteel.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private Context context;
    private TextView tvSignUp, tvLogin, tvForgotPin;
    private EditText etUsername, etPin11, etPin12, etPin13, etPin14;
    boolean updateFlag = false;
    private Realm realm;
    private String username = "", pin = "", pin1 = "", pin2 = "", pin3 = "", pin4 = "", firebaseId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;
        session = new Session(context);
        realm = RealmController.with(this).getRealm();
        RealmController.with(context).refresh();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    firebaseId = task.getResult().getToken();
                    Log.e(TAG, "onComplete: " + firebaseId);
                });

        init();
    }

    private void init() {
        tvSignUp = findViewById(R.id.tvSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        tvForgotPin = findViewById(R.id.tvForgotPin);

        etUsername = findViewById(R.id.etUsername);
        etPin11 = findViewById(R.id.etPin11);
        etPin12 = findViewById(R.id.etPin12);
        etPin13 = findViewById(R.id.etPin13);
        etPin14 = findViewById(R.id.etPin14);

        etPin11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin11.getText().toString().length() == 1) {
                    etPin12.requestFocus();
                }
            }
        });
        etPin12.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin12.getText().toString().length() == 1) {
                    etPin13.requestFocus();
                } else {
                    etPin11.requestFocus();
                }
            }
        });
        etPin13.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin13.getText().toString().length() == 1) {
                    etPin14.requestFocus();
                } else {
                    etPin12.requestFocus();
                }
            }
        });
        etPin14.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin14.getText().toString().length() == 1) {
                    hideSoftKeyboard();
                } else {
                    etPin13.requestFocus();
                }
            }
        });

        tvLogin.setOnClickListener(v -> validate());
        tvSignUp.setOnClickListener(v -> goToActivityForResult(context, RegisterActivity.class,2));
        tvForgotPin.setOnClickListener(v -> goToActivityForResult(context, ForgotPinActivity.class, 6));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void validate() {
        username = etUsername.getText().toString();
        pin1 = etPin11.getText().toString();
        pin2 = etPin12.getText().toString();
        pin3 = etPin13.getText().toString();
        pin4 = etPin14.getText().toString();

        pin = pin1 + pin2 + pin3 + pin4;

        if (username.isEmpty()) {
            etUsername.setError("Invalid mobile number");
            etUsername.requestFocus();
        } else if (username.length() < 10) {
            etUsername.setError("Enter 10 digit mobile number");
            etUsername.requestFocus();
        } else if (pin1.isEmpty() ) {
            etPin11.setError("Invalid digit");
            etPin11.requestFocus();
        } else if (pin2.isEmpty()) {
            etPin12.setError("Invalid digit");
            etPin12.requestFocus();
        } else if (pin3.isEmpty() ) {
            etPin13.setError("Invalid digit");
            etPin13.requestFocus();
        } else if (pin4.isEmpty()) {
            etPin14.setError("Invalid digit");
            etPin14.requestFocus();
        } else {
            login(pin);
        }
    }

    private void login(final String pin) {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.login, response -> {
            Log.e(TAG, response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.optInt("status");
                showSnackBar(tvSignUp, jsonObject.optString("message"));
                showToast(jsonObject.optString("message"));
                if (status == 1) {
                    JSONObject result = jsonObject.getJSONObject("result");
                    User user = RealmController.with(context).getUser();
                    if (user != null) {
                        updateFlag = true;
                        realm.beginTransaction();
                    } else {
                        updateFlag = false;
                        user = new User();
                    }

                    user.setUserId(result.optString("userId"));
                    user.setName(result.optString("name"));
                    user.setEmail(result.optString("email"));
                    user.setContact(result.optString("contact"));
                    user.setPIN(result.optString("PIN"));
                    user.setOtp(result.optString("otp"));
                    user.setEmailVerified(result.optString("emailVerified"));
                    user.setOtpVerified(result.optString("otpVerified"));
                    user.setStatus(result.optString("status"));
                    user.setDateTimeAdded(result.optString("dateTimeAdded"));

                    if (updateFlag) {
                        realm.commitTransaction();
                    } else {
                        realm.beginTransaction();
                        realm.copyToRealm(user);
                        realm.commitTransaction();
                    }

                    Constant.orderArray = null;
                    session.setLoginStatus(true);
                    setResultOfActivity(1);
                } else if (status == 2) {
                    goToActivityForResult(context, GeneratePinActivity.class, 4);
                } else if (status == 3) {
                    JSONObject result = jsonObject.getJSONObject("result");
                    User user = RealmController.with(context).getUser();
                    if (user != null) {
                        updateFlag = true;
                        realm.beginTransaction();
                    } else {
                        updateFlag = false;
                        user = new User();
                    }

                    user.setUserId(result.optString("userId"));
                    user.setName(result.optString("name"));
                    user.setEmail(result.optString("email"));
                    user.setContact(result.optString("contact"));
                    user.setPIN(result.optString("PIN"));
                    user.setOtp(result.optString("otp"));
                    user.setEmailVerified(result.optString("emailVerified"));
                    user.setOtpVerified(result.optString("otpVerified"));
                    user.setStatus(result.optString("status"));
                    user.setDateTimeAdded(result.optString("dateTimeAdded"));

                    if (updateFlag) {
                        realm.commitTransaction();
                    } else {
                        realm.beginTransaction();
                        realm.copyToRealm(user);
                        realm.commitTransaction();
                    }

                    goToActivityForResult(context, VerifyOTPActivity.class, 3);
                } else if (status == 5) {
                    showSnackBar(tvSignUp, jsonObject.optString("message"));
                    showToast(jsonObject.optString("message"));
                    goToActivityForResult(context, ForgotPinActivity.class, 6);
                } else {
                    etPin14.setError(jsonObject.optString("message"));
                    etPin14.requestFocus();
                }
                hideProgressDialog();
            } catch (JSONException e) {
                hideProgressDialog();
                e.printStackTrace();
            }
        }, error -> {
            hideProgressDialog();
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            Log.d(TAG, "Error: " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("contact", username);
                params.put("PIN", pin);
                params.put("firebaseId", firebaseId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Basic " + Base64.encodeToString(Constant.credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(1);
            }
        }
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(1);
            }
        }
        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(1);
            }
        }
        if (requestCode == 6) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(1);
            }
        }
    }
}
