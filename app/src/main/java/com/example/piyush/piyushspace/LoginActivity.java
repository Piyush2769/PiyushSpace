package com.example.piyush.piyushspace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush.piyushspace.Model.Users;
import com.example.piyush.piyushspace.Prevalent.Prevalent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputNumber,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private CheckBox chkBoxrememberMe;
    private TextView AdminLink,NotAdminLink;
    private String parentDbName="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

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


        LoginButton=findViewById(R.id.login_btn);
        InputNumber=findViewById(R.id.login_phone_number_input);
        InputPassword=findViewById(R.id.login_password_input);
        loadingBar=new ProgressDialog(this);
        chkBoxrememberMe=findViewById(R.id.remember_me_chk);
        AdminLink=findViewById(R.id.admin_panel_link);
        NotAdminLink=findViewById(R.id.not_admin_panel_link);
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });

    }

    private void LoginUser()
    {
        String phone=InputNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Enter your Number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter your Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Signing in Account");
            loadingBar.setMessage("Please wait while we are Signing in..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {

        if(chkBoxrememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);

        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users userData=dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Admins"))
                            {
                                Toasty.success(LoginActivity.this, "Login Admin Successful", Toast.LENGTH_SHORT, true).show();
                                loadingBar.dismiss();

                                Intent i=new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(i);
                            }
                            else if(parentDbName.equals("Users"))
                            {
                                Toasty.success(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT, true).show();
                                loadingBar.dismiss();

                                Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                                Prevalent.currentOnlineUsers=userData;
                                startActivity(i);
                            }
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toasty.error(LoginActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this "+phone+" number does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
