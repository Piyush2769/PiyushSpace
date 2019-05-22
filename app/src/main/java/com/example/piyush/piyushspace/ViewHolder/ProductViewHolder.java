package com.example.piyush.piyushspace.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piyush.piyushspace.Interface.ItemClickListener;
import com.example.piyush.piyushspace.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView=itemView.findViewById(R.id.product_image);
        txtProductName=itemView.findViewById(R.id.product_nname);
        txtProductDescription=itemView.findViewById(R.id.product_description);
        txtProductPrice=itemView.findViewById(R.id.product_pricee);


    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}
