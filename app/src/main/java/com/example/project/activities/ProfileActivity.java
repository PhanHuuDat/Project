package com.example.project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private Button btnLogout;
    private TextView tvEmail;
    private TextInputLayout edtEmail, edtNewMail, edtPwd, edtConfirm;
    private Button btnChange, btnSave;
    private LinearLayout grpInfo, grpEdit;
    private RelativeLayout btnHome;
    private RelativeLayout btnOrders;
    private RelativeLayout btnProfile;
    private ProgressBar pbLoad;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //init vars
        mapping();
        //init db
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // No user is signed in
            goToLogin();
        }
        getCurrentUser();
        pbLoad.setVisibility(View.GONE);
        setEvent();
    }

    private void setEvent() {
        //logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                goToLogin();
            }
        });
        //init edit profile
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grpInfo.setVisibility(View.GONE);
                grpEdit.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                btnChange.setVisibility(View.GONE);
            }
        });

        //change email
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getEditText().getText().toString();
                String pwd = edtPwd.getEditText().getText().toString();
                String rePwd = edtConfirm.getEditText().getText().toString();
                if (pwd.equals(rePwd)) {
                    changeEmail(email, pwd);
                } else {
                    Toast.makeText(ProfileActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //navigate to home
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //navigate to order list
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileActivity.this, OrdersActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //reload this
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    private void goToLogin() {
        Intent myIntent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void getCurrentUser() {
        tvEmail.setText(mAuth.getCurrentUser().getEmail());
    }

    private void changeEmail(String email, final String password) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider.getCredential(email, password); // Current Login Credentials

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //----------------Code for Changing Email Address----------\\
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateEmail(edtNewMail.getEditText().getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this, "Email Changed", Toast.LENGTH_LONG).show();
                                    btnProfile.performClick();
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Email or password incorrect", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

    private void mapping() {
        btnLogout = findViewById(R.id.btn_logout);
        btnChange = findViewById(R.id.btn_change);
        btnSave = findViewById(R.id.btn_save);
        tvEmail = findViewById(R.id.tv_email);
        edtEmail = findViewById(R.id.edt_email);
        edtNewMail = findViewById(R.id.edt_new_email);
        edtPwd = findViewById(R.id.edt_pwd);
        edtConfirm = findViewById(R.id.edt_confirm_pwd);
        btnHome = findViewById(R.id.btn_home);
        btnOrders = findViewById(R.id.btn_orders);
        btnProfile = findViewById(R.id.btn_profile);
        pbLoad = findViewById(R.id.pb_load);
        grpEdit = findViewById(R.id.grp_info_input);
        grpInfo = findViewById(R.id.grp_info_show);
    }
}