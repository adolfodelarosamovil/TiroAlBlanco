package com.adp.tiroalblanco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class AboutAuthorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ocultamos la ActionBar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about_author);
    }

    public void close(View view) {
        finish();
    }
}
