package com.example.expensemanager.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog=new ProgressDialog(this);
        dialog.setTitle("Login Account");
        dialog.setMessage("Please Wait.. ");

        auth=FirebaseAuth.getInstance();

        binding.dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                finish();
            }
        });


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=binding.signInEmail.getText().toString();
                String password=binding.signInPassword.getText().toString();
                if (email.isEmpty())
                {
                    binding.signInEmail.setError("Please enter your email");
                    binding.signInEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    binding.signInEmail.setError("Please enter valid email");
                    binding.signInEmail.requestFocus();
                }else if (password.isEmpty())
                {
                    binding.signInPassword.setError("Enter valid password");
                    binding.signInPassword.requestFocus();
                }else if (password.length()>6)
                {
                    binding.signInPassword.setError("Password lenght must be 6");
                    binding.signInPassword.requestFocus();
                }else {

                    dialog.show();

                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                startActivity(new Intent(SignInActivity.this,HomeActivity.class));
                                finish();
                                Toast.makeText(SignInActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}