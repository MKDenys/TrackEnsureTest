package com.example.trackensuretest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trackensuretest.R;
import com.example.trackensuretest.models.Refuel;

import java.util.List;

public class RefuelListAdapter extends RecyclerView.Adapter<RefuelListAdapter.ViewHolder> {
    private List<Refuel> refuelList;
    private OnEditButtonClickListener onEditButtonClickListener;
    private OnDelButtonClickListener onDelButtonClickListener;

    public RefuelListAdapter(List<Refuel> refuelList,
                             OnEditButtonClickListener onEditButtonClickListener,
                             OnDelButtonClickListener onDelButtonClickListener) {
        this.refuelList = refuelList;
        this.onEditButtonClickListener = onEditButtonClickListener;
        this.onDelButtonClickListener = onDelButtonClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.refuel_list_item, parent, false);
        return new ViewHolder(v, onEditButtonClickListener, onDelButtonClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Refuel item = refuelList.get(position);

        holder.fuelSupplierName.setText(item.getFuelSupplierName());
        holder.fuelType.setText(item.getFuelType());
        holder.amount.setText(String.valueOf(item.getAmount()));
        holder.cost.setText(String.format("%.2f", item.getCost()));
    }

    @Override
    public int getItemCount() {
        return refuelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView fuelSupplierName;
        public TextView fuelType;
        public TextView amount;
        public TextView cost;
        public ImageButton edit;
        public ImageButton del;
        OnEditButtonClickListener editClickListener;
        OnDelButtonClickListener delClickListener;

        public ViewHolder(View itemView, OnEditButtonClickListener editClickListener,
                          OnDelButtonClickListener delClickListener) {
            super(itemView);
            fuelSupplierName = itemView.findViewById(R.id.fuel_supplier_name_textView);
            fuelType = itemView.findViewById(R.id.fuel_type_textView);
            amount = itemView.findViewById(R.id.amount_textView);
            cost = itemView.findViewById(R.id.cost_textView);
            edit = itemView.findViewById(R.id.editButton);
            del = itemView.findViewById(R.id.delButton);
            this.editClickListener = editClickListener;
            this.delClickListener = delClickListener;

            edit.setOnClickListener(this);
            del.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.editButton:
                    this.editClickListener.OnEditButtonClick(getAdapterPosition());
                    break;
                case R.id.delButton:
                    this.delClickListener.OnDelButtonClick(getAdapterPosition());
                    break;
            }
        }
    }

    public interface OnEditButtonClickListener{
        void OnEditButtonClick(int position);
    }

    public interface OnDelButtonClickListener{
        void OnDelButtonClick(int position);
    }
}
