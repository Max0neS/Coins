package com.example.coinwallet.utils;

import com.example.coinwallet.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransactionCache {

    // Кэш: ключ - строка вида "userName:type", значение - список транзакций
    private final Map<String, List<Transaction>> cache = new HashMap<>();

    // Получение данных из кэша
    public List<Transaction> getTransactions(String userName, boolean type) {
        String key = userName + ":" + type;
        return cache.get(key);
    }

    // Добавление данных в кэш
    public void putTransactions(String userName, boolean type, List<Transaction> transactions) {
        String key = userName + ":" + type;
        cache.put(key, transactions);
    }

    public void clearCache() {
        cache.clear();
    }
}