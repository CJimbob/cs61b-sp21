package deque;

import java.util.Iterator;

public class ArrayDeque<T> {

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
    public void addLast(T item) {
        if (size >= L.length) {
            expand();
        }
        L[size++] = item;
    }

    public T removeFirst() {
        T item = L[0];
        T[] a = (T[]) new Object[L.length];
        System.arraycopy(L, 1, a, 0, size);
        L = a;
        size--;

        int InverseUsage = L.length / size;
        if (L.length >= 16 && InverseUsage >= 4) {
            shrink();
        }
        return item;
    }


    public T removeLast() {
        T item = L[size - 1];
        size--;
        L[size] = null;

        int InverseUsage = L.length / size;
        if (L.length >= 16 && InverseUsage >= 4) {
            shrink();
        }

        return item;
    }


    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }
    public int size() {
        return size;
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return L[index];
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(L[i] + " ");
        }
        System.out.println();
    }

    public Iterator<T> iterator() {
        return new ArrayDeque.MaxArrayIterator();
    }

    private class MaxArrayIterator implements Iterator<T> {
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
        System.out.println(L.removeFirst());
        System.out.println(L.removeLast());
        System.out.println(L.removeFirst());
        System.out.println(L.removeLast());
        System.out.println(L.removeFirst());
        System.out.println(L.removeLast());
        System.out.println(L.removeFirst());
        System.out.println(L.removeLast());
        System.out.println(L.removeFirst());
        System.out.println(L.removeLast());
        System.out.println(L.removeFirst());
        System.out.println(L.removeLast());
        System.out.println();
    }

}
