package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private int size;
    public T[] L;

    public ArrayDeque() {
        L = (T[]) new Object[8];
    }

    private void expand() {
        T[] a = (T[]) new Object[size * 2];
        System.arraycopy(L, 0, a, 0, size);
        L = a;
    }

    private void shrink() {
        T[] a = (T[]) new Object[L.length / 4];
        System.arraycopy(L, 0, a, 0, size);
        L = a;
    }
    @Override
    public void addFirst(T item) {
        if (size >= L.length) {
            expand();
        }
        T[] a = (T[]) new Object[L.length];
        System.arraycopy(L, 0, a, 1, size);
        L = a;
        L[0] = item;
        size++;
    }
    @Override
    public void addLast(T item) {
        if (size >= L.length) {
            expand();
        }
        L[size++] = item;
    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = L[0];
        T[] a = (T[]) new Object[L.length];
        System.arraycopy(L, 1, a, 0, size - 1);
        L = a;
        size--;

        double usage = (double) size / (double) L.length;
        if (L.length >= 16 && usage <= 0.25) {
            shrink();
        }
        return item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = L[size - 1];
        size--;
        L[size] = null;

        double usage = (double) size / (double) L.length;
        if (L.length >= 16 && usage <= 0.25) {
            shrink();
        }
        return item;
    }

    @Override
    public int size() {
        return size;
    }
    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return L[index];
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(L[i] + " ");
        }
        System.out.println();
    }

    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {
        int pos = 0;
        @Override
        public boolean hasNext() {
            if (pos < size) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            return L[pos++];
        }
    }

    public boolean equals(Object o) {
        if (o instanceof ArrayDeque) {
            ArrayDeque<T> newO = (ArrayDeque<T>) o;
            if (this.size != newO.size) {
                return false;
            }
            int pos = 0;
            for (T item : this) {
                if (item != newO.L[pos]) {
                    return false;
                }
                pos++;
            }
            return true;
        }
        return false;
    }
    public static void main(String[] args) {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        for (int i = 0; i < 1000; i++) {
            L.addLast(0);
        }
        for (int i = 0; i < 1000; i++) {
            L.removeFirst();
        }


        ArrayDeque<Integer> L2 = new ArrayDeque<>();
        L2.addFirst(10);
        L2.addFirst(20);
        L2.addLast(30);

        for (int item : L) {
            System.out.println(item);
        }
        System.out.println(L.equals(L2));

        ArrayDeque<Integer> L3 = new ArrayDeque<>();
        System.out.println(L3.removeLast());

    }

}
