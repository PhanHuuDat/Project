package com.example.project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout edt_uname, edt_pwd;
    private Button btn_login, btn_reg;
    private LinearLayout lnHome;
    private FirebaseAuth mAuth;
    private ProgressBar pbLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mapping();

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line opening a login activity.
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hiding our progress bar.
                pbLoad.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.GONE);
                // getting data from our edit text on below line.
                String email = edt_uname.getEditText().getText().toString();
                String password = edt_pwd.getEditText().getText().toString();
//                 on below line validating the text input.
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
//                    Toast.makeText(LoginActivity.this, "Please enter your email and password..", Toast.LENGTH_SHORT).show();
                    Snackbar.make(lnHome, "Please enter your email and password...", Snackbar.LENGTH_SHORT).show();
                    pbLoad.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    return;
                }
//                 on below line we are calling a sign in method and passing email and password to it.
                signIn(email, password);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // in on start method checking if
        // the user is already sign in.
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // if the user is not null then we are
            // opening a main activity on below line.
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            this.finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }

    private void signIn(String email, String pwd) {
        mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // on below line we are checking if the task is success or not.
                if (task.isSuccessful()) {
                    // on below line we are hiding our progress bar.
                    pbLoad.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
//                    Toast.makeText(LoginActivity.this, "Login Successful..", Toast.LENGTH_SHORT).show();
                    Snackbar.make(lnHome, "Login successful...", Snackbar.LENGTH_SHORT).show();
                    // on below line we are opening our mainactivity.
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                } else {
                    // hiding our progress bar and displaying a toast message.
                    pbLoad.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
//                    Toast.makeText(LoginActivity.this, "Please enter valid user credentials..", Toast.LENGTH_SHORT).show();
                    Snackbar.make(lnHome, "Please enter valid user credentials...", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void mapping() {
        edt_uname = findViewById(R.id.edt_uname);
        edt_pwd = findViewById(R.id.edt_pwd);
        btn_login = findViewById(R.id.btn_login);
        btn_reg = findViewById(R.id.btn_reg);
        mAuth = FirebaseAuth.getInstance();
        pbLoad = findViewById(R.id.pd_load);
        lnHome = findViewById(R.id.ln_home);
    }
}