package com.example.piyush.piyushspace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tShirts, sportsTshirts,femaleDresses,sweaters;
    private ImageView glasses,hats,purse,shoes;
    private ImageView headphone,laptop,watch,mobile;
    private Button LogOutBtn,CheckOrdersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        tShirts=findViewById(R.id.t_shirts);
        sportsTshirts=findViewById(R.id.sports);
        femaleDresses=findViewById(R.id.female_dresses);
        sweaters=findViewById(R.id.sweater);
        glasses=findViewById(R.id.glasses);
        hats=findViewById(R.id.hats);
        purse=findViewById(R.id.purse);
        shoes=findViewById(R.id.shoes);
        headphone=findViewById(R.id.headphones);
        laptop=findViewById(R.id.laptops);
        watch=findViewById(R.id.watches);
        mobile=findViewById(R.id.mobiles);

        LogOutBtn=findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn=findViewById(R.id.check_orders_btn);

        LogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);
                startActivity(i);
            }
        });


        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","tShirts");
                startActivity(i);
            }
        });

        sportsTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","sports");
                startActivity(i);
            }
        });

        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","femaleDresses");
                startActivity(i);
            }
        });

        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","sweaters");
                startActivity(i);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","glasses");
                startActivity(i);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","hats");
                startActivity(i);
            }
        });

        purse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","purse");
                startActivity(i);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","shoes");
                startActivity(i);
            }
        });

        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","headphones");
                startActivity(i);
            }
        });

        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","laptop");
                startActivity(i);
            }
        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","mobile");
                startActivity(i);
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddnewProductActivity.class);
                i.putExtra("category","watch");
                startActivity(i);
            }
        });


    }
}
