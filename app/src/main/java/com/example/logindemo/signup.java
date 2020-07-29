package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class signup extends AppCompatActivity {

    private EditText Name,email,pwd;
    private Button submit;
    private ImageView back_button;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupUIViews(); //function which defines all the variables

        mAuth = FirebaseAuth.getInstance(); //initializing the Firebase auth




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate_user_info())
                {
                    // .trim() removes white spaces from user inputed text
                    String user_email = email.getText().toString().trim();
                    String user_pwd = pwd.getText().toString().trim();

                    //uploading data to database
                    mAuth.createUserWithEmailAndPassword(user_email,user_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(signup.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signup.this,MainActivity.class)); //redirect user to login page after successful account creation
                            }
                            else
                            {
                                Toast.makeText(signup.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });




                }


            }
        });

        //redirects user to login page whenever login text is clicked in sign up page
       back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signup.this,MainActivity.class));
            }
        });


    }

    //Defining java variables to xml variable
    private void setupUIViews()
    {
        Name = findViewById(R.id.suname);
        email = findViewById(R.id.suemailid);
        pwd = findViewById(R.id.supassword);


        submit = findViewById(R.id.joinus);
        back_button = findViewById(R.id.tvUserLogin);
    }

    //The function checks if all the field have been filled
    private Boolean validate_user_info()
    {
        Boolean result = false;
        String name = Name.getText().toString();
        String emailid = email.getText().toString();
        String password = pwd.getText().toString();

        if(name.isEmpty() || emailid.isEmpty() || password.isEmpty() )
        {
            //show notification to enter all details
            Toast.makeText(this,"Incomplete information", Toast.LENGTH_SHORT).show();
        }
        else
        {
            result = true;
        }
        return result;
    }


}