package com.madhavsteel.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.madhavsteel.adapter.ProductsSizeListAdapter;
import com.madhavsteel.model.EditModel;
import com.madhavsteel.model.Products;
import com.madhavsteel.model.Sizes;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.RealmController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SelectedSizeListActivity extends BaseActivity {

    private static final String TAG = "SelectedSizeList";
    private Context context;
    private ImageView ivBack, ivDelete;
    private TextView tvTitle, tvCalculatePrice, tvAddMoreItem, tvOrder, tvInfo;
    private LinearLayout llList, llNoList, llPrice, llCart;
    private RecyclerView recyclerView;
    private ProductsSizeListAdapter adapter;
    private ArrayList<Products> list = new ArrayList<>();
    public ArrayList<EditModel> editModelArrayList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private String userId = "", userName = "", userContact = "";
    private boolean flagCalculate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_list);

        context = SelectedSizeListActivity.this;
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
            userName = user.getName();
            userContact = user.getContact();
        }

        init();
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvCalculatePrice = findViewById(R.id.tvCalculatePrice);
        tvAddMoreItem = findViewById(R.id.tvAddMoreItem);
        tvOrder = findViewById(R.id.tvOrder);
        tvInfo = findViewById(R.id.tvInfo);

        tvAddMoreItem.setVisibility(View.GONE);
        tvCalculatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.getLoginStatus()) {
                    if (validate()) {
                        calculatePrice();
                    }
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
        ivDelete = findViewById(R.id.ivDelete);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivDelete.setVisibility(View.VISIBLE);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.selectedSizeList.clear();
                Constant.sizeList.clear();
                Constant.measurementList.clear();
                Constant.quantityList.clear();
                Constant.selectedSize = "";
                Constant.selectedQuantity = "";
                Constant.selectedMeasurement = "";
                setCancelResultOfActivity(13);
            }
        });

        llPrice = findViewById(R.id.llPrice);
        llList = findViewById(R.id.llList);
        llNoList = findViewById(R.id.llNoList);
        llCart = findViewById(R.id.llCart);
        llCart.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
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

        bindList();
    }

    public void expandGroup(int gPos) {
        if (adapter.isGroupExpanded(gPos)) {
            return;
        }
        adapter.toggleGroup(gPos);
    }

    private void bindList() {
        tvTitle.setText("Calculate Price");

        list.clear();
        editModelArrayList.clear();

        Collections.sort(Constant.sizeList, new ProductSorting());
        String productId = "";
        int h = 0, position = 0;
        for (int i = 0; i < Constant.sizeList.size(); i = i + h) {
            h = 0;
            productId = Constant.sizeList.get(i).getProductId();
            ArrayList<Sizes> sizes = new ArrayList<>();
            for (int j = i; j < Constant.sizeList.size(); j++) {
                if (productId.equals(Constant.sizeList.get(j).getProductId())) {
                    Sizes model = new Sizes();
                    model.setPosition(String.valueOf(position));
                    model.setSizeId(Constant.sizeList.get(j).getSizeId());
                    model.setProductId(Constant.sizeList.get(j).getProductId());
                    model.setProductTitle(Constant.sizeList.get(j).getProductTitle());
                    model.setCatId(Constant.sizeList.get(j).getCatId());
                    model.setCatTitle(Constant.sizeList.get(j).getCatTitle());
                    model.setSizeTitle(Constant.sizeList.get(j).getSizeTitle());
                    model.setWidth(Constant.sizeList.get(j).getWidth());
                    model.setPrice(Constant.sizeList.get(j).getPrice());
                    model.setPriceInKg(Constant.sizeList.get(j).getPriceInKg());
                    model.setPriceType(Constant.sizeList.get(j).getPriceType());
                    model.setDifferentPrice(Constant.sizeList.get(j).getDifferentPrice());
                    model.setSizeImage(Constant.sizeList.get(j).getSizeImage());
                    model.setSizeThumbImage(Constant.sizeList.get(j).getSizeThumbImage());
                    model.setSizeMediumImage(Constant.sizeList.get(j).getSizeMediumImage());
                    model.setHeight(Constant.sizeList.get(j).getHeight());
                    model.setWidth(Constant.sizeList.get(j).getWidth());
                    model.setRound(Constant.sizeList.get(j).getRound());
                    model.setSquare(Constant.sizeList.get(j).getSquare());
                    model.setRectangle(Constant.sizeList.get(j).getRectangle());
                    model.setHexagon(Constant.sizeList.get(j).getHexagon());
                    model.setOctagon(Constant.sizeList.get(j).getOctagon());
                    model.setStatus(Constant.sizeList.get(j).getStatus());
                    model.setDateTimeAdded(Constant.sizeList.get(j).getDateTimeAdded());
                    sizes.add(model);

                    position++;
                    h++;
                }
            }
            Products product = new Products(Constant.sizeList.get(i).getProductTitle(), sizes);
            product.setProductId(productId);
            product.setSizeList(sizes);
            list.add(product);
        }

        editModelArrayList = populateList();


        bindData();
    }

    private void bindData() {

        adapter = new ProductsSizeListAdapter(context, list, editModelArrayList, new ProductsSizeListAdapter.AdapterListener() {
            @Override
            public void onItemSelected(Sizes model) {
                Constant.selectionChange = true;
                Constant.selectedSizeList.remove(model.getSizeId());
                for (int i = 0; i < Constant.sizeList.size(); i++) {
                    if (model.getSizeId().equals(Constant.sizeList.get(i).getSizeId())) {
                        Constant.sizeList.remove(i);
                        break;
                    }
                }
                list.remove(model);
                adapter.notifyDataSetChanged();
                bindList();
                tvCalculatePrice.setVisibility(View.VISIBLE);
                llPrice.setVisibility(View.GONE);
                tvInfo.setVisibility(View.GONE);
                Log.e(TAG, "onItemSelected: delete" );
            }
        }, new ProductsSizeListAdapter.AdapterListener() {
            @Override
            public void onItemSelected(Sizes model) {
                tvCalculatePrice.setVisibility(View.VISIBLE);
                llPrice.setVisibility(View.GONE);
                tvInfo.setVisibility(View.GONE);
            }
        });

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

        for (int i = 0; i < editModelArrayList.size(); i++) {
            expandGroup(i);
        }
    }

    private boolean validate() {

        Constant.quantityList.clear();
        Constant.measurementList.clear();
        Constant.selectedSize = "";
        Constant.selectedQuantity = "";
        Constant.selectedMeasurement = "";

        for (int i = 0; i < ProductsSizeListAdapter.editModelArrayList.size(); i++) {

            Constant.quantityList.add(ProductsSizeListAdapter.editModelArrayList.get(i).getEditTextValue());
            Constant.measurementList.add(ProductsSizeListAdapter.editModelArrayList.get(i).getSpinnerValue());

            Constant.selectedSize = Constant.selectedSize + ProductsSizeListAdapter.editModelArrayList.get(i).getSizeId() + ", ";
            Constant.selectedQuantity = Constant.selectedQuantity + ProductsSizeListAdapter.editModelArrayList.get(i).getEditTextValue() + ", ";
            Constant.selectedMeasurement = Constant.selectedMeasurement + ProductsSizeListAdapter.editModelArrayList.get(i).getSpinnerValue() + ", ";
        }

        String str3 = Constant.selectedSize.trim();
        if (!str3.isEmpty()) str3 = str3.substring(0, str3.length() - 1);
        Constant.selectedSize = str3;

        String str2 = Constant.selectedQuantity.trim();
        if (!str2.isEmpty()) str2 = str2.substring(0, str2.length() - 1);
        Constant.selectedQuantity = str2;

        String str = Constant.selectedMeasurement.trim();
        if (!str.isEmpty()) str = str.substring(0, str.length() - 1);
        Constant.selectedMeasurement = str;

        Log.e(TAG, "validate: " + Constant.selectedSize);
        Log.e(TAG, "validate: " + Constant.selectedQuantity);
        Log.e(TAG, "validate: " + Constant.selectedMeasurement);

        return true;
    }

    private void calculatePrice() {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.calculatePrice, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        Constant.priceResult = jsonObject.optJSONObject("result");
                        goToActivityForResult(context, PriceListActivity.class, 14);
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
                params.put("sizeId", Constant.selectedSize);
                params.put("quantity", Constant.selectedQuantity);
                params.put("measurement", Constant.selectedMeasurement);
                Log.e(TAG, "getParams: " + params.toString());
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

    private void setCalculatedPrice(JSONObject jsonObject) {
        /*float price = 0, gstPrice = 0, total = 0;
        for (int i = 0; i < Constant.selectedSizeList.size(); i++) {
            float amount = 0;
            if (Constant.measurementList.get(i).toLowerCase().equals("kilogram")) {
                amount = Float.parseFloat(Constant.sizeList.get(i).getPriceInKg());
            } else if (Constant.measurementList.get(i).toLowerCase().equals("tonne")) {
                amount = Float.parseFloat(Constant.sizeList.get(i).getPrice());
            }
            try {
                Log.e(TAG, "calculatePrice: " + price + " + " + Float.parseFloat(Constant.quantityList.get(i)) + " * " + amount);
                price = price + (Float.parseFloat(Constant.quantityList.get(i)) * amount);
                tvCalculatedPrice.setText("Calculated Price : " + price + "\\n GST Price : " + gstPrice + " \\n Total Amount : " + total);
            } catch (Exception e) {
                tvCalculatedPrice.setText("Something went wrong");
                e.printStackTrace();
            }
        }*/

        Constant.priceResult = jsonObject.optJSONObject("result");

        /*tvCalculatedPrice.setText(result.optString("totalAmount"));
        tvGSTPrice.setText(result.optString("gstCharge"));
        tvTotal.setText(result.optString("grandTotal"));

        tvCalculatePrice.setVisibility(View.GONE);
        llPrice.setVisibility(View.VISIBLE);
        tvInfo.setVisibility(View.VISIBLE);*/
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
                        myDialog.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 24) {
            if (resultCode == Activity.RESULT_OK) {
                if (validate()) {
                    calculatePrice();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
        if (requestCode == 14) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(13);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private ArrayList<EditModel> populateList() {

        ArrayList<EditModel> list = new ArrayList<>();

        for (int i = 0; i < this.list.size(); i++) {
            for (int j = 0; j < this.list.get(i).getSizeList().size(); j++) {
                EditModel editModel = new EditModel();
                editModel.setSizeId(this.list.get(i).getSizeList().get(j).getSizeId());
                editModel.setEditTextValue(String.valueOf(1));
                if (this.list.get(i).getSizeList().get(j).getPriceType().equals("1")) {
                    editModel.setSpinnerValue("Piece");
                } else {
                    editModel.setSpinnerValue("Tonne");
                }
                list.add(editModel);
            }
        }

        return list;
    }

    class ProductSorting implements Comparator<Sizes> {

        @Override
        public int compare(Sizes model1, Sizes model2) {
            if ((Integer.parseInt(model1.getProductId())) < (Integer.parseInt(model2.getProductId()))) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
