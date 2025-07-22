package com.madhavsteel.viewHolder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madhavsteel.R;
import com.madhavsteel.model.Order;
import com.madhavsteel.model.Products;
import com.madhavsteel.utils.Constant;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class ProductViewHolder extends GroupViewHolder {

    private String TAG = "ProductViewHolder";
    private TextView tvTitle, tvSubTitle;
    private ImageView ivArrow;
    private LinearLayout llPrice;

    public ProductViewHolder(View itemView) {
        super(itemView);

        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvSubTitle = itemView.findViewById(R.id.tvSubTitle);

        ivArrow = itemView.findViewById(R.id.ivArrow);

        llPrice = itemView.findViewById(R.id.llPrice);
    }

    @Override
    public void expand() {
        ivArrow.setImageResource(R.drawable.ic_arrow_down);
        Log.e("Adapter", "expand");
    }

    @Override
    public void collapse() {
        ivArrow.setImageResource(R.drawable.ic_arrow_up);
        Log.e("Adapter", "collapse");
    }

    public void setGroupName(ExpandableGroup group, Products model) {
        tvTitle.setText(group.getTitle());
        group.describeContents();
        expand();
    }

    public void setGroupName(ExpandableGroup group, Order model) {
        tvTitle.setText(group.getTitle());
        tvSubTitle.setText(String.format("%,d", Integer.parseInt(String.format("%.0f", Float.parseFloat(model.getPrice())))));
        llPrice.setVisibility(View.VISIBLE);
        expand();
    }
}
