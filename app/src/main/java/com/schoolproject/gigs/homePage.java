package com.schoolproject.gigs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class homePage extends AppCompatActivity {


    private GoogleSignInAccount signInAccount;
    BottomNavigationView bottomNavigationView;
    homeFragment home = new homeFragment();
    profileFragment profile = new profileFragment();
    searchFragment search = new searchFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.home):
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                        return true;
                    case (R.id.profile):
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profile).commit();
                        return true;
                    case (R.id.search):
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, search).commit();
                        return true;
                    case (R.id.add):
                        add();
                        return true;
                }
                return false;
            }
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
    }

    private void add() {
        Intent intent = new Intent(this, newFlashCard.class);
        startActivity(intent);
    }


//    public void onClickOnFAB(View view) {
//        Intent intent = new Intent(this, newFlashCard.class);
//        startActivity(intent);
//    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}



