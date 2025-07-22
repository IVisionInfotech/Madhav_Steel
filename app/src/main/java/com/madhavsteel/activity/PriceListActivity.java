package com.madhavsteel.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.madhavsteel.R;
import com.madhavsteel.adapter.PriceListAdapter;
import com.madhavsteel.model.Order;
import com.madhavsteel.model.OrderInquiry;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.RealmController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PriceListActivity extends BaseActivity {

    private static final String TAG = "PriceListActivity";
    private Context context;
    private ImageView ivBack;
    private TextView tvTitle, tvCalculatePrice, tvCalculatedPrice, tvGSTPrice, tvTotal, tvOrder, tvInfo, tvProduct, tvProductPrice;
    private LinearLayout llPrice;
    private RecyclerView recyclerView;
    private PriceListAdapter adapter;
    private LinearLayout llList, llNoList;
    private ArrayList<Order> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private String userId = "", userName = "", userContact = "";
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_list);

        context = PriceListActivity.this;
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
            userName = user.getName();
            userContact = user.getContact();
        }

        init();

        bindData();
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvProduct = findViewById(R.id.tvProduct);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvCalculatePrice = findViewById(R.id.tvCalculatePrice);
        tvCalculatedPrice = findViewById(R.id.tvCalculatedPrice);
        tvGSTPrice = findViewById(R.id.tvGSTPrice);
        tvTotal = findViewById(R.id.tvTotal);
        tvOrder = findViewById(R.id.tvOrder);
        tvInfo = findViewById(R.id.tvInfo);
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = getResources().getString(R.string.contact);
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        });

        tvCalculatePrice.setVisibility(View.GONE);
        tvCalculatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.getLoginStatus()) {

                } else {
                    goToActivityForResult(context, PreLoginActivity.class, 24);
                }
            }
        });
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llPrice = findViewById(R.id.llPrice);
        llList = findViewById(R.id.llList);
        llNoList = findViewById(R.id.llNoList);

        recyclerView = findViewById(R.id.recyclerView);

        mLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
            userName = user.getName();
            userContact = user.getContact();
        }
    }

    private void bindData() {

        tvTitle.setText("Calculated Price");
        String title = "", price = "";

        if (Constant.priceResult != null) {
            try {
                JSONArray priceArray = Constant.priceResult.optJSONArray("priceArray");
                for (int i = 0; i < priceArray.length(); i++) {
                    JSONObject object = priceArray.getJSONObject(i);

                    JSONArray orderInquiryArray = object.optJSONArray("orderInquiryArray");
                    ArrayList<OrderInquiry> orderInquiries = new ArrayList<>();
                    for (int j = 0; j < orderInquiryArray.length(); j++) {
                        JSONObject jsonObject = orderInquiryArray.getJSONObject(j);
                        OrderInquiry orderInquiry = new OrderInquiry();
                        orderInquiry.setOrderInquiryId(jsonObject.optString("orderInquiryId"));
                        orderInquiry.setOrderId(jsonObject.optString("orderId"));
                        orderInquiry.setUserId(jsonObject.optString("userId"));
                        orderInquiry.setUserName(jsonObject.optString("userName"));
                        orderInquiry.setUserContact(jsonObject.optString("userContact"));
                        orderInquiry.setSizeId(jsonObject.optString("sizeId"));
                        orderInquiry.setSizeTitle(jsonObject.optString("sizeTitle"));
                        orderInquiry.setCatId(jsonObject.optString("catId"));
                        orderInquiry.setCatTitle(jsonObject.optString("catTitle"));
                        orderInquiry.setQuantity(jsonObject.optString("quantity"));
                        orderInquiry.setMeasurement(jsonObject.optString("measurement"));
                        orderInquiry.setProductId(jsonObject.optString("productId"));
                        orderInquiry.setPrice(jsonObject.optString("price"));
                        orderInquiry.setTonnePrice(jsonObject.optString("tonnePrice"));
                        orderInquiry.setKgPrice(jsonObject.optString("kgPrice"));
                        orderInquiry.setDifferentPrice(jsonObject.optString("differentPrice"));
                        orderInquiry.setOrderStatus(jsonObject.optString("orderStatus"));
                        orderInquiry.setStatus(jsonObject.optString("status"));
                        orderInquiry.setDateTimeAdded(jsonObject.optString("dateTimeAdded"));
                        orderInquiries.add(orderInquiry);
                        position++;
                    }

                    Order model = new Order(object.optString("productTitle"), orderInquiries);
                    model.setOrderId(object.optString("orderId"));
                    model.setUserId(object.optString("userId"));
                    model.setUserName(object.optString("userName"));
                    model.setUserContact(object.optString("userContact"));
                    model.setProductId(object.optString("productId"));
                    model.setProductTitle(object.optString("productTitle"));
                    model.setPrice(object.optString("price"));
                    model.setOrderStatus(object.optString("orderStatus"));
                    model.setStatus(object.optString("status"));
                    model.setDateTimeAdded(object.optString("dateTimeAdded"));
                    model.setInquiryList(orderInquiries);
                    title = title + object.optString("productTitle") + "\n";
                    price = price + String.format("%,d", Integer.parseInt(String.format("%.0f", Float.parseFloat(object.optString("price"))))) + "\n";
                    list.add(model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        tvProduct.setText(title);
        tvProductPrice.setText(price);
        tvCalculatedPrice.setText(String.format("%,d", Integer.parseInt(String.format("%.0f", Float.parseFloat(Constant.priceResult.optString("totalAmount"))))));
        tvGSTPrice.setText(String.format("%,d", Integer.parseInt(String.format("%.0f", Float.parseFloat(Constant.priceResult.optString("gstCharge"))))));
        tvTotal.setText(String.format("%,d", Integer.parseInt(String.format("%.0f", Float.parseFloat(Constant.priceResult.optString("grandTotal"))))));

        tvCalculatePrice.setVisibility(View.GONE);
        llPrice.setVisibility(View.VISIBLE);
        tvInfo.setVisibility(View.VISIBLE);

        adapter = new PriceListAdapter(context, list);

        if (list != null) {
            if (list.size() > 0) {
                llList.setVisibility(View.VISIBLE);
                llNoList.setVisibility(View.GONE);
            } else {
                llList.setVisibility(View.GONE);
                llNoList.setVisibility(View.VISIBLE);
            }
        } else {
            llList.setVisibility(View.GONE);
            llNoList.setVisibility(View.VISIBLE);
        }

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        for (int i = 0; i < position; i++) {
            expandGroup(i);
        }
    }

    private void expandGroup(int gPos) {
        if (adapter.isGroupExpanded(gPos)) {
            return;
        }
        adapter.toggleGroup(gPos);
    }

    private void orderInquiry(final Dialog myDialog, final String userName, final String userContact) {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.orderInquiry, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        Constant.orderArray = null;
                        Constant.selectedSizeList.clear();
                        Constant.sizeList.clear();
                        Constant.measurementList.clear();
                        Constant.quantityList.clear();
                        Constant.selectedSize = "";
                        Constant.selectedQuantity = "";
                        Constant.selectedMeasurement = "";
                        myDialog.dismiss();
                        setResultOfActivity(14);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
                hideProgressDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("userName", userName);
                params.put("userContact", userContact);
                params.put("sizeId", Constant.selectedSize);
                params.put("quantity", Constant.selectedQuantity);
                params.put("measurement", Constant.selectedMeasurement);
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

    private void showDialog() {
        final Dialog myDialog = new Dialog(context);
        myDialog.getWindow();
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCancelable(false);
        myDialog.setContentView(R.layout.popup_order);

        TextView tvCancel = myDialog.findViewById(R.id.tvCancel);
        TextView tvSubmit = myDialog.findViewById(R.id.tvSubmit);
        final EditText etName = myDialog.findViewById(R.id.etName);
        final EditText etContact = myDialog.findViewById(R.id.etContact);

        etName.setText(userName);
        etContact.setText(userContact);
        etName.setSelection(etName.getText().toString().length());
        etContact.setSelection(etContact.getText().toString().length());

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = etName.getText().toString();
                userContact = etContact.getText().toString();

                if (userName.equals("") || userName.isEmpty()) {
                    etName.setError("Enter name");
                    etName.requestFocus();
                } else if (userContact.equals("") || userContact.isEmpty()) {
                    etContact.setError("Enter contact");
                    etContact.requestFocus();
                } else {
                    orderInquiry(myDialog, userName, userContact);
                }
            }
        });
        myDialog.show();
    }
}
