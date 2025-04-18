package com.example.coinwallet.utils;

import com.example.coinwallet.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransactionCache {

    private final Map<String, List<Transaction>> cache = new HashMap<>();

    public List<Transaction> getTransactions(String key) {
        return cache.get(key);
    }

    public void putTransactions(String key, List<Transaction> transactions) {
        cache.put(key, transactions);
    }

    public void clearCache() {
        cache.clear();
    }
}