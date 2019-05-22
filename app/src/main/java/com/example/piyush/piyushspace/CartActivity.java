package com.example.piyush.piyushspace;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush.piyushspace.Prevalent.Prevalent;
import com.example.piyush.piyushspace.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount,txtMsg1;

    private int overTotalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn=findViewById(R.id.next_process_btn);
        txtTotalAmount=findViewById(R.id.total_price);
        txtMsg1=findViewById(R.id.msg1);

        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i= new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                i.putExtra("Total Price",String.valueOf(overTotalPrice));
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();

        txtTotalAmount.setText("Total Price=Rs."+String.valueOf(overTotalPrice));

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options
                = new FirebaseRecyclerOptions.Builder<Cart>()
                    .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlineUsers.getPhone())
                            .child("Products"),Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model)
            {
                holder.txtProductPrice.setText("Price="+model.getPrice());
                holder.txtProductName.setText(model.getPname());
                holder.txtProductQuantity.setText("Quantity= "+model.getQuantity());

                int oneTypeProduct=(Integer.parseInt(model.getPrice().replaceAll("\\D+","").trim())) * Integer.parseInt(model.getQuantity().trim());
                overTotalPrice=overTotalPrice+oneTypeProduct;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[]=new CharSequence[]
                                {
                                  "Edit",
                                  "Remove"
                                };
                        AlertDialog.Builder builder =new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i==0)
                                {
                                    Intent i1=new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    i1.putExtra("pid",model.getPid());
                                    startActivity(i1);
                                }

                                if(i==1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUsers.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Item removed Successfully", Toast.LENGTH_SHORT).show();

                                                        Intent i1=new Intent(CartActivity.this,HomeActivity.class);
                                                        startActivity(i1);

                                                    }

                                                }
                                            });
                                }

                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout,viewGroup,false);
                CartViewHolder holder=new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
                    String userName=dataSnapshot.child("name").getValue().toString();

                    if(shippingState.equals("Shipped"))
                    {
                        txtTotalAmount.setText("Dear "+userName+ "\n order is shipped successfully!");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can purchase more products if you want!", Toast.LENGTH_SHORT).show();
                    }
                    //else if(shippingState.equals("Not Shipped"))
                    //{
                        //txtTotalAmount.setText("Product has not been shipped till now");
                        Toast.makeText(CartActivity.this, "You can purchase more products if you want!", Toast.LENGTH_SHORT).show();

                    //}
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
