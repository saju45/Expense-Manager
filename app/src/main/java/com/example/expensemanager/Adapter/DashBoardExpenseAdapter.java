package com.example.expensemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.Model.Data;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.DashboardExpenseBinding;

import java.util.ArrayList;

public class DashBoardExpenseAdapter extends RecyclerView.Adapter<DashBoardExpenseAdapter.viewHolder> {

    Context context;
    ArrayList<Data> list;

    public DashBoardExpenseAdapter(Context context, ArrayList<Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.dashboard_expense,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Data data=list.get(position);

        holder.binding.amountExpenseDs.setText(data.getAmount()+"");
        holder.binding.dateExpenseDs.setText(data.getDate());
        holder.binding.typeExpenseDs.setText(data.getType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        DashboardExpenseBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding=DashboardExpenseBinding.bind(itemView);
        }
    }
}
