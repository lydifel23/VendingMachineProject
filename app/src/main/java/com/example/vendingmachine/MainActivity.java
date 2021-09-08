package com.example.vendingmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    View waterImage,cokecanImage,cokeImage,crunchImage;
    TextView totaltext,numWater,numCokecan,numCoke,numCrunch,plusbutton,minusbutton;
    Button purchasebutton,resetbutton;

    int[] products= {0,0,0,0};         //how many products (of each category) are in the basket//int[] products={water,cokecan,coke,crunch};
    double[] productsprices= {0.5,1,1.5,2.5};               //double[] productsprices={waterprice,cokecanprice,cokeprice,crunchprice};

    boolean[] productsselected= {false,false,false,false}; //the product that is currently selected //Boolean[] productsselected={waterselected,cokecanselected,cokeselected,crunchselected};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //transfer values by sp
        sp=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        //find layout objects
        waterImage=this.findViewById(R.id.waterimage);
        cokecanImage=this.findViewById(R.id.cokecanimage);
        cokeImage=this.findViewById(R.id.cokeimage);
        crunchImage=this.findViewById(R.id.crunchimage);

        totaltext=this.findViewById(R.id.total);
        numWater=this.findViewById(R.id.water);
        numCokecan=this.findViewById(R.id.cokecan);
        numCoke=this.findViewById(R.id.coke);
        numCrunch=this.findViewById(R.id.crunch);

        plusbutton=this.findViewById(R.id.plus);
        minusbutton=this.findViewById(R.id.minus);
        purchasebutton=this.findViewById(R.id.purchasebutton);
        resetbutton=this.findViewById(R.id.resetbutton);

        //initial state
        plusbutton.setEnabled(false);minusbutton.setEnabled(false);purchasebutton.setEnabled(false);

        //tap on product-images, activity
        waterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusbutton.setEnabled(true);minusbutton.setEnabled(true);
                for(int i=0;i<productsselected.length;i++) productsselected[i]=false;
                productsselected[0]=true;    //unique product selected
            }
        });
        cokecanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusbutton.setEnabled(true);minusbutton.setEnabled(true);
                for(int i=0;i<productsselected.length;i++) productsselected[i]=false;
                productsselected[1]=true; //unique product selected
            }
        });
        cokeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusbutton.setEnabled(true);minusbutton.setEnabled(true);
                for(int i=0;i<productsselected.length;i++) productsselected[i]=false;
                productsselected[2]=true; //unique product selected
            }
        });

        crunchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusbutton.setEnabled(true);minusbutton.setEnabled(true);
                for(int i=0;i<productsselected.length;i++) productsselected[i]=false;
                productsselected[3]=true; //unique product selected
            }
        });

        //tap on (+) button activity
        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<productsselected.length;i++){
                    if (productsselected[i]==true) {
                        products[i]=products[i]+1;         //add product to the basket
                    }
                }
                //purchase button is enabled provided that there are products selected
                if(calcTotal(products,productsprices)>0) purchasebutton.setEnabled(true);
                else purchasebutton.setEnabled(false);
                // show text views
                showNumberOfProductsSelected(products);
                showTotal(products,productsprices);
            }
        });

        //tap on minus-button activity
        minusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<productsselected.length;i++){
                    if (productsselected[i]==true) {
                        products[i]=products[i]-1;              //remove product from the basket
                        if (products[i]<0) products[i]=0;      //number of products<0 -> don't exist
                    }
                }
                //purchase button is enabled provided that there are products selected
                if(calcTotal(products,productsprices)>0) purchasebutton.setEnabled(true);
                else purchasebutton.setEnabled(false);
                // show text views
                showNumberOfProductsSelected(products);
                showTotal(products,productsprices);
            }
        });

        //tap on purchase-button activity
        purchasebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sp.edit();
                //pass values of total amount to other activities
                editor.putFloat("totalmoney",(float) calcTotal(products,productsprices));
                //pass values of number of selected products to other activities
                editor.putInt("numwater",products[0]);editor.putInt("numcokecan",products[1]);editor.putInt("numcoke",products[2]);editor.putInt("numcrunch",products[3]);
                editor.commit();
                //start new screen
                Intent intent=new Intent(MainActivity.this,Purchase.class);
                startActivity(intent);
            }
        });
        //tap on reset-button activity
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<products.length;i++) products[i]=0;                     //no products in the basket
                for(int i=0;i<productsselected.length;i++) productsselected[i]=false; //no products selected
                //refresh texts
                showNumberOfProductsSelected(products);
                showTotal(products,productsprices);
            }
        });
    }



    public double calcTotal(int[] products,double[] productsprices){  //calculate total of euros to be paid
        double total=0;
        for(int i=0;i<products.length;i++){
            total=total+products[i]*productsprices[i];
        }
        return  total;
    }

    public void showNumberOfProductsSelected(int[] products){       //set text that shows the products in the basket
        // show number of products that are selected
        numWater.setText("x "+  products[0] );
        numCokecan.setText("x "+  products[1] );
        numCoke.setText("x "+  products[2] );
        numCrunch.setText("x "+  products[3] );

    }

    public void showTotal(int[] products,double[] productsprices){    //set text that shows the total of euros to be paid
        totaltext.setText("Total of "+calcTotal(products,productsprices) +"E");
    }
}