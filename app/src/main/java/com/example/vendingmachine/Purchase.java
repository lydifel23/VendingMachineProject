package com.example.vendingmachine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Purchase extends Activity {
    SharedPreferences sp;
    TextView moneylefttext,holdingtext,totaltext;
    View coin10,coin20,coin50,coin1,coin2;
    Button insertbutton,cancelbutton,getbutton,resetbutton;

    int[] coins={0,0,0,0,0};                  //how many coins of each category are inserted in the machine
    double[] coinsvalue={0.1,  0.2, 0.5,1,2};

    boolean[] coinsselected= {false,false,false,false,false};  //the coin that is currently selected//Boolean[] coinsselected={10selected,20selected,50selected,1selected,2selected};
    double total,balance;
    public static int k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchaseview);

        //shared values
        sp=getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        total=sp.getFloat("totalmoney",0);
        //find layout objects
        moneylefttext=this.findViewById(R.id.moneylefttext);
        holdingtext=this.findViewById(R.id.holdingtext);totaltext=this.findViewById(R.id.totaltext);
        coin10=this.findViewById(R.id.image10);coin20=this.findViewById(R.id.image20);coin50=this.findViewById(R.id.image50);
        coin1=this.findViewById(R.id.image1);coin2=this.findViewById(R.id.image2);
        insertbutton=this.findViewById(R.id.insert);cancelbutton=this.findViewById(R.id.cancel);getbutton=this.findViewById(R.id.get);resetbutton=this.findViewById(R.id.resetbtn);
        //initial state
        insertbutton.setEnabled(false);cancelbutton.setEnabled(false);getbutton.setEnabled(false);
        totaltext.setText("Your total is "+total+"E");

        //tap on coins-images, activity
        coin10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertbutton.setEnabled(true);
                for(int i=0;i<coinsselected.length;i++) coinsselected[i]=false;
                coinsselected[0]=true;   //unique coin selected
                holdingtext.setText("You are holding 10 cents");
            }
        });
        coin20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertbutton.setEnabled(true);
                for(int i=0;i<coinsselected.length;i++) coinsselected[i]=false;
                coinsselected[1]=true;  //unique coin selected
                holdingtext.setText("You are holding 20 cents");
            }
        });
        coin50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertbutton.setEnabled(true);
                for(int i=0;i<coinsselected.length;i++) coinsselected[i]=false;
                coinsselected[2]=true;  //unique coin selected
                holdingtext.setText("You are holding 50 cents");
            }
        });
        coin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertbutton.setEnabled(true);
                for(int i=0;i<coinsselected.length;i++) coinsselected[i]=false;
                coinsselected[3]=true; //unique coin selected
                holdingtext.setText("You are holding 1 euro");
            }
        });
        coin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertbutton.setEnabled(true);
                for(int i=0;i<coinsselected.length;i++) coinsselected[i]=false;
                coinsselected[4]=true; //unique coin selected
                holdingtext.setText("You are holding 2 euros");
            }
        });

        //tap on insert button activity
        insertbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelbutton.setEnabled(true);
                for(int i=0;i<coinsselected.length;i++){
                    if (coinsselected[i]==true) {
                        coins[i]=coins[i]+1;         //insert selected coin in the machine
                    }
                }
                holdingtext.setText("");             //text that shows which coin is selected
                balance= total-calcInsertedMoney(coins,coinsvalue); //difference from the total
                if (balance>0) moneylefttext.setText(String.format("%.1f", balance)+"E left");  //balance>0 == there are money left to pay
                if (balance<=0){                                                                //balance<0 == money paid or overpaid(change)
                    moneylefttext.setText(String.format("%.1f", -1*balance)+"E change");
                    getbutton.setEnabled(true);       //money paid-> now the consumer can get the products
                }
            }
        });
        //get products-button activity
        getbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k=0;
                Purchase.this.finish();        //close this screen
                //pass the value of change to next activity
                SharedPreferences.Editor editor=sp.edit();
                editor.putFloat("change",(float) -balance);
                editor.commit();
                //start new screen
                Intent intent=new Intent(Purchase.this,Products.class);
                startActivity(intent);
            }
        });
        //cancel-button activity
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k=1;
                Purchase.this.finish();   //close this screen
                //pass values of refund to next activity
                SharedPreferences.Editor editor=sp.edit();
                editor.putFloat("change",(float)calcInsertedMoney(coins,coinsvalue));
                editor.commit();

                //start new screen
                Intent intent=new Intent(Purchase.this,Change.class);
                startActivity(intent);
            }
        });
        //reset button activity- return to main activity with no data
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Purchase.this.finish();
                Intent intent=new Intent(Purchase.this,MainActivity.class);
                startActivity(intent);

            }
        });

    }
    //calculate the amount of money inserted
    public double calcInsertedMoney(int[] coins,double[] coinsvalue){  //calculate total of euros
        double inserted=0;
        for(int i=0;i<coins.length;i++){
            inserted=inserted+coins[i]*coinsvalue[i];
        }
        return  inserted;
    }

}
