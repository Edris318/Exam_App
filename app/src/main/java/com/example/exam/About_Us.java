package com.example.exam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About_Us extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        toolbar = findViewById(R.id.toolbar_aboutUS);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        Element adsElement = new Element();
        View aboutPage = new AboutPage(this)
                .isRTL(true)
                .setImage(R.drawable.ic_launcher_round_score)
                .setDescription(getResources().getString(R.string.description_app))
                .addItem(new Element().setTitle("Version 1.1"))
                .addGroup("CONNECT WITH US!")
                .addEmail("edrissharifi7@gmail.com","Email")
                .addYoutube("UCycTbgsfpuMKPAeNgmUOGew")   //Enter your youtube link here (replace with my channel link)
                .addPlayStore("com.example.exam")   //Replace all this with your package name
                .addInstagram("edris_keyana")    //Your instagram id
                .addItem(createCopyright())
                .addGitHub("https://github.com/Edris318","Edris Sharifi")
                .create();
        setContentView(aboutPage);
    }
    private Element createCopyright()
    {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d & Developed by Edris Keyana", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIconDrawable(R.drawable.ic_right);
        copyright.setGravity(Gravity.CENTER);
        copyright.setGravity(Gravity.START);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About_Us.this,copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }


}