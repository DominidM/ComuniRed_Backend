package com.comunired.shared.infrastructure.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimedCache<K, V> {

    private final long ttlMillis;
    private final ConcurrentHashMap<K, Entry<V>> cache = new ConcurrentHashMap<>();

    public TimedCache(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    public V get(K key) {
        Entry<V> entry = cache.get(key);
        if (entry == null) return null;
        if (System.currentTimeMillis() - entry.timestamp > ttlMillis) {
            cache.remove(key);
            return null;
        }
        return entry.value;
    }

    public V computeIfAbsent(K key, java.util.function.Function<? super K, ? extends V> loader) {
        V existing = get(key);
        if (existing != null) return existing;
        V loaded = loader.apply(key);
        if (loaded != null) {
            cache.put(key, new Entry<>(loaded, System.currentTimeMillis()));
        }
        return loaded;
    }

    public void put(K key, V value) {
        cache.put(key, new Entry<>(value, System.currentTimeMillis()));
    }

    public void invalidate(K key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    private record Entry<V>(V value, long timestamp) {}
}
