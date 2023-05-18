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
import androidx.recyclerview.widget.RecyclerView;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productNameTextView.setText(product.getProductName());

        holder.costTextView.setText("$" + String.valueOf(product.getCost()) + ".99"); // Add this
        holder.productImage.setImageResource(product.getImageResource());



        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer number = Integer.parseInt(holder.numberTextView.getText().toString());
                if (number > 1) {
                    number--;
                    holder.numberTextView.setText(String.valueOf(number));
                }
            }
        });

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer number = Integer.parseInt(holder.numberTextView.getText().toString());
                number++;
                holder.numberTextView.setText(String.valueOf(number));
            }
        });

        holder.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer number = Integer.parseInt(holder.numberTextView.getText().toString());
                Intent intent = new Intent(context, OrderConfirmation.class);
                intent.putExtra("numberValue", number);
                intent.putExtra("ProductName", product.getProductName());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {


        ImageButton minusButton;
        ImageButton plusButton;
        Button placeOrderButton;
        TextView productNameTextView, cansDistributedTextView, cansLeftTextView, costTextView; // Add costTextView
        ImageView productImage;
        TextView numberTextView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            minusButton = itemView.findViewById(R.id.minusButton);
            plusButton = itemView.findViewById(R.id.plusButton);
            placeOrderButton = itemView.findViewById(R.id.placeOrderButton);

            costTextView = itemView.findViewById(R.id.costTextView); // Add this
            productImage = itemView.findViewById(R.id.productImage);

        }
    }
}