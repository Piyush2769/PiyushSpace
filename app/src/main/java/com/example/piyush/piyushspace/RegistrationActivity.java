package com.example.piyush.piyushspace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {

    private Button CreateAccount;
    private EditText InputName,InputPhoneNumber,InputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //To set the top to the same colour as that of the viewPager
        if(Build.VERSION.SDK_INT >=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Toasty.Config.getInstance().apply();


        CreateAccount=findViewById(R.id.register_btn);
        InputName=findViewById(R.id.register_name_input);
        InputPassword=findViewById(R.id.register_password_input);
        InputPhoneNumber=findViewById(R.id.register_phone_number_input);
        loadingBar=new ProgressDialog(this);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount()
    {
        String name=InputName.getText().toString();
        String phoneNumber=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please Enter your Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneNumber) || phoneNumber.length()<10)
        {
            Toast.makeText(this, "Please Enter your Valid Number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter your Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating an Account");
            loadingBar.setMessage("Please wait while we are creating");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name,phoneNumber,password);
        }

    }

    private void ValidatePhoneNumber(final String name, final String phoneNumber, final String password)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!(dataSnapshot.child("Users").child(phoneNumber).exists()))
                {
                    HashMap<String,Object> userDataMap=new HashMap<>();
                    userDataMap.put("phone",phoneNumber);
                    userDataMap.put("password",password);
                    userDataMap.put("name",name);

                    RootRef.child("Users").child(phoneNumber).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toasty.success(RegistrationActivity.this, "Account created successfully", Toast.LENGTH_SHORT, true).show();
                                        loadingBar.dismiss();

                                        Intent i=new Intent(RegistrationActivity.this,LoginActivity.class);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toasty.error(RegistrationActivity.this, "Error! Try Again later", Toast.LENGTH_SHORT, true).show();
                                    }

                                }
                            });

                }
                else 
                {
                    Toast.makeText(RegistrationActivity.this, "This "+phoneNumber+" already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please try again using different number", Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(RegistrationActivity.this,MainActivity.class);
                    startActivity(i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
