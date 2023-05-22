package com.example.brewlogi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.BeerName.setText(product.getProductName());
        holder.OfferType.setText(product.getType());
        holder.Cost.setText("$" + String.valueOf(product.getCost()) + ".99");

        holder.parentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Order.class);
                intent.putExtra("productName", product.getProductName());
                intent.putExtra("OfferType", product.getType());
                intent.putExtra("Cost","$" + String.valueOf(product.getCost()) + ".99");
                intent.putExtra("Image", product.getImageResource());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView BeerName, OfferType, Cost; // Add costTextView
        CardView parentCard;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            BeerName = itemView.findViewById(R.id.beername_rv);
            OfferType = itemView.findViewById(R.id.type_rv);
            Cost = itemView.findViewById(R.id.cost_rv);
            parentCard = itemView.findViewById(R.id.parentCard);
        }
    }
}