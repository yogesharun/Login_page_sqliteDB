package com.example.sqldb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText mTextUsername;
    EditText mTextPassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    DBhandler dBhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextUsername = (EditText)findViewById(R.id.text_username);
        mTextPassword = (EditText)findViewById(R.id.text_password);
        mButtonLogin = (Button)findViewById(R.id.loginBTN);
        mTextViewRegister = (TextView) findViewById(R.id.text_register);
        dBhandler=new DBhandler(MainActivity.this);
        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTextUsername.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter username..", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mTextPassword.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter password..", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(dBhandler.check(mTextUsername.getText().toString(),mTextPassword.getText().toString()))
                {
                    Intent i = new Intent(MainActivity.this,ShowData.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email",mTextUsername.getText().toString());
                    i.putExtras(bundle);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Email OR Password wrong..", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }
}