package com.example.coinwallet.utils;

import com.example.coinwallet.dto.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryCache.class);
    private static final int MAX_CACHE_SIZE = 8;

    // LinkedHashMap для реализации LRU
    private final Map<Long, List<TransactionDTO>> cache;

    public InMemoryCache() {
        this.cache = new LinkedHashMap<Long, List<TransactionDTO>>(MAX_CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, List<TransactionDTO>> eldest) {
                boolean shouldRemove = size() > MAX_CACHE_SIZE;
                if (shouldRemove) {
                    // MODIFIED: Уточнили сообщение
                    LOGGER.info("Cache overflow, removing least recently used entry for userId: {}", eldest.getKey());
                }
                return shouldRemove;
            }
        };
    }

    public void put(Long userId, List<TransactionDTO> transactions) {
        // MODIFIED: Уточнили сообщение
        LOGGER.info("Storing transactions in cache for userId: {}", userId);
        cache.put(userId, transactions);
    }

    public List<TransactionDTO> get(Long userId) {
        List<TransactionDTO> transactions = cache.get(userId);
        if (transactions != null) {
            // MODIFIED: Уточнили сообщение
            LOGGER.info("Retrieved transactions from cache for userId: {}", userId);
        } else {
            // MODIFIED: Уточнили сообщение
            LOGGER.info("No transactions found in cache for userId: {}", userId);
        }
        return transactions;
    }

    public void remove(Long userId) {
        if (cache.remove(userId) != null) {
            LOGGER.info("Removed cache entry for userId: {}", userId);
        }
    }

    public void clear() {
        cache.clear();
        LOGGER.info("Cache cleared");
    }
}