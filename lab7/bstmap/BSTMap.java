package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private BSTMapNode root;

    public BSTMap() {

    }
    private int size = 0;

    private class BSTMapNode {

        private K key;
        private V val;
        private BSTMapNode left = null;
        private BSTMapNode right = null;

        public BSTMapNode(K key, V val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public String toString() {
            return this.val + " " + this.key;
        }

        public BSTMapNode putHelp(K key, BSTMapNode node) {


            if (node.key.compareTo(key) == 0) {
                return node;
            } else if (node.key.compareTo(key) > 0) {
                return node.putHelp(key, node.left);
            } else {
                return node.putHelp(key, node.right);
            }

        }



        public BSTMapNode containHelp(K key, BSTMapNode node) {
            if (node == null) {
                return null;

            }

            if (node.key.compareTo(key) == 0) {
                return node;
            } else if (node.key.compareTo(key) > 0) {
                return node.putHelp(key, node.left);
            } else {
                return node.putHelp(key, node.right);
            }

        }


    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {

        BSTMapNode node = root.containHelp(key, root);
        if (node != null && node.key == key) {
            return true;
        }
        return false;
    }

    @Override
    public V get(K key) {
        BSTMapNode parentNode = root.containHelp(key, root);
        return (V) parentNode.val;


    }

    @Override
    public int size() {
        return size;
    }


    public BSTMapNode put(BSTMapNode node, K key, V value) {

        if (node == null) {
            return new BSTMapNode(key, value);
        }
        if (node.key.compareTo(key) > 0) {
            node.left = put(node.left, key, value);
        } else {
            node.right = put(node.right, key, value);
        }
        size++;
        return node;

    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        BSTMap<Integer, String> map = new BSTMap<>();
        map.put(map.root, 5, "Fuzhou");
        System.out.println(map.containsKey(6));
        System.out.println(map.containsKey(5));
        map.put(map.root,6, "Shanghai");
        map.put(map.root,4, "zhou");
        System.out.println(map.get(4));
        System.out.println(map);


    }


}
