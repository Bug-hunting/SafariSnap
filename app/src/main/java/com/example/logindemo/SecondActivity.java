package com.example.logindemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth mauth;
    private Button logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mauth = FirebaseAuth.getInstance();

        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    //this override function is required to handle menu option activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.LogoutMenu:
            {
                Logout_event();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void Logout_event()
    {
        mauth.signOut(); //logout user
        finish(); //end activity

        startActivity(new Intent(SecondActivity.this,MainActivity.class));
    }
}