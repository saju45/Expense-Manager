package com.example.expensemanager.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expensemanager.Adapter.ExpenseAdpter;
import com.example.expensemanager.Model.Data;
import com.example.expensemanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExpenseFragment extends Fragment {

    RecyclerView recyclerView;
    TextView expenseTotalValue;
    private DatabaseReference mExpenseDatabase;
    private FirebaseAuth mAuth;
    ExpenseAdpter adapter;
    ArrayList<Data> list;

    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_expense, container, false);

        list=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        expenseTotalValue=view.findViewById(R.id.expense_text_result);
        recyclerView=view.findViewById(R.id.expense_recyclerview);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

       adapter =new ExpenseAdpter(getContext(),list);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                int totalExpenseAmount=0;

                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Data data=snapshot1.getValue(Data.class);

                    totalExpenseAmount+=data.getAmount();
                    String stTotalAmount=String.valueOf(totalExpenseAmount);
                    expenseTotalValue.setText(stTotalAmount+".00");
                    list.add(data);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}