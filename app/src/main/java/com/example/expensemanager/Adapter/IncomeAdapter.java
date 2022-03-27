package com.example.expensemanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.Model.Data;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.IncomeRecyclerDataBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.viewHolder> {

    Context context;
    ArrayList<Data> list;

    private EditText editAmount;
    private EditText editType;
    private EditText editNote;
    private Button update;
    private Button delete;

    private DatabaseReference mIncomeDatabase;
    private FirebaseAuth mAuth;

    public IncomeAdapter(Context context, ArrayList<Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.income_recycler_data,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {


       Data data=list.get(position);

        holder.binding.dateTextIncome.setText(data.getDate());
        holder.binding.noteTextIncome.setText(data.getNote());
        holder.binding.typeTextIncome.setText(data.getType());
       holder.binding.amountTextIncome.setText(data.getAmount()+"");


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               updateData(data);
           }
       });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        IncomeRecyclerDataBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding=IncomeRecyclerDataBinding.bind(itemView);
        }
    }
    public void updateData(Data data)
    {

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase=FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);


        AlertDialog.Builder mydialog=new AlertDialog.Builder(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View myView=inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myView);

        editAmount=myView.findViewById(R.id.update_amount_edit);
        editType=myView.findViewById(R.id.update_type_edit);
        editNote=myView.findViewById(R.id.update_note_edit);

        update=myView.findViewById(R.id.update_btn);
        delete=myView.findViewById(R.id.delete_btn);

        AlertDialog dialog=mydialog.create();
        dialog.show();
        dialog.setCancelable(false);

        editAmount.setText(data.getAmount()+"");
        editNote.setText(data.getNote());
        editType.setText(data.getType());


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String note=editNote.getText().toString();
                String type=editType.getText().toString();
                String mdamount=editAmount.getText().toString();
                int amount=Integer.parseInt(mdamount);
                 String id=data.getId();
                String date= DateFormat.getDateInstance().format(new Date());

                Data data1=new Data(amount,type,note,id,date);

                mIncomeDatabase.child(id).setValue(data1);
                dialog.dismiss();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
