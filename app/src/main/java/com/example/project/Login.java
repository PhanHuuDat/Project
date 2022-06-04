package com.example.project;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    private TextInputLayout edt_UserName, edt_Pass;
    private MaterialButton btn_SignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        anhxa();

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doValidate()) {
                    Intent intent = new Intent(Login.this, DashboardScreen.class);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    public boolean doValidate() {
        String user = edt_UserName.getEditText().getText().toString();
        String pass = edt_Pass.getEditText().getText().toString();

        if (user.isEmpty()) {
            edt_UserName.setError("Username cannot be empty!");
            return false;
        } else if (pass.isEmpty()) {
            edt_Pass.setError("Password cannot be empty!");
            return false;
        } else {
            edt_UserName.setError(null);
            edt_UserName.setErrorEnabled(false);

            edt_Pass.setError(null);
            edt_Pass.setErrorEnabled(false);
            return true;
        }

    }

    public void anhxa() {
        edt_UserName = (TextInputLayout) findViewById(R.id.edt_username);
        edt_Pass = (TextInputLayout) findViewById(R.id.edt_password);
        btn_SignIn = (MaterialButton) findViewById(R.id.btn_sign_in);
    }
}