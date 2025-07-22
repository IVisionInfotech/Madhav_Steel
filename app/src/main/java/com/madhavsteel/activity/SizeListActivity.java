package com.madhavsteel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.madhavsteel.R;
import com.madhavsteel.adapter.SizeListAdapter;
import com.madhavsteel.model.Category;
import com.madhavsteel.model.Product;
import com.madhavsteel.model.Sizes;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SizeListActivity extends BaseActivity {

    private static final String TAG = "SizeListActivity";
    private Context context;
    private ImageView ivBack;
    private TextView tvTitle, tvCalculatePrice, tvAddMoreItem;
    private RecyclerView recyclerView;
    private SizeListAdapter adapter;
    private LinearLayout llList, llNoList, llCart;
    private ArrayList<Sizes> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_list);

        context = SizeListActivity.this;

        adapter = new SizeListAdapter(context, list, new SizeListAdapter.AdapterListener() {
            @Override
            public void onItemSelected(Sizes model) {

            }
        });

        init();
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvCalculatePrice = findViewById(R.id.tvCalculatePrice);
        tvAddMoreItem = findViewById(R.id.tvAddMoreItem);
        tvCalculatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.sizeList == null) {
                    Toast.makeText(context, "Please select atleast one size", Toast.LENGTH_SHORT).show();
                } else {
                    if (Constant.sizeList.size() > 0) {
                        goToActivityForResult(context, SelectedSizeListActivity.class, 13);
                    } else {
                        Toast.makeText(context, "Please select atleast one size", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tvAddMoreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCancelResultOfActivity(12);
            }
        });

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llList = findViewById(R.id.llList);
        llNoList = findViewById(R.id.llNoList);
        llCart = findViewById(R.id.llCart);

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

        if (getIntent() != null) {
            if (getIntent().hasExtra("details")) {
                Category model = (Category) getIntent().getSerializableExtra("details");
                bindData(model);
                Constant.sizeStatus = 1;
            } else if (getIntent().hasExtra("productDetails")) {
                Product model = (Product) getIntent().getSerializableExtra("productDetails");
                bindData(model);
                Constant.sizeStatus = 2;
            }
        }

        if (Constant.selectionChange) {
            getCategorySize(Constant.catId);
            Constant.selectionChange = false;
        }
    }

    private void bindData(Category model) {

        tvTitle.setText(model.getCatTitle());

        getCategorySize(model.getCatId());

    }

    private void bindData(Product model) {

        tvTitle.setText(model.getProductTitle());

        getCategorySize(model.getProductId());

    }

    private void getCategorySize(final String catId) {
        Constant.catId = catId;
        list.clear();
        adapter.notifyDataSetChanged();
        /*Constant.selectedSizeList.clear();
        Constant.sizeList.clear();*/
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.getCategorySize, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONArray sizeArray = result.getJSONArray("sizeArray");
                        bindRecyclerView(sizeArray);
                    } else {
                        llList.setVisibility(View.GONE);
                        llNoList.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("catId", catId);
                params.put("sizeStatus", String.valueOf(Constant.sizeStatus));
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

    private void bindRecyclerView(JSONArray sizeArray) {
        list.clear();
        try {
            for (int i = 0; i < sizeArray.length(); i++) {
                JSONObject object = sizeArray.getJSONObject(i);
                Sizes model = new Sizes();
                model.setSizeId(object.optString("sizeId"));
                model.setProductId(object.optString("productId"));
                model.setProductTitle(object.optString("productTitle"));
                model.setCatId(object.optString("catId"));
                model.setCatTitle(object.optString("catTitle"));
                model.setSizeTitle(object.optString("sizeTitle"));
                model.setWidth(object.optString("weight"));
                model.setPrice(object.optString("price"));
                model.setPriceInKg(object.optString("priceInKg"));
                model.setPriceType(object.optString("priceType"));
                model.setDifferentPrice(object.optString("differentPrice"));
                model.setSizeImage(object.optString("sizeImage"));
                model.setSizeThumbImage(object.optString("sizeThumbImage"));
                model.setSizeMediumImage(object.optString("sizeMediumImage"));
                model.setHeight(object.optString("height"));
                model.setWidth(object.optString("width"));
                model.setRound(object.optString("round"));
                model.setSquare(object.optString("square"));
                model.setRectangle(object.optString("rectangle"));
                model.setHexagon(object.optString("hexagon"));
                model.setOctagon(object.optString("octagon"));
                model.setStatus(object.optString("status"));
                model.setDateTimeAdded(object.optString("dateTimeAdded"));
                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list != null) {
            if (list.size() > 0) {
                llList.setVisibility(View.VISIBLE);
                llNoList.setVisibility(View.GONE);
                llCart.setVisibility(View.VISIBLE);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 13) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(12);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setCancelResultOfActivity(12);
            }
        }
    }
}