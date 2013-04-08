package ycache.eviction;

import java.util.Collection;

/**
 * LFU algorithm for cache elements eviction.
 * @author Roman Voropaev
 * @version 1.0
 */
public class LFUEviction<K> implements EvictionStrategy<K> {

    private
    /**
     * Called by cache to notify about closing.
     */
    @Override
    public void notifyClose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Called by cache to notify about new element.
     *
     * @param key Key of new element
     */
    @Override
    public void notifyPut(K key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Called by cache to notify about accessing element.
     *
     * @param key
     */
    @Override
    public void notifyGet(K key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Called by cache to notify about removing element.
     *
     * @param key
     */
    @Override
    public void notifyRemove(K key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Returns next elements to be removed according to this algorithm (LRU, LFU...).
     *
     * @return Collection of keys
     */
    @Override
    public Collection<K> nextVictims(int count) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
