package com.gmail.superclean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar nToolbar;



    //buttons
    private Button nHomeBtn;
    private Button nJobsBtn;
    private Button nChatBtn;

    private ViewPager nViewPager;
    private SectionsPagerAdapter nSectionsPagerAdapter;
    private TabLayout nTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        /*------------------START TOOLBAR----------------------------*/
        nToolbar = findViewById(R.id.mainPageToolbar);
        setSupportActionBar(nToolbar);
        getSupportActionBar().setTitle("Main Page");
        /*-------------------END TOOLBAR---------------------------*/

        /*------------------START PAGER----------------------------*/

        nViewPager = (ViewPager) findViewById(R.id.mainTabPager);
        nSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        nViewPager.setAdapter(nSectionsPagerAdapter);
        nTabLayout = findViewById(R.id.mainTabs);
        nTabLayout.setupWithViewPager(nViewPager);

        /*------------------END PAGER----------------------------*/
    }

    /*-------------------START USER SIGN-IN CHECKER-----------------------*/
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        //IF USER IS NOT SIGNED-IN ...
        if(currentUser == null)
        {
            // SEND TO STARTING PAGE METHOD
           sendToStart();
        }
    }

    /*STARTING PAGE METHOD*/
    private void sendToStart()
    {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    /*MENU SECTION*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

         getMenuInflater().inflate(R.menu.main_menu, menu);

         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_menu_logout)
        {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }if(item.getItemId() == R.id.main_menu_about)
        {
            Intent aboutIntent = new Intent(MainActivity.this, About.class);
            startActivity(aboutIntent);
        }
        if(item.getItemId() == R.id.main_menu_account)
        {
            Intent accIntent = new Intent(MainActivity.this, UserAccount.class);
            startActivity(accIntent);
        }

        return true;
    }
}
