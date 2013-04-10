import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import ycache.Cache;
import ycache.SimpleCache;

/**
 * Tests for SimpleCache class.
 */
public class SimpleCacheTest {
    @Test
    public void testGet() {
        Cache<String,Object> cache = new SimpleCache<String, Object>(5);
        cache.put("test", "test");
        cache.put("nottest", 4);
        assertEquals("test", cache.get("test"));
    }

    @Test
    public void testLRU() {
        Cache<String,Object> cache = new SimpleCache<String, Object>(50);
        for (int i = 0; i < 100; i++) {
            cache.put(String.valueOf(i), i);

        }
        assertFalse(cache.contains("3"));
        assertTrue(cache.contains("99"));

    }
}
