package com.hippo.flash.meetutu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Personal on 04-Dec-16.
 */
public class Info_Activity extends AppCompatActivity {

    String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_layout);

        ActionBar actionBar = getSupportActionBar();

        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E91E63")));

        Bundle bundle = getIntent().getExtras();
       String name = bundle.getString("name");
        String email= bundle.getString("email");
         mobile = bundle.getString("mobile");
        String tut = bundle.getString("tut");
        String adr = bundle.getString("adr");
        String cat = bundle.getString("cat");


        TextView tutorname=(TextView) findViewById(R.id.tutorname);
        TextView tutemail=(TextView) findViewById(R.id.tutemail);
        TextView tutmobile=(TextView) findViewById(R.id.tutmobile);
        TextView tutname=(TextView) findViewById(R.id.tutname);
        TextView tutaddr=(TextView) findViewById(R.id.tutaddr);
        TextView tuttype=(TextView) findViewById(R.id.tuttype);
      String tut_cat="T";
        String lea_cat="L";

        tutorname.setText(name);
        tutemail.setText(email);
        tutmobile.setText(mobile);
        tutname.setText(tut);
        tutaddr.setText(adr);

        if(cat.equals(tut_cat))
        tuttype.setText("Tutor");

        if(cat.equals(lea_cat))
            tuttype.setText("Learner");

          mobile="91"+mobile;

    }


    public void connectwhatsapp(View v) {

        Uri uri = Uri.parse("smsto:" + mobile);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
        sendIntent.setPackage("com.whatsapp");
        startActivity(sendIntent);
    }





}
