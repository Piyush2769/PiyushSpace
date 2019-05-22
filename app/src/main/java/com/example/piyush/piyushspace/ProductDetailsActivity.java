package com.example.piyush.piyushspace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.piyush.piyushspace.Model.Products;
import com.example.piyush.piyushspace.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productName,productPrice,productDescription;

    private String productId="", state="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId=getIntent().getStringExtra("pid");

        addToCartBtn=findViewById(R.id.add_to_cart);
        productImage=findViewById(R.id.product_image_details);
        productDescription=findViewById(R.id.product_image_desc);
        productName=findViewById(R.id.product_image_title);
        productPrice=findViewById(R.id.product_image_price);
        numberButton=findViewById(R.id.number_btn);

        getProductDetails(productId);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    adingToCartList();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
    }

    private void adingToCartList()
    {
        String saveCurrentDate,saveCurrentTime;

        Calendar calForDate= Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef =FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("pid",productId);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentOnlineUsers.getPhone())
                .child("Products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        cartListRef.child("Admin View").child(Prevalent.currentOnlineUsers.getPhone())
                                .child("Products").child(productId)
                                .updateChildren(cartMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        Toast.makeText(ProductDetailsActivity.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();

                                        Intent i=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                        startActivity(i);

                                    }
                                });
                    }
                });



    }

    private void getProductDetails(String productId)
    {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Products products=dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText("Rs."+products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef=FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUsers.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shippingState=dataSnapshot.child("status").getValue().toString();


                    if(shippingState.equals("Shipped"))
                    {
                        state="Order Shipped";
                    }
                    else if(shippingState.equals("Not Shipped"))
                    {
                        state="Order Placed";
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
