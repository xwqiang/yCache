package ycache.eviction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * FIFO algorithm for cache elements eviction.
 * @author Roman Voropaev
 * @version 1.0
 */
public class FIFOEviction<K> implements EvictionStrategy<K> {

    private Queue<K> queue;

    public FIFOEviction(int cacheSize) {
        this.queue = new ArrayBlockingQueue<K>(cacheSize);
    }

    /**
     * Called by cache to notify about closing.
     */
    @Override
    public void notifyClear() {
        queue.clear();
    }

    /**
     * Called by cache to notify about new element.
     *
     * @param key Key of new element
     */
    @Override
    public void notifyPut(K key) {
        queue.offer(key);
    }

    /**
     * Called by cache to notify about accessing element.
     *
     * @param key
     */
    @Override
    public void notifyGet(K key) {
        // Nothing to do
    }

    /**
     * Called by cache to notify about removing element.
     *
     * @param key
     */
    @Override
    public void notifyRemove(K key) {
        queue.remove(key);
    }

    /**
     * Returns next elements to be removed according to this algorithm (LRU, LFU...).
     *
     * @return Collection of keys
     */
    @Override
    public Collection nextVictims(int count) {
        if (count > queue.size()) throw new IllegalStateException(count+" elements can't be evicted");
        List<K> res = new ArrayList<K>(count);
        for (int i = 0; i < count; i++) {
            res.add(queue.poll());
        }
        return res;
    }
}
