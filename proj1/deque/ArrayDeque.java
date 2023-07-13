package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T>{
    public int size;
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
        T item = L[0];
        T[] a = (T[]) new Object[L.length];
        System.arraycopy(L, 1, a, 0, size - 1);
        L = a;
        size--;

        int InverseUsage = L.length / size;
        if (L.length >= 16 && InverseUsage >= 4) {
            shrink();
        }
        return item;
    }

    @Override
    public T removeLast() {
        T item = L[size - 1];
        size--;
        L[size] = null;
        if (size > 0) {
            int InverseUsage = L.length / size;
            if (L.length >= 16 && InverseUsage >= 4) {
                shrink();
            }
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
        if (o instanceof ArrayDeque && this.equals(o)) {
            return true;
        }
        return false;
    }
    public static void main(String[] args) {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addFirst(10);
        L.addFirst(20);
        L.addLast(30);
        L.addFirst(10);
        L.addFirst(20);
        L.addLast(30);
        L.addFirst(10);
        L.addFirst(20);
        L.addLast(30);
        L.addFirst(10);
        L.addFirst(20);
        L.addLast(30);
        L.addFirst(10);
        L.addFirst(20);
        L.addLast(30);
        L.addFirst(10);
        L.addFirst(20);
        L.addLast(30);

        for (int item : L) {
            System.out.println(item);
        }

    }

}
