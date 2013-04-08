package ycache.eviction;

import java.util.Collection;

/**
 * This interface defines algorithm for cache elements eviction.
 * @author Roman Voropaev
 * @version 1.0
 */
public interface EvictionStrategy<K> {

    /**
     * Called by cache to notify about closing.
     */
    void notifyClose();

    /**
     * Called by cache to notify about new element.
     * @param key Key of new element
     */
    void notifyPut(K key);

    /**
     * Called by cache to notify about accessing element.
     * @param key
     */
    void notifyGet(K key);

    /**
     * Called by cache to notify about removing element.
     * @param key
     */
    void notifyRemove(K key);

    /**
     * Returns next elements to be removed according to this algorithm (LRU, LFU...).
     * @return Collection of keys
     */
    Collection<K> nextVictims(int count);
}
