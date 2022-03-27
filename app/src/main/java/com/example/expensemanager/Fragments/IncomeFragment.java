package com.example.expensemanager.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expensemanager.Adapter.IncomeAdapter;
import com.example.expensemanager.Model.Data;
import com.example.expensemanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class IncomeFragment extends Fragment {

   private TextView incomeTotalValue;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private ArrayList<Data> list;



    public IncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_income, container, false);

        list=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        incomeTotalValue =view.findViewById(R.id.income_text_result);
        recyclerView=view.findViewById(R.id.income_recyclerview);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        IncomeAdapter adapter=new IncomeAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                int totalValue=0;
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Data data=snapshot1.getValue(Data.class);

                    totalValue+=data.getAmount();
                    String stTotalValue=String.valueOf(totalValue);
                    incomeTotalValue.setText(stTotalValue+".00");
                    list.add(data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }


}