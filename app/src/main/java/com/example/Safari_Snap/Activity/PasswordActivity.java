package com.example.Safari_Snap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Safari_Snap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    private EditText email;
    private Button resetpassword;
    private ImageView back_button;

    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        setupUIViews();

        //redirects user to login page whenever login text is clicked in sign up page
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordActivity.this, MainActivity.class));
            }
        });

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String useremail = email.getText().toString().trim();

                if (useremail.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
                } else {
                    mauth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Password Reset Email sent", Toast.LENGTH_SHORT).show();
                                finish();

                                startActivity(new Intent(PasswordActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Account not Found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });
    }

    private void setupUIViews() {
        email = findViewById(R.id.etPasswordEmail);
        resetpassword = findViewById(R.id.btnPasswordReset);
        back_button = findViewById(R.id.back__button_password);
        mauth = FirebaseAuth.getInstance();

    }


}