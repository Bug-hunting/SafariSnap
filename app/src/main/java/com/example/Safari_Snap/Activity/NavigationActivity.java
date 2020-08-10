package com.example.Safari_Snap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.Safari_Snap.Fragments.ResultFragment;
import com.example.Safari_Snap.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mauth;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_View);

        mauth = FirebaseAuth.getInstance();

        init_drawer();

    }


    //setting up navigation drawer
    private void init_drawer() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //define activity when a menu item is pressed
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.homeMenu: {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.HomeScreen);
            }
            break;


            case R.id.profileMenu: {
                if (isValidDestination(R.id.profileScreen)) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.profileScreen);
                }
            }
            break;

            //when menu item is pressed redirects to the stats screen fragment
            case R.id.aboutMenu: {
                if (isValidDestination(R.id.aboutScreen)) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.aboutScreen);
                }
            }
            break;

            case R.id.statsMenu: {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.statsScreen);
            }
            break;

            //when menu item is pressed logout the user
            case R.id.LogoutMenu: {
                Logout_event();
            }
            break;


        }

        // closes the drawer menu to the left when menu item is selected
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //do this when button is selected
        if (item.getItemId() == android.R.id.home) {//check if drawer is open
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true; // to register the click
            } else
                return false; //to not register the click
        }
        return super.onOptionsItemSelected(item);
    }

    //back button implementation
    @Override
    public boolean onSupportNavigateUp() {
        //opens navigation drawer but doesn't close it
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
        //switch cae android.R.id.home completes the closing action
    }

    //this function is removing back track when user selects the same option multiple times//this function is removing back track when user selects the same option multiple times
    private boolean isValidDestination(int destination) {
        return destination != Objects.requireNonNull(Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination()).getId();
    }


    private void Logout_event() {
        //when user logs out clear the stats
        ResultFragment.correct = 0;
        ResultFragment.incorrect = 0;

        mauth.signOut(); //logout user from firebase
        finish(); //end activity
        startActivity(new Intent(NavigationActivity.this, MainActivity.class));
    }


}