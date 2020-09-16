package com.gmail.superclean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class About extends AppCompatActivity {


    private Toolbar nToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);



        nToolbar = findViewById(R.id.aboutPageToolbar);
        setSupportActionBar(nToolbar);
        getSupportActionBar().setTitle("About");
    }


    /*STARTING PAGE METHOD*/
    private void sendToStart()
    {
        Intent startIntent = new Intent(About.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    /*MENU SECTION*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.about_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.about_menu_logout)
        {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        return true;
    }
}
