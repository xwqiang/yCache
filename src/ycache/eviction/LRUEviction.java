package ycache.eviction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * LRU algorithm for cache elements eviction.
 * @author Roman Voropaev
 * @version 1.0
 */
public class LRUEviction<K> implements EvictionStrategy<K> {

    private Queue<LRUEntry> queue;
    private Map<K,LRUEntry> lookup;

    public LRUEviction(int cacheSize) {
        this.queue = new PriorityBlockingQueue<LRUEntry>(cacheSize);
        this.lookup = new ConcurrentHashMap<K, LRUEntry>(cacheSize);
    }

    public LRUEviction() {
        this.queue = new PriorityBlockingQueue<LRUEntry>();
        this.lookup = new ConcurrentHashMap<K, LRUEntry>();
    }

    /**
     * Called by cache to notify about closing.
     */
    @Override
    public void notifyClose() {
        queue.clear();
        lookup.clear();
    }

    /**
     * Called by cache to notify about new element.
     *
     * @param key Key of new element
     */
    @Override
    public void notifyPut(K key) {
        long now = System.currentTimeMillis();
        LRUEntry entry = new LRUEntry(key, now);
        queue.offer(entry);
        lookup.put(key, entry);
    }

    /**
     * Called by cache to notify about accessing element.
     *
     * @param key
     */
    @Override
    public void notifyGet(K key) {
        // Block. queue update = remove & insert again
        LRUEntry entry = lookup.get(key);
        assert entry != null;
        boolean removed = queue.remove(entry);
        assert removed;
        entry.timestamp = System.currentTimeMillis();
        boolean added = queue.add(entry);
        assert added;
    }

    /**
     * Called by cache to notify about removing element.
     *
     * @param key
     */
    @Override
    public void notifyRemove(K key) {
        boolean removed = queue.remove(lookup.remove(key));
        assert removed;
    }

    /**
     * Returns next elements to be removed according to this algorithm (LRU, LFU...).
     *
     * @return Collection of keys
     */
    @Override
    public Collection<K> nextVictims(int count) {
        if (count > queue.size()) throw new IllegalStateException(count+" elements can't be evicted");
        List<K> res = new ArrayList<K>(count);
        for (int i = 0; i < count; i++) {
            res.add(queue.poll().key);
        }
        return res;
    }

    private class LRUEntry implements Comparable<LRUEntry> {
        long timestamp;
        K key;

        public LRUEntry(K key, long timestamp) {
            this.timestamp = timestamp;
            this.key = key;
        }

        @Override
        public int compareTo(LRUEntry o) {
            if (timestamp < o.timestamp) return -1;
            if (timestamp > o.timestamp) return 1;
            return 0;
        }
    }

}
