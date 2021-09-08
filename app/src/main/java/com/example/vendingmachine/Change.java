package com.example.vendingmachine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.vendingmachine.Purchase.k;

public class Change extends Activity {
    SharedPreferences sp;
    float change;double total;

    TextView yourchange,n10,n20,n50,n1,n2;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change);

        //shared values
        sp=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        change=sp.getFloat("change",1);
        total=sp.getFloat("totalmoney",0);

        //find layout objects
        yourchange=this.findViewById(R.id.yourchangetext);
        n10=this.findViewById(R.id.change10);n20=this.findViewById(R.id.change20);n50=this.findViewById(R.id.change50);n1=this.findViewById(R.id.change1);n2=this.findViewById(R.id.change2);
        ok=this.findViewById(R.id.okccoinsbtn);

        if (k==0) yourchange.setText("Your change is "+change+"E");int[]leastcoins=calcLeastCoinsChange(change);
        if (k==1){yourchange.setText("Your refund is "+(change)+"E");leastcoins=calcLeastCoinsChange(change);}

        //show number of coins to be returned
        n2.setText("x"+leastcoins[0]);n1.setText("x"+leastcoins[1]);n50.setText("x"+leastcoins[2]);n20.setText("x"+leastcoins[3]);n10.setText("x"+leastcoins[4]);

        //ok-button activity, close this screen, return to main activity
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Change.this.finish();
                Intent intent=new Intent(Change.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    int[] calcLeastCoinsChange(double change){   //calculate change ->least number of coins
        change=round2Dec(change);
        int[] leastcoins={0,0,0,0,0};double coins[]={2,1,0.5,0.2,0.1};double reminder;
        //change=a*2+b*1+c*0.5+d*0.2+e*0.1
        for(int i=0;i<coins.length;i++){
            if (change>=coins[i]){                           //check if we can use a specific coin as change
                reminder=round2Dec(change % coins[i]);    // calculate the reminder
                leastcoins[i]=(int) Math.round((change-reminder)/coins[i]);  // how many of them can we use
                change = reminder;                                           //refresh change to be returned
            }
        }
        return leastcoins;
    }

    double round2Dec(double a){
        double b=Math.round(a*100);
        return b/100;
    }
}
