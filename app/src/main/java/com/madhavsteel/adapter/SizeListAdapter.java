package com.madhavsteel.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import com.madhavsteel.R;
import com.madhavsteel.model.Sizes;
import com.madhavsteel.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class SizeListAdapter extends RecyclerView.Adapter implements Filterable {
    private AdapterListener listener;
    private Context context;
    private List<Sizes> list;
    private List<Sizes> listFilter;

    public SizeListAdapter(Context context, List<Sizes> list, AdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listFilter = list;
        this.listener = listener;
    }

    @Override
    public CategoryView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.recyclerview_size_list_item, parent, false);
        final CategoryView listHolder = new CategoryView(mainGroup);
        mainGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(listFilter.get(listHolder.getAdapterPosition()));
            }
        });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Sizes model = listFilter.get(position);
        final CategoryView mainHolder = (CategoryView) holder;

        mainHolder.cbTitle.setText(model.getSizeTitle());

        if (Constant.selectedSizeList.contains(model.getSizeId())) {
            mainHolder.cbTitle.setChecked(true);
        }

        Log.e("hh", "onBindViewHolder: " + Constant.selectedSizeList + " : " + model.getSizeId());

        mainHolder.cbTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!Constant.selectedSizeList.contains(model.getSizeId())) {
                        Constant.selectedSizeList.add(model.getSizeId());
                        Constant.sizeList.add(model);
                    }
                } else {
                    if (Constant.selectedSizeList.contains(model.getSizeId())) {
                        Constant.selectedSizeList.remove(model.getSizeId());
                        for (int i = 0; i < Constant.sizeList.size(); i++) {
                            if (model.getSizeId().equals(Constant.sizeList.get(i).getSizeId())) {
                                Constant.sizeList.remove(i);
                                break;
                            }
                        }
                    }
                }
                Log.e("OnCheckedChange", Constant.selectedSizeList + "");
                Log.e("OnCheckedChange", Constant.sizeList.size() + "");
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFilter = list;
                } else {
                    List<Sizes> filteredList = new ArrayList<>();
                    for (Sizes row : list) {
                        if (row.getSizeTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    listFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFilter = (ArrayList<Sizes>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AdapterListener {
        void onItemSelected(Sizes model);
    }

    public class CategoryView extends RecyclerView.ViewHolder {
        public CheckBox cbTitle;

        CategoryView(View view) {
            super(view);
            this.cbTitle = view.findViewById(R.id.cbTitle);
        }
    }

}