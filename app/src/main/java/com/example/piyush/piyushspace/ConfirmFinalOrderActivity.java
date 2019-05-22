package com.example.piyush.piyushspace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.piyush.piyushspace.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameE,phoneE,addressE,cityE;
    private Button confirmOrderBtn;

    private String totalAmt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmt=getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price= Rs."+totalAmt, Toast.LENGTH_SHORT).show();

        confirmOrderBtn=findViewById(R.id.confirm_final_order_btn);
        nameE=findViewById(R.id.final_full_name);
        phoneE=findViewById(R.id.final_phone_number);
        addressE=findViewById(R.id.final_address);
        cityE=findViewById(R.id.final_city);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });

    }

    private void Check()
    {
        if(TextUtils.isEmpty(nameE.getText().toString()))
        {
            Toast.makeText(this, "Please fill the name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneE.getText().toString()))
        {
            Toast.makeText(this, "Please fill the phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressE.getText().toString()))
        {
            Toast.makeText(this, "Please fill the full address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityE.getText().toString()))
        {
            Toast.makeText(this, "Please fill the city name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder()
    {
        final String saveCurrentDate,saveCurrentTime;

        Calendar calForDate= Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUsers.getPhone());

        HashMap<String,Object> ordersMap=new HashMap<>();

        ordersMap.put("totalAmount",totalAmt);
        ordersMap.put("name",nameE.getText().toString());
        ordersMap.put("phone",phoneE.getText().toString());
        ordersMap.put("address",addressE.getText().toString());
        ordersMap.put("city",cityE.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("status","Not Shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUsers.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Your Order has been placed successfully", Toast.LENGTH_SHORT).show();

                                        Intent i=new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }
                                }
                            });

                }
            }
        });


    }
}
