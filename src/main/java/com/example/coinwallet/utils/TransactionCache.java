package com.example.coinwallet.utils;

import com.example.coinwallet.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransactionCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCache.class);
    private static final int MAX_CACHE_SIZE = 3; // Ограничение размера кэша

    private final Map<String, List<Transaction>> cache;

    public TransactionCache() {
        this.cache = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, List<Transaction>> eldest) {
                if (size() > MAX_CACHE_SIZE) {
                    LOGGER.debug("Cache size limit reached, removing eldest entry: {}", eldest.getKey());
                    return true;
                }
                return false;
            }
        };
        LOGGER.info("TransactionCache initialized with max size: {}", MAX_CACHE_SIZE);
    }

    public List<Transaction> getTransactions(String key) {
        List<Transaction> transactions = cache.get(key);
        if (transactions != null) {
            LOGGER.debug("Cache hit for key: {}, returning {} transactions", key, transactions.size());
        } else {
            LOGGER.debug("Cache miss for key: {}", key);
        }
        return transactions;
    }

    public void putTransactions(String key, List<Transaction> transactions) {
        LOGGER.debug("Adding to cache: key = {}, {} transactions", key, transactions.size());
        cache.put(key, transactions);
    }

    public void clearCache() {
        LOGGER.info("Clearing entire cache, size before clear: {}", cache.size());
        cache.clear();
    }

    public void removeTransactions(String key) {
        LOGGER.debug("Removing cache entry for key: {}", key);
        cache.remove(key);
    }
}