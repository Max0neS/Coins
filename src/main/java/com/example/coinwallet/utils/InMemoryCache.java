package com.example.coinwallet.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class InMemoryCache<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryCache.class);

    private static class CacheEntry<V> {
        private final V value;
        private final long expiryTime;
        private int frequency;

        public CacheEntry(V value, long ttlMillis) {
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + ttlMillis;
            this.frequency = 1; // Initial frequency
        }

        public V getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= expiryTime;
        }

        public int getFrequency() {
            return frequency;
        }

        public void incrementFrequency() {
            this.frequency++;
        }
    }

    private final Map<K, CacheEntry<V>> cache;
    private final long ttlMillis;
    private final int maxSize;

    private final ScheduledExecutorService scheduler;


    public InMemoryCache() {
        this(300_000, 100); // 5 minutes, 100 entries
    }

    public InMemoryCache(long ttlMillis, int maxSize) {
        this.ttlMillis = ttlMillis;
        this.maxSize = maxSize;
        this.cache = new ConcurrentHashMap<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        // ADDED: Schedule periodic cleanup
        scheduler.scheduleAtFixedRate(
                this::evictExpiredEntries, ttlMillis, ttlMillis, TimeUnit.MILLISECONDS);
        LOGGER.info("InMemoryCache initialized with TTL={}ms, maxSize={}", ttlMillis, maxSize);
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            LOGGER.info("Cache miss for key: {}", key);
            return null;
        }
        if (entry.isExpired()) {
            cache.remove(key);
            LOGGER.info("Cache entry expired for key: {}", key);
            return null;
        }
        entry.incrementFrequency();
        LOGGER.info("Cache hit for key: {}, frequency: {}", key, entry.getFrequency());
        LOGGER.debug("Cache size: {}", cache.size());
        return entry.getValue();
    }

    public void put(K key, V value) {
        if (cache.size() >= maxSize) {
            evictLeastFrequentlyUsed();
        }
        CacheEntry<V> entry = new CacheEntry<>(value, ttlMillis);
        cache.put(key, entry);
        LOGGER.info("Cache put for key: {}, frequency: {}", key, entry.getFrequency());
        LOGGER.debug("Cache size after put: {}", cache.size());
    }


    public void remove(K key) {
        CacheEntry<V> entry = cache.remove(key);
        if (entry != null) {
            LOGGER.info("Cache removed for key: {}, frequency was: {}", key, entry.getFrequency());
            LOGGER.debug("Cache size after remove: {}", cache.size());
        }
    }


    public void clear() {
        LOGGER.info("Clearing entire cache, size before: {}", cache.size());
        cache.clear();
        LOGGER.info("Cache cleared, size after: {}", cache.size());
    }


    private void evictExpiredEntries() {
        for (K key : new HashSet<>(cache.keySet())) {
            CacheEntry<V> entry = cache.get(key);
            if (entry != null && entry.isExpired()) {
                cache.remove(key);
                LOGGER.info("Cache entry evicted due to TTL for key: {}, frequency was: {}", key, entry.getFrequency());
                LOGGER.debug("Cache size after eviction: {}", cache.size());
            }
        }
    }

    private void evictLeastFrequentlyUsed() {
        K lfuKey = null;
        int minFrequency = Integer.MAX_VALUE;

        for (Map.Entry<K, CacheEntry<V>> entry : cache.entrySet()) {
            int frequency = entry.getValue().getFrequency();
            if (frequency < minFrequency) {
                minFrequency = frequency;
                lfuKey = entry.getKey();
            }
        }

        if (lfuKey != null) {
            CacheEntry<V> entry = cache.remove(lfuKey);
            LOGGER.info("Cache overflow, evicted least frequently used key: {}, frequency: {}", lfuKey, minFrequency);
            LOGGER.debug("Cache size after LFU eviction: {}", cache.size());
        }
    }

    public int size() {
        return cache.size();
    }
}