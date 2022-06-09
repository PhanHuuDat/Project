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

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout edtUname, edtPwd, edtConfirmPwd;
    private Button btnReg, btnLogin;
    private LinearLayout lnReg;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mapping();

        // adding on click for login tv.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a login activity on clicking login text.
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        // adding click listener for register button.
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hiding our progress bar.
                loadingPB.setVisibility(View.VISIBLE);

                // getting data fro =m our edit text.
                String userName = edtUname.getEditText().getText().toString();
                String pwd = edtPwd.getEditText().getText().toString();
                String cnfPwd = edtConfirmPwd.getEditText().getText().toString();

                if (!pwd.equals(cnfPwd)) {
                    // checking if the password and confirm password is equal or not.
//                    Toast.makeText(RegisterActivity.this, "Please check both having same password..", Toast.LENGTH_SHORT).show();
                    Snackbar.make(lnReg, "Please check both having same password...", Snackbar.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cnfPwd)) {
                    // checking if the text fields are empty or not.
//                    Toast.makeText(RegisterActivity.this, "Please enter your credentials..", Toast.LENGTH_SHORT).show();
                    Snackbar.make(lnReg, "Please enter your credentials...", Snackbar.LENGTH_SHORT).show();
                } else {
                    // on below line we are creating a new user by passing email and password.
                    createUser(userName, pwd);
                }
            }
        });
    }

    private void createUser(String userName, String pwd) {
        mAuth.createUserWithEmailAndPassword(userName, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // on below line we are checking if the task is success or not.
                if (task.isSuccessful()) {
                    // in on success method we are hiding our progress bar and opening a login activity.
                    loadingPB.setVisibility(View.GONE);
//                    Toast.makeText(RegisterActivity.this, "User Registered..", Toast.LENGTH_SHORT).show();
                    Snackbar.make(lnReg, "User registered...", Snackbar.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                } else {
                    // in else condition we are displaying a failure toast message.
                    loadingPB.setVisibility(View.GONE);
//                    Toast.makeText(RegisterActivity.this, "Fail to register user..", Toast.LENGTH_SHORT).show();
                    Snackbar.make(lnReg, "Fail to register user...", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mapping() {
        // initializing all our variables.
        edtUname = findViewById(R.id.edt_uname);
        edtPwd = findViewById(R.id.edt_pwd);
        loadingPB = findViewById(R.id.pd_load);
        edtConfirmPwd = findViewById(R.id.edt_confirm_pwd);
        btnLogin = findViewById(R.id.btn_login);
        btnReg = findViewById(R.id.btn_reg);
        lnReg = findViewById(R.id.ln_reg);
        mAuth = FirebaseAuth.getInstance();
    }
}