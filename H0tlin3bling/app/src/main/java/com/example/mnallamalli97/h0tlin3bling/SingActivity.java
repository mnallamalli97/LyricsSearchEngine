package com.example.mnallamalli97.h0tlin3bling;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SingActivity extends AppCompatActivity {

    BottomNavigationView navigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing);

        navigationMenu = findViewById(R.id.bottom_navigation);


        navigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_search:
                        //pass intent to go to singing page
                        Intent search = new Intent(SingActivity.this,MainActivity.class);
                        SingActivity.this.startActivity(search);
                        break;
                    case R.id.navigation_sing:
                        break;
                }
                return true;
            }
        });

    }
}
