package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {

    public AnyNode sentinel;
    int size;



    private class AnyNode {
        public T item;
        public AnyNode next;
        public AnyNode prev;

        public AnyNode(T item, AnyNode next, AnyNode prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    public LinkedListDeque() {
        sentinel =  new AnyNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public LinkedListDeque(LinkedListDeque<T> other) {
        sentinel =  new AnyNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;

        T item;
        AnyNode n = sentinel.next;
        for (int i = 0; i < other.size(); i++) {
           item = other.get(i);
           addFirst(item);
        }

    }

    @Override
    public void addFirst(T item) {
        sentinel.next.prev = new AnyNode(item, sentinel.next, sentinel);
        sentinel.next = sentinel.next.prev;

        size++;
    }

    @Override
    public void addLast(T item) {
        sentinel.prev.next = new AnyNode(item, sentinel, sentinel.prev);
        sentinel.prev = sentinel.prev.next;

        size++;
    }

    @Override
    public void printDeque() {
        AnyNode n = sentinel.next;

        while (n != sentinel) {
            System.out.print(n.item + " ");
            n = n.next;
        }
        System.out.println();
    }

    @Override
    public int size() {
        return size;
    }
    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }

        T item = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;

        size--;
        return item;
    }
    @Override
    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }
        T item = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;

        size--;
        return item;
    }

    @Override
    public T get(int index) {
        AnyNode n = sentinel.next;
        if (index >= this.size() || index < 0 || n == sentinel) {
            return null;
        }

        while (index > 0 && n != sentinel) {
            n = n.next;
            index--;
        }
        return n.item;
    }


    private AnyNode getNode(int index) {
        AnyNode n;
        if (index > 0) {
            n = getNode(index - 1).next;
        } else {
            return sentinel.next;
        }
        return n;
    }


    public T getRecursive(int index) {
        return getNode(index).item;
    }

    public Iterator<T> iterator() {
      return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        AnyNode list = sentinel.next;
        @Override
        public boolean hasNext() {
            if (list.next != sentinel) {
                return true;
            }
            return false;
        }
        @Override
        public T next() {
            T item = list.item;
            list = list.next;
            return item;
        }
    }

    public boolean equals(Object o) {


        if (o instanceof LinkedListDeque) {
            LinkedListDeque<T> newO = (LinkedListDeque<T>) o;
            if (this.size != newO.size) {
                return false;
            }
            AnyNode n1 = newO.sentinel.next;
            for (T item : this) {
                if (item != n1.item) return false;
                n1 = n1.next;
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        L.addLast(10);
        L.addFirst(10);
        L.addFirst(20);
        L.addFirst(30);

        LinkedListDeque<Integer> L2 = new LinkedListDeque<>();
        L2.addLast(10);
        L2.addFirst(10);
        L2.addFirst(20);
        L2.addFirst(30);


        System.out.println(L.equals(L2));


    }




}
