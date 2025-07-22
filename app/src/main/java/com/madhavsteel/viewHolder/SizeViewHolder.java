package com.madhavsteel.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.madhavsteel.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class SizeViewHolder extends ChildViewHolder {

    private Context context;
    public TextView tvTitle, tvSize;
    public ImageView ivDelete;
    public Spinner spnMeasurement;
    public EditText etQuantity;

    public SizeViewHolder(View view, Context context) {
        super(view);

        this.context = context;

        this.tvTitle = view.findViewById(R.id.tvTitle);
        this.tvSize = view.findViewById(R.id.tvSize);

        this.ivDelete = view.findViewById(R.id.ivDelete);

        this.spnMeasurement = view.findViewById(R.id.spnMeasurement);

        this.etQuantity = view.findViewById(R.id.etQuantity);
    }
}
