package com.adp.tiroalblanco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.adp.tiroalblanco.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ocultamos la ActionBar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_about);

        //Cargar el fichero HTML en un web view
        WebView wv = (WebView) findViewById(R.id.webView1);
        wv.loadUrl("file:///android_asset/juego_de_la_diana.html");
    }

    public void close(View view) {
        finish();
        overridePendingTransition(R.anim.anim_slide_right_in, R.anim.anim_slide_right_out);
    }

    public void irAboutAuthor(View view) {
        Intent intent = new Intent(this, AboutAuthorActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up_in, R.anim.anim_slide_up_out);
    }
}
