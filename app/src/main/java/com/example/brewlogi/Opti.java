package com.example.brewlogi;



import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class Opti{

    public static Integer result;

    private static int getRandomInt(int min, int max) {
            Random random = new Random();
            return random.nextInt(max - min + 1) + min;
        }


    public static Map<String, Map<String, Map<String, Integer>>> generateSales(SalesDataCallback salesDataCallBack) {
        DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Inventory");

        Map<String, Map<String, Map<String, Integer>>> sales = new HashMap<>();

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String stall = dataSnapshot.getKey();
                    if (!"Total Stock".equals(stall)) {
                        Map<String, Map<String, Integer>> products = new HashMap<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String product = dataSnapshot1.getKey();
                            DataSnapshot dataSnapshot2 = dataSnapshot1.child("Cans distributed");
                            Integer cansDistributed = dataSnapshot2.getValue(Integer.class);
                            products.put(product, Map.of(
                                    "hour_1", getRandomInt(100, 130),
                                    "hour_2", getRandomInt(0, 10),
                                    "hour_3", getRandomInt(60, 85),
                                    "hour_4", getRandomInt(0, 7),
                                    "hour_5", getRandomInt(0, 5)
                            ));
                        }
                        sales.put(stall, products);
                    }
                    salesDataCallBack.onSalesDataReceived(sales);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        System.out.println(sales);
        return sales;
    }

        private static List<Integer> retrieve(String stallName, String productNo, Map<String, Map<String, Map<String, Integer>>> sales) {
            List<Integer> list = new ArrayList<>();

            for (String stall : sales.keySet()) {
                if (stall.equals(stallName)) {
                    for (String product : sales.get(stall).keySet()) {
                        if (product.equals(productNo)) {
                            for (String hour : sales.get(stall).get(product).keySet()) {
                                list.add(sales.get(stall).get(product).get(hour));
                            }
                        }
                    }
                }
            }

            return list;
        }

        private static int rate(String stallName, String productNo, Map<String, Map<String, Map<String, Integer>>> saleDict) {
            List<Integer> list = new ArrayList<>();

            for (int i = 0; i < 1000; i++) {
                List<Integer> sales = retrieve(stallName, productNo, saleDict);
                list.add(sales.stream().mapToInt(Integer::intValue).sum());
            }

            return (int) list.stream().mapToInt(Integer::intValue).average().orElse(0);
        }

       public static Integer getResult(String stallname, String prodname, Map<String, Map<String, Map<String, Integer>>> saleDict){

        result = rate(stallname, prodname, saleDict);
        return result;

        }


}


