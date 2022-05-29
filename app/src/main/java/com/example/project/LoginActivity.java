package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;


public class LoginActivity  extends Activity {
    EditText username, password;
    Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String p = password.getText().toString();
                if(user.equals("customer")&&p.equals("customer"));{
                    Intent myIntent = new Intent(LoginActivity.this,MainActivity.class);
                    myIntent.putExtra("isLogin",true);
                    startActivity(myIntent);
                }
            }
        });
    }

    private void mapping(){
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        btn_login = findViewById(R.id.btn_login);
    }
}
