package com.example.brewlogi;

import java.util.Map;

public interface SalesDataCallback {
    void onSalesDataReceived(Map<String, Map<String, Map<String, Integer>>> sales);
}
