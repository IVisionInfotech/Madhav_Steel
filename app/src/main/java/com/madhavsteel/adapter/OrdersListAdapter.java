package com.madhavsteel.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madhavsteel.R;
import com.madhavsteel.model.Order;
import com.madhavsteel.model.OrderInquiry;
import com.madhavsteel.viewHolder.ProductViewHolder;
import com.madhavsteel.viewHolder.SizePriceViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class OrdersListAdapter extends ExpandableRecyclerViewAdapter<ProductViewHolder, SizePriceViewHolder> {

    private String TAG = "PriceListAdapter";
    private Context context;

    public interface AdapterListener {
        void onItemSelected(OrderInquiry model);
    }

    public OrdersListAdapter(Context context, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.expandable_recyclerview_product_list_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public SizePriceViewHolder onCreateChildViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyclerview_price_list_item, parent, false);

        return new SizePriceViewHolder(view, context);
    }

    @Override
    public void onBindChildViewHolder(SizePriceViewHolder holder, final int flatPosition, ExpandableGroup group, int childIndex) {
        final OrderInquiry model = ((Order) group).getItems().get(childIndex);
        final SizePriceViewHolder mainHolder = (SizePriceViewHolder) holder;

        mainHolder.tvTitle.setText(model.getSizeTitle());
        mainHolder.tvSize.setText(model.getQuantity() + " " + model.getMeasurement());
        mainHolder.tvPrice.setVisibility(View.GONE);
        mainHolder.tvMultiply.setVisibility(View.GONE);
        mainHolder.ivRupee.setVisibility(View.GONE);
        mainHolder.tvTotalPrice.setText(String.format("%,d", Integer.parseInt(String.format("%.0f", Float.parseFloat(model.getPrice())))));
    }

    @Override
    public void onBindGroupViewHolder(ProductViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Order model = ((Order) group);
        holder.setGroupName(group, model);
    }
}