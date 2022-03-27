package com.example.expensemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.Model.Data;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.DashboardIncomeBinding;

import java.util.ArrayList;

public class DashBoardInComeAdapter extends RecyclerView.Adapter<DashBoardInComeAdapter.viewHolder>{

    Context context;
    ArrayList<Data> dataArrayList;

    public DashBoardInComeAdapter(Context context, ArrayList<Data> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.dashboard_income,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Data data=dataArrayList.get(position);

        holder.binding.typeIncomeDs.setText(data.getType());
        holder.binding.dateIncomeDs.setText(data.getDate());
        holder.binding.amountIncomeDs.setText(data.getAmount()+"");
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        DashboardIncomeBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=DashboardIncomeBinding.bind(itemView);
        }
    }
}
