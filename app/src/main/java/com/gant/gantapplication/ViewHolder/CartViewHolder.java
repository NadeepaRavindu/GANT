package com.gant.gantapplication.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gant.gantapplication.R;



public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private AdapterView.OnItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtProductName =itemView.findViewById(R.id.cart_product_name);
        txtProductPrice =itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity =itemView.findViewById(R.id.cart_product_quantity);
    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
