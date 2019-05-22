package com.example.piyush.piyushspace;

import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mPager;
    private int[] layouts={R.layout.first_slide, R.layout.second_slide,R.layout.third_slide};
    private MpagerAdapter mpagerAdapter;

    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    private Button btnSkip,btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if(new com.example.piyush.piyushspace.PreferenceManager(this).checkPreference())
        {
            loadHome();
        }

        //To set the top to the same colour as that of the viewPager
        if(Build.VERSION.SDK_INT >=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mPager=findViewById(R.id.viewPager);
        mpagerAdapter=new MpagerAdapter(layouts,this);
        btnNext=findViewById(R.id.btnNext);
        btnSkip=findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        mPager.setAdapter(mpagerAdapter);

        Dots_Layout=(LinearLayout)findViewById(R.id.dotsLayout);
        currentDots(0);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentDots(position);

                if(position==layouts.length-1)
                {
                    btnNext.setText("Start");
                    btnSkip.setVisibility(View.INVISIBLE);
                }
                else
                {

                    btnNext.setText("Next");
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void currentDots(int current_position)
    {
        if(Dots_Layout!=null)
            Dots_Layout.removeAllViews();

        dots=new ImageView[layouts.length];

        for(int i=0;i<layouts.length;i++)
        {
            dots[i]=new ImageView(this);
            if(i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);

            Dots_Layout.addView(dots[i],params);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnNext:
                loadNextSlide();
                break;

            case R.id.btnSkip:
                loadHome();
                new com.example.piyush.piyushspace.PreferenceManager(this).writePreference();
                break;
        }
    }

    private void loadHome()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void loadNextSlide()
    {
        int next_slide=mPager.getCurrentItem()+1;

        if(next_slide<layouts.length)
        {
            mPager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new com.example.piyush.piyushspace.PreferenceManager(this).writePreference();
        }
    }
}
