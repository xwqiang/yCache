import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import ycache.Cache;
import ycache.SimpleCache;
import ycache.eviction.FIFOEviction;
import ycache.eviction.LFUEviction;
import ycache.eviction.RandomEviction;

/**
 * Tests for SimpleCache class.
 */
public class SimpleCacheTest {

    @Test
    public void testLRU() throws InterruptedException {
        System.out.print("\nTesting LRU\n");
        Cache<String,Object> cache = new SimpleCache<String, Object>(50);
        for (int i = 0; i < 50; i++) {
            cache.put(String.valueOf(i), i);

        }
        for (int i = 50; i > 0; i--) {
            cache.get(String.valueOf(i));
            Thread.sleep(10);
        }
        for (int i = 50; i < 100; i++) {
            cache.put(String.valueOf(i), i);
        }
        assertFalse(cache.contains("3"));
        assertTrue(cache.contains("99"));
        assertTrue(cache.size() <= 50);
    }

    @Test
    public void testRandom() {
        System.out.print("\nTesting random\n");
        Cache<String,Object> cache = new SimpleCache<String, Object>(50, new RandomEviction<String>(50));
        for (int i = 0; i < 100; i++) {
            cache.put(String.valueOf(i), i);
        }
    }

    @Test
    public void testFIFO() {
        System.out.print("\nTesting FIFO\n");
        Cache<String,Object> cache = new SimpleCache<String, Object>(50, new FIFOEviction<String>(50));
        for (int i = 0; i < 100; i++) {
            cache.put(String.valueOf(i), i);
        }
    }

    @Test
    public void testLFU() {
        System.out.print("\nTesting LFU\n");
        Cache<String,Object> cache = new SimpleCache<String, Object>(50, new LFUEviction<String>(50));
        for (int i = 0; i < 50; i++) {
            cache.put(String.valueOf(i), i);
        }
        for (int i = 0; i < 50; i = i+2) {
            cache.get(String.valueOf(i));
        }
        for (int i = 50; i < 100; i++) {
            cache.put(String.valueOf(i), i);
        }
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        // not good test, but at least something
        System.out.print("\nTesting concurrent access\n");
        final Cache<String,Object> cache = new SimpleCache<String, Object>(100);
        long start = System.nanoTime();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    cache.put(String.valueOf(i), i);
                }
                for (int i = 0; i < 100; i++) {
                    cache.get(String.valueOf(i));
                }
            }
        });
        t.start(); t.join();
        long end = System.nanoTime();
        System.out.printf("1 Thread - %d ns %n", end-start);

        cache.clear();
        start = System.nanoTime();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 50; i++) {
                    cache.put(String.valueOf(i), i);
                }
                for (int i = 0; i < 50; i++) {
                    cache.get(String.valueOf(i));
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.nanoTime();
                for (int i = 50; i < 100; i++) {
                    cache.put(String.valueOf(i), i);
                }
                for (int i = 50; i < 100; i++) {
                    cache.get(String.valueOf(i));
                }
            }
        });
        t1.start(); t2.start();
        t1.join(); t2.join();
        end = System.nanoTime();
        System.out.printf("2 Threads - %d ns %n", end-start);
    }
}
