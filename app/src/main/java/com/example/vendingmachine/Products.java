package com.example.vendingmachine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Products extends Activity {
    SharedPreferences sp;
    int numwater,numcokecan,numcoke,numcrunch;

    TextView watertext,cokecantext,coketext,crunchtext;
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        //shared values
        sp=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        numwater=sp.getInt("numwater",0);numcokecan=sp.getInt("numcokecan",0);numcoke=sp.getInt("numcoke",0);numcrunch=sp.getInt("numcrunch",0);

       //find layout values
        watertext=this.findViewById(R.id.change10);cokecantext=this.findViewById(R.id.change20);coketext=this.findViewById(R.id.change50);crunchtext=this.findViewById(R.id.change1);
        ok=this.findViewById(R.id.okccoinsbtn);

        //set texts
        watertext.setText("x"+numwater);cokecantext.setText("x"+numcokecan);coketext.setText("x"+numcoke);crunchtext.setText("x"+numcrunch);

        //ok button activity- finish this screen, start a new one
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Products.this.finish();
                    Intent intent=new Intent(Products.this,Change.class);
                    startActivity(intent);
            }
        });
    }
}
