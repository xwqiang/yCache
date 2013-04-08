package ycache.eviction;

import java.util.*;

/**
 * This algorithm randomly chooses elements for eviction.
 *
 * @author Roman Voropaev
 */
public class RandomEviction<K> implements EvictionStrategy<K> {

    private List<K> keys;
    private boolean closed = false;

    public RandomEviction(int cacheSize) {
        this.keys = new ArrayList<K>(cacheSize);
    }

    /**
     * Called by cache to notify about closing.
     */
    @Override
    public void notifyClose() {
        closed = true;
        keys.clear();
    }

    /**
     * Called by cache to notify about new element.
     *
     * @param key Key of new element
     */
    @Override
    public void notifyPut(K key) {
        keys.add(key);
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
        keys.remove(key);
    }

    /**
     * Returns next elements to be removed according to this algorithm (LRU, LFU...).
     *
     * @return Collection of keys
     */
    @Override
    public Collection nextVictims(int count) {
        if (closed || count > keys.size()) throw new IllegalStateException(count+" elements can't be evicted");
        int m = count;
        Random rnd = new Random();
        // Floyd algorithm O(m)
        List<K> res = new ArrayList<K>(count);
        int n = keys.size();
        for(int i = n-m; i<n; i++){
            int pos = rnd.nextInt(i+1);
            K item = keys.get(pos);
            if (res.contains(item))
                res.add(keys.get(i));
            else
                res.add(item);
        }
        return res;
    }
}
