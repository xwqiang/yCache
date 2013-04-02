import static org.junit.Assert.assertEquals;
import org.junit.Test;
import ycache.Cache;
import ycache.SimpleCache;

/**
 * Tests for SimpleCache class.
 */
public class SimpleCacheTest {
    @Test
    public void testGet() {
        Cache<String,Object> cache = new SimpleCache<String, Object>();
        cache.put("test", "test");
        assertEquals("test",cache.get("test"));
    }
}
