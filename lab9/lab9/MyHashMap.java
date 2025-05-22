package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int index = hash(key);
        if (buckets[index].size() == 0) {
            return null;
        }
        return buckets[index].get(key);
        //throw new UnsupportedOperationException();
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (buckets[index] == null) {
            buckets[index] = new ArrayMap<>();
        }
        if (buckets[index].containsKey(key)) {
            buckets[index].put(key, value);
            return;
        } else {
            if (loadFactor() >= MAX_LF) {
                resize(buckets.length * 2);
            }
            buckets[index].put(key, value);
            size += 1;
        }
        //throw new UnsupportedOperationException();
    }
    private void resize(int newSize) {
        ArrayMap<K, V>[] newBuckets = new ArrayMap[newSize];
        for (int i = 0; i < newSize; i += 1) {
            newBuckets[i] = new ArrayMap<>();
        }
        for (int i = 0; i < buckets.length; i += 1) {
            if (buckets[i].size() > 0) {
                for (K key : buckets[i].keySet()) {
                    int index = Math.floorMod(key.hashCode(), newSize);
                    newBuckets[index].put(key, buckets[i].get(key));
                }
            }
        }
        this.buckets = newBuckets;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
        //throw new UnsupportedOperationException();
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (int i = 0; i < buckets.length; i += 1) {
            if (buckets[i].size() > 0) {
                for (K key : buckets[i].keySet()) {
                    keySet.add(key);
                }
            }
        }
        return keySet;
        // throw new UnsupportedOperationException();
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int index = hash(key);
        for (K key2 : buckets[index]) {
            if (key.equals(key2)) {
                V value = buckets[index].get(key);
                buckets[index].remove(key);
                size -= 1;
                return value;
            }
        }
        return null;
        //throw new UnsupportedOperationException();
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        int index = hash(key);
        for (K key2 : buckets[index]) {
            if (key.equals(key2)) {
                V value2 = buckets[index].get(key);
                if (value.equals(value2)) {
                    buckets[index].remove(key);
                    size -= 1;
                    return value;
                }
            }
        }
        return null;
        //throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
