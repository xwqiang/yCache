package ycache;

import org.apache.log4j.Logger;
import ycache.eviction.EvictionStrategy;
import ycache.eviction.LRUEviction;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic in-memory heap-based cache implementation.
 * It is non-persistant, thread-safe.
 *
 * @author Roman Voropaev
 * @version 1.0
 */
public class SimpleCache<K,V> implements Cache<K,V>{

    // Logging
    private final Logger LOG = Logger.getLogger(SimpleCache.class);

    // Map contains cache data.
    private final Map<K,V> map;

    // Cleaner to free space by eviction
    private final EvictionStrategy<K> cleaner;
    // How many elements will be evicted at once on put
    private static final int CLEAN_STRIDE = 10;

    // Current cache size
    private long size = 0;
    // Number of puts to cache
    private long puts = 0;
    // Number of gets from cache
    private long gets = 0;
    // Cache misses
    private long misses = 0;
    // Cache hits
    private long hits;

    // Max size of cache. Cache can't be larger.
    private long maxSize;

    /**
     * Constructor that supports specifying eviction strategy.
     *
     * @param maxSize Max size of cache
     * @param eviction Eviction strategy
     */
    public SimpleCache(int maxSize, EvictionStrategy<K> eviction) {
        this.map = new ConcurrentHashMap<K, V>(maxSize);
        this.cleaner = eviction;
        this.maxSize = maxSize;
        LOG.info(String.format("Cache object created (maxSize=%d,stratagy=%s)",
                maxSize, eviction.getClass().getName() ));
    }

    /**
     * Default constructor. Uses LRU algorithm by default.
     *
     * @param maxSize Size of cache
     */
    public SimpleCache(int maxSize) {
        this(maxSize, new LRUEviction<K>(maxSize));
    }

    /**
     * Puts element to cache.
     *
     * @param key   Key
     * @param value Value
     */
    @Override
    public void put(K key, V value) {
        cleaner.notifyPut(key);
        puts++;
        size++;
        if (size >= maxSize) {
            // Should be cleaned
            free((int)(CLEAN_STRIDE < size? CLEAN_STRIDE : size));
        }
        map.put(key, value);
        if (key!=null && value != null)
            LOG.debug(String.format("Element (%s:%s) was inserted to cache", key.toString(), value.toString()));
    }

    /**
     * Returns element from cache.
     *
     * @param key Value key
     * @return Cached value for given key
     */
    @Override
    public V get(K key) {
        gets++;
        if (!map.containsKey(key))
            misses++;
        else {
            hits++;
            cleaner.notifyGet(key);
        }
        V value = map.get(key);
        if (key!=null && value != null)
            LOG.debug(String.format("Element (%s:%s) was accessed in cache", key.toString(), value.toString()));
        return value;
    }

    /**
     * Removes element from cache.
     *
     * @param key Key of value to be removed
     */
    @Override
    public void remove(K key) {
        cleaner.notifyRemove(key);
        V value = map.remove(key);
        size--;
        if (key!=null && value != null)
            LOG.debug(String.format("Element (%s:%s) was removed from cache", key.toString(), value.toString()));
    }

    /**
     * Clear cache
     */
    @Override
    public void clear() {
        cleaner.notifyClear();
        map.clear();
        size = 0;
        LOG.debug("Cache was closed");
    }

    /**
     * Get current count of cached elements.
     *
     * @return Current count of cached elements
     */
    @Override
    public long size() {
        return size;
    }

    /**
     * Get set of keys from this cache.
     *
     * @return Keys as a set
     */
    @Override
    public Set<K> keys() {
        return map.keySet();
    }

    /**
     * Get set of values from this cache.
     *
     * @return Values collection
     */
    @Override
    public Collection<V> values() {
        return map.values();
    }

    /**
     * Free space by evicting {@code count} elements.
     *
     * @param count Number of elements
     */
    @Override
    public void free(int count) {
        Collection<K> victims = cleaner.nextVictims(count);
        LOG.debug("Elements " + victims + " will be killed");
        for (K key : victims) {
            remove(key);
        }
    }

    /**
     * Get element without touching cache access stats.
     *
     * @param key Key
     * @return Cached value for given key
     */
    @Override
    public V getQuiet(K key) {
        return map.get(key);
    }

    /**
     * Check if cache contains element.
     *
     * @param key Key
     * @return true if cache contains element, false otherwise
     */
    @Override
    public boolean contains(K key) {
        return map.containsKey(key);
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
        synchronized (map) {
            if (map.containsKey(key)) return false;
            map.put(key, value);
        }
        cleaner.notifyPut(key);
        puts++;
        size++;
        LOG.debug(String.format("Element (%s:%s) was inserted to cache", key.toString(), value.toString()));
        return true;
    }

    public long getPuts() {
        return puts;
    }

    public long getGets() {
        return gets;
    }

    public long getMisses() {
        return misses;
    }

    public long getHits() {
        return hits;
    }
}
