package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private double bucketNum = 16;
    private double itemNum = 0;
    private double maxLoad = 0.75;

    private HashSet<K> keySet = new HashSet<>();

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = createTable((int) bucketNum);
    }

    public MyHashMap(int initialSize) {
        bucketNum = initialSize;
        buckets = createTable((int) bucketNum);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        bucketNum = initialSize;
        this.maxLoad = maxLoad;
        buckets = createTable((int) bucketNum);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        buckets = createTable((int) bucketNum);
        keySet = new HashSet<>();
        itemNum = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    @Override
    public V get(K key) {
        int index = getIndex(key.hashCode());
        if (buckets[index] == null) {
            buckets[index] = createBucket();
        }
        Collection<Node> list = buckets[index];
        for (Node node : list) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return (int) itemNum;
    }

    @Override
    public void put(K key, V value) {
        double loadFactor = itemNum / bucketNum;
        if (loadFactor >= maxLoad) {
            resize();
        }
        int index = getIndex(key.hashCode());
        if (buckets[index] == null) {
            buckets[index] = createBucket();
        }
        Collection<Node> list = buckets[index];
        if (keySet.contains(key)) {
            for (Node node : list) {
                if (node.key.equals(key)) {
                    node.value = value;
                }
            }
        } else {
            Node newNode = new Node(key, value);
            keySet.add(key);
            list.add(newNode);
            itemNum++;
        }
    }

    private void put(Collection<Node>[] buckets, K key, V value) {
        int index = getIndex(key.hashCode());
        if (buckets[index] == null) {
            buckets[index] = createBucket();
        }
        Collection<Node> list = buckets[index];
        Node newNode = createNode(key, value);
        list.add(newNode);
    }

    private void resize() {
        bucketNum *= 2;
        Collection<Node>[] newBuckets = createTable((int) bucketNum);
        for (int i = 0; i < bucketNum / 2; i++) {
            if (buckets[i] != null) {
                Collection<Node> OriList = buckets[i];
                for (Node node : OriList) {
                    put(newBuckets, node.key, node.value);
                }
            }
        }
        buckets = newBuckets;
    }

    private int getIndex(int hashCode) {
         return Math.floorMod(hashCode, (int) bucketNum);
    }


    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }


    public static void main(String[] args) {
        MyHashMap<Integer, String> hashMap = new MyHashMap<>();
        hashMap.put(1, "Hi");
        hashMap.put(109, "No");
        hashMap.put(1210, "i");
        hashMap.put(1032121, "Ba");
        hashMap.put(10324, "po");
        hashMap.put(102, "Li");
        hashMap.put(11, "HW");
        hashMap.put(332121, "La");
        hashMap.put(3324, "poq");
        hashMap.put(32, "LQi");
        hashMap.put(1, "HWW");
        hashMap.put(2032121, "BAa");
        hashMap.put(20324, "pXo");
        hashMap.put(202, "LCi");
        hashMap.put(1, "HWZ");
    }
}
