package ycache;

import java.util.Collection;
import java.util.Set;

/**
 * Main Cache interface.
 * Supports common operations on cache.
 * May be extended in future.
 * @author Roman Voropaev
 * @version 1.0
 *
 */
public interface Cache<K, V> {
    /**
     * Puts element to cache.
     * @param key Key
     * @param value Value
     */
    void put(K key, V value);

    /**
     * Returns element from cache.
     * @param key Value key
     * @return Cached value for given key
     */
    V get(K key);

    /**
     * Removes element from cache.
     * @param key Key of value to be removed
     */
    void remove(K key);

    /**
     * Close cache, remove allocated resources.
     */
    void close();

    /**
     * Get current count of cached elements.
     * @return Current count of cached elements
     */
    long size();

    /**
     * Get set of keys from this cache.
     * @return Keys as a set
     */
    Set<K> keys();

    /**
     * Get set of values from this cache.
     * @return Values as a set
     */
    Collection<V> values();

    /**
     * Free space by evicting {@code count} elements.
     * @param count Number of elements
     */
    void free(int count);

    /**
     * Get element without touching cache access stats.
     * @param key Key
     * @return Cached value for given key
     */
    V getQuiet(K key);

    /**
     * Check if cache contains element.
     * @param key Key
     * @return true if cache contains element, false otherwise
     */
    boolean contains(K key);

    /**
     * Put element to cache only if it doesn't already contain it.
     * @param key Element key
     * @param value Element value
     * @return True if element was put to cache, false if cache already contains it
     */
    boolean putIfAbsent(K key, V value);
}
