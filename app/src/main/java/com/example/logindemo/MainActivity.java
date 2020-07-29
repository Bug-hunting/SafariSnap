package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText Name,Password;
    private Button login;

    private TextView signup;
    private FirebaseAuth firebaseAuth; //creating object of main class
    private ProgressDialog progressDialog;

    private TextView forget_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIViews();

        //Checking if user is logged in
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if( user != null)
        {
            finish(); // completes the current activity
            startActivity(new Intent(MainActivity.this,SecondActivity.class)); // redirects directly to homepage
        }

        //when login button is press
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });


        //when sign up text is press
        signup.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent event = new Intent(MainActivity.this,signup.class);
                startActivity(event);
            }
        }));

        //when forget password is press
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this,PasswordActivity.class));
            }
        });


    }

    private void setupUIViews()
    {
        Name = findViewById(R.id.etName);
        Password = findViewById(R.id.etPassword);

        login = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.tvRegister);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this); //shows a visual graphic

        forget_password = findViewById(R.id.tvForgotPassword);

    }

    //creating function to check password
    private void validate(String userName,String userPassword)
    {

        if(userName.isEmpty() || userPassword.isEmpty())
        {
            Toast.makeText(this,"Enter Login Information", Toast.LENGTH_SHORT).show();
        }
        else
            {

                progressDialog.setMessage("Connecting to server");
                progressDialog.show();

                //going to database
                firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss(); //close the dialog after completion

                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(MainActivity.this, SecondActivity.class)); // redirects directly to homepage
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show(); //alerts user for failed login
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show(); //alerts user for failed login
                        }
                    }
                });
            }


    }

}