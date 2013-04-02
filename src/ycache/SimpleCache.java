package ycache;

import java.util.Set;

/**
 * Basic in-memory heap-based cache implementation.
 * It is non-persistant, thread-safe.
 * @author Roman Voropaev
 * @version 1.0
 */
public class SimpleCache<K,V> implements Cache<K,V>{
    /**
     * Puts element to cache.
     *
     * @param key   Key
     * @param value Value
     */
    @Override
    public void put(K key, V value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Returns element from cache.
     *
     * @param key Value key
     * @return Cached value for given key
     */
    @Override
    public V get(K key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Removes element from cache.
     *
     * @param key Key of value to be removed
     */
    @Override
    public void remove(K key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Close cache, remove allocated resources.
     */
    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Get current count of cached elements.
     *
     * @return Current count of cached elements
     */
    @Override
    public long size() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Get set of keys from this cache.
     *
     * @return Keys as a set
     */
    @Override
    public Set keys() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Get set of values from this cache.
     *
     * @return Values as a set
     */
    @Override
    public Set values() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Free space by evicting {@code count} elements.
     *
     * @param count Number of elements
     */
    @Override
    public void free(long count) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Get element without touching cache access stats.
     *
     * @param key Key
     * @return Cached value for given key
     */
    @Override
    public V getQuiet(K key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Check if cache contains element.
     *
     * @param key Key
     * @return true if cache contains element, false otherwise
     */
    @Override
    public boolean contains(K key) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Put element to cache only if it doesn't already contain it.
     *
     * @param key   Element key
     * @param value Element value
     * @return True if element was put to cache, false if cache already contains it
     */
    @Override
    public boolean putIfAbsent(K key, V value) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
