package net.segoia.util.data;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Simple LRU cache implementation based on LinkedHashMap
 * @author adi
 *
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    /**
     * 
     */
    private static final long serialVersionUID = 2179632799549660511L;
    
    /** 
     * Maximum number of items in the cache. 
     */
    private int capacity;

    public LRUCache(int capacity, float loadFactor) {
	/* call super constructor with access order set to true */
	super(capacity, loadFactor, true); 
	this.capacity = capacity;
    }
    
    public LRUCache(int capacity) {
	this(capacity,0.75f);
    }

    protected boolean removeEldestEntry(Entry<K, V> entry) {
	return (size() > capacity);
    }
}