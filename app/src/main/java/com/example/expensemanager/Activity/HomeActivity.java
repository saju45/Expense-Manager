package com.example.expensemanager.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.expensemanager.Fragments.DashboardFragment;
import com.example.expensemanager.Fragments.ExpenseFragment;
import com.example.expensemanager.Fragments.IncomeFragment;
import com.example.expensemanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private DashboardFragment dashboardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth=FirebaseAuth.getInstance();
         bottomNavigationView=findViewById(R.id.bottomBar);
         frameLayout=findViewById(R.id.frame_layout);

         dashboardFragment=new DashboardFragment();
         incomeFragment=new IncomeFragment();
         expenseFragment=new ExpenseFragment();


         
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Expense Manager");
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setFragment(dashboardFragment);

        NavigationView navigationView=findViewById(R.id.naview);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.dashboard:
                        setFragment(dashboardFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                        return true;

                    case R.id.income:
                        setFragment(incomeFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                        return true;
                    case R.id.expense:
                        setFragment(expenseFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                        return true;

                    default:

                        return false;

                }
            }
        });
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.END))
        {
            drawerLayout.closeDrawer(GravityCompat.END);
        }else {
            super.onBackPressed();
        }

    }
    public void displaySelectedListener(int itemId)
    {

        Fragment fragment=null;

        switch (itemId)
        {
            case R.id.nav_dashboard:

                break;
            case R.id.nav_income:

                break;
            case R.id.nav_expense:

                break;
            case R.id.nav_logout:
                auth.signOut();
                startActivity(new Intent(HomeActivity.this,SignInActivity.class));
                break;

        }
        if (fragment!=null)
        {
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,fragment);
            transaction.commit();
        }

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        displaySelectedListener(item.getItemId());

        return true;
    }
}