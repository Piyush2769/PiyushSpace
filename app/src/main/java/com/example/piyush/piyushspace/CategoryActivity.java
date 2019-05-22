package com.example.piyush.piyushspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);



        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        RecyclerView recyclerView=findViewById(R.id.cart_category);
        List<category_item> mList=new ArrayList<>();
        mList.add(new category_item(R.drawable.cat2));
        mList.add(new category_item(R.drawable.cat3));
        mList.add(new category_item(R.drawable.cat4));
        mList.add(new category_item(R.drawable.cat5));
        mList.add(new category_item(R.drawable.cat6));
        mList.add(new category_item(R.drawable.cat7));
        CategoryListAdapter adapter=new CategoryListAdapter(this,mList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

}
