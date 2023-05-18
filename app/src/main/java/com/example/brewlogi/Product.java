package com.example.brewlogi;

public class Product {
    String stallname;

    String productName;
    String cansDistributed;
    String cansLeft;

    int cost;

    int imageResource;

    public Product(String productName, String cansDistributed, String cansLeft) {
        this.productName = productName;
        this.cansDistributed = cansDistributed;
        this.cansLeft = cansLeft;

    }

    public Product(String productName, String cansDistributed, String cansLeft, int cost, int imageResource){
        this.productName = productName;
        this.cansDistributed = cansDistributed;
        this.cansLeft = cansLeft;
        this.cost = cost;
        this.imageResource = imageResource;
    }
     Product(String productName, String stallname) {
        this.productName = productName;
        this.stallname=stallname;
    }

    public String getStallname() {
        return stallname;
    }

    public String getProductName() {
        return productName;
    }

    public String getCansDistributed() {
        return cansDistributed;
    }

    public String getCansLeft() {
        return cansLeft;
    }

    public int getCost() {
        return cost;
    }

    public int getImageResource() {
        return imageResource;
    }
}
