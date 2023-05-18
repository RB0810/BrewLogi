package com.example.brewlogi;

public class Product {
    String stallname;

    String productName;
    String cansDistributed;
    String cansLeft;

    public Product(String productName, String cansDistributed, String cansLeft) {
        this.productName = productName;
        this.cansDistributed = cansDistributed;
        this.cansLeft = cansLeft;
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
}
