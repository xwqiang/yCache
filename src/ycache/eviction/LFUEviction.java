package ycache.eviction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * LFU algorithm for cache elements eviction.
 * @author Roman Voropaev
 * @version 1.0
 */
public class LFUEviction<K> implements EvictionStrategy<K> {

    private Map<K,AtomicLong> lookup;

    public LFUEviction(int cacheSize) {
        this.lookup = new ConcurrentHashMap<K, AtomicLong>(cacheSize);
    }

    public LFUEviction() {
        this.lookup = new ConcurrentHashMap<K, AtomicLong>();
    }

    /**
     * Called by cache to notify about closing.
     */
    @Override
    public void notifyClear() {
        lookup.clear();
    }

    /**
     * Called by cache to notify about new element.
     *
     * @param key Key of new element
     */
    @Override
    public void notifyPut(K key) {
        lookup.put(key, new AtomicLong(0));
    }

    /**
     * Called by cache to notify about accessing element.
     *
     * @param key
     */
    @Override
    public void notifyGet(K key) {
        AtomicLong touches = lookup.get(key);
        assert touches != null;
        touches.incrementAndGet();
    }

    /**
     * Called by cache to notify about removing element.
     *
     * @param key
     */
    @Override
    public void notifyRemove(K key) {
        lookup.remove(key);
    }

    /**
     * Returns next elements to be removed according to this algorithm (LRU, LFU...).
     *
     * @return Collection of keys
     */
    @Override
    public Collection<K> nextVictims(int count) {
        if (count > lookup.size()) throw new IllegalStateException(count+" elements can't be evicted");
        List<K> res = new ArrayList<K>(count);
        Map.Entry<K,AtomicLong>[] entries = new Map.Entry[0];
        entries = lookup.entrySet().toArray(entries);
        Arrays.sort(entries, new Comparator<Map.Entry<K, AtomicLong>>() {
            @Override
            public int compare(Map.Entry<K, AtomicLong> o1, Map.Entry<K, AtomicLong> o2) {
                long v1 = o1.getValue().get();
                long v2 = o2.getValue().get();
                return v1<v2? -1 : v1>v2? 1 : 0;
            }
        });

        for (int i = 0; i < count; i++) {
            res.add(entries[i].getKey());
        }
        return res;

    }
}
