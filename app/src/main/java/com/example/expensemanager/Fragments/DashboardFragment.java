package com.example.expensemanager.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanager.Adapter.DashBoardExpenseAdapter;
import com.example.expensemanager.Adapter.DashBoardInComeAdapter;
import com.example.expensemanager.Adapter.IncomeAdapter;
import com.example.expensemanager.Model.Data;
import com.example.expensemanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DashboardFragment extends Fragment {

    private TextView totalIncomeResult;
    private TextView totalExpenseResult;

    private FloatingActionButton fb_main;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    private TextView fab_income_text;
    private TextView fab_expense_text;

    private boolean isOpen = false;
    private Animation fadOpen, fabClose;

    private FirebaseAuth mauth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;

    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;

    private ArrayList<Data> list;
    private ArrayList<Data> dataArrayList;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        list=new ArrayList<>();
        dataArrayList=new ArrayList<>();
        totalIncomeResult = view.findViewById(R.id.incomee_set_result);
        totalExpenseResult = view.findViewById(R.id.expense_set_result);


        mauth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mauth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        mRecyclerIncome = view.findViewById(R.id.recycler_income);
        mRecyclerExpense = view.findViewById(R.id.recycler_expense);


        fb_main = view.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = view.findViewById(R.id.income_ft_btn);
        fab_expense_btn = view.findViewById(R.id.expense_ft_btn);

        fab_income_text = view.findViewById(R.id.income_ft_text);
        fab_expense_text = view.findViewById(R.id.expense_ft_text);

        fadOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        fabClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fb_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();
                if (isOpen) {
                    fab_income_btn.startAnimation(fabClose);
                    fab_expense_btn.startAnimation(fabClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_text.startAnimation(fabClose);
                    fab_expense_text.startAnimation(fabClose);
                    isOpen = false;

                } else {

                    fab_income_btn.startAnimation(fadOpen);
                    fab_expense_btn.startAnimation(fadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_text.startAnimation(fadOpen);
                    fab_expense_text.startAnimation(fadOpen);
                    fab_income_text.setClickable(true);
                    fab_expense_text.setClickable(true);
                    isOpen = true;

                }

            }
        });

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalIncomeAmount = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Data data = snapshot1.getValue(Data.class);
                    totalIncomeAmount += data.getAmount();
                    totalIncomeResult.setText(totalIncomeAmount + ".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalIncomeAmount = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Data data = snapshot1.getValue(Data.class);
                    totalIncomeAmount += data.getAmount();
                    totalExpenseResult.setText(totalIncomeAmount + ".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dashBoardIncomeData();
        dashBoardExpenseData();





        return view;
    }

    public void closeDialog() {
        if (isOpen) {
            fab_income_btn.startAnimation(fabClose);
            fab_expense_btn.startAnimation(fabClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_text.startAnimation(fabClose);
            fab_expense_text.startAnimation(fabClose);
            isOpen = false;

        } else {

            fab_income_btn.startAnimation(fadOpen);
            fab_expense_btn.startAnimation(fadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_text.startAnimation(fadOpen);
            fab_expense_text.startAnimation(fadOpen);
            fab_income_text.setClickable(true);
            fab_expense_text.setClickable(true);
            isOpen = true;

        }

    }

    public void addData() {

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incomeDataInsert();
            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseDataInsert();
            }
        });

    }

    public void incomeDataInsert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.custom_layout_for_insert_data, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.setCancelable(false);

        EditText edit_amount = view.findViewById(R.id.amount_edit);
        EditText edit_type = view.findViewById(R.id.type_edit);
        EditText edit_note = view.findViewById(R.id.note_edit);

        Button save_btn = view.findViewById(R.id.save_btn);
        Button cancel_btn = view.findViewById(R.id.cancel_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = edit_type.getText().toString().trim();
                String amount = edit_amount.getText().toString().trim();
                String note = edit_note.getText().toString().toString();

                if (TextUtils.isEmpty(type)) {
                    edit_type.setError("Require field..");
                    return;

                }

                if (TextUtils.isEmpty(amount)) {
                    edit_amount.setError("Require field..");
                    return;

                }

                int ouramountint = Integer.parseInt(amount);

                if (TextUtils.isEmpty(note)) {
                    edit_note.setError("Require field..");
                    return;

                }

                String id = mIncomeDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(ouramountint, type, note, id, mDate);

                mIncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                closeDialog();


            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDialog();
                dialog.dismiss();
            }
        });
    }

    public void expenseDataInsert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.custom_layout_for_insert_data, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.setCancelable(false);

        EditText amount = view.findViewById(R.id.amount_edit);
        EditText type = view.findViewById(R.id.type_edit);
        EditText note = view.findViewById(R.id.note_edit);

        Button save = view.findViewById(R.id.save_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type_ = type.getText().toString().trim();
                String amount_ = amount.getText().toString().trim();
                String note_ = note.getText().toString().toString();


                if (TextUtils.isEmpty(type_)) {
                    type.setError("Require field..");
                    return;

                }
                int ouramountint = Integer.parseInt(amount_);

                if (TextUtils.isEmpty(amount_)) {
                    amount.setError("Require field..");
                    return;

                }


                if (TextUtils.isEmpty(note_)) {
                    note.setError("Require field..");
                    return;

                }


                String id = mIncomeDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(ouramountint, type_, note_, id, date);
                Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_SHORT).show();

                mExpenseDatabase.child(id).setValue(data);

                dialog.dismiss();
                closeDialog();

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
                dialog.dismiss();
            }
        });

    }

    public void dashBoardIncomeData(){

        LinearLayoutManager layoutManagerIncome=new LinearLayoutManager(getActivity());
        layoutManagerIncome.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManagerIncome.setReverseLayout(true);
        layoutManagerIncome.setStackFromEnd(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        DashBoardInComeAdapter adapter=new DashBoardInComeAdapter(getActivity(),list);
        mRecyclerIncome.setAdapter(adapter);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Data data=snapshot1.getValue(Data.class);
                    list.add(data);
                }adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void dashBoardExpenseData()
    {
        LinearLayoutManager layoutManagerExpense=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setReverseLayout(true);
        layoutManagerExpense.setStackFromEnd(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);

        DashBoardExpenseAdapter adapter=new DashBoardExpenseAdapter(getActivity(),dataArrayList);
        mRecyclerExpense.setAdapter(adapter);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataArrayList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Data data=snapshot1.getValue(Data.class);
                    dataArrayList.add(data);
                }adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}