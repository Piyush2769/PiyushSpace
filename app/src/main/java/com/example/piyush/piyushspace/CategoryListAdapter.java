package com.example.piyush.piyushspace;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.myViewHolder>
{
    Context mContext;
    List<category_item> mData;

    public CategoryListAdapter(Context mContext,List<category_item> mData) {
        this.mContext = mContext;
        this.mData=mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View v=layoutInflater.inflate(R.layout.category_cart_item,viewGroup,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i)
    {
        myViewHolder.background.setImageResource(mData.get(i).getBackground());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView background;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            background=itemView.findViewById(R.id.card_background);
        }
    }
}
