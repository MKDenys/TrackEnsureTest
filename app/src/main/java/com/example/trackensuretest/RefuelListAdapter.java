package com.example.trackensuretest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class RefuelListAdapter extends RecyclerView.Adapter<RefuelListAdapter.ViewHolder> {
    private List<Refuel> refuelList;
    private Context context;

    public RefuelListAdapter(Context context, List<Refuel> refuelList) {
        this.context = context;
        this.refuelList = refuelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.refuel_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Refuel item = refuelList.get(position);

        holder.fuelSupplierName.setText(item.getFuelSupplierName());
        holder.fuelType.setText(item.getFuelType());
        holder.amount.setText(String.valueOf(item.getAmount()));
        holder.cost.setText(String.format("%.2f", item.getCost()));

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase appDatabase = App.getInstance().getAppDatabase();
                RefuelDao refuelDao = appDatabase.refuelDao();
                refuelDao.delete(item);
                refuelList.remove(item);
                notifyItemRemoved(position);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RefuelEditorActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("refuel", (Serializable) item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return refuelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView fuelSupplierName;
        public TextView fuelType;
        public TextView amount;
        public TextView cost;
        public ImageButton edit;
        public ImageButton del;

        public ViewHolder(View itemView) {
            super(itemView);
            fuelSupplierName = itemView.findViewById(R.id.fuel_supplier_name_textView);
            fuelType = itemView.findViewById(R.id.fuel_type_textView);
            amount = itemView.findViewById(R.id.amount_textView);
            cost = itemView.findViewById(R.id.cost_textView);
            edit = itemView.findViewById(R.id.editButton);
            del = itemView.findViewById(R.id.delButton);
        }
    }
}
