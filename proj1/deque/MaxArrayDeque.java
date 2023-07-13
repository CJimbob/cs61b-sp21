package deque;

import net.sf.saxon.functions.Minimax;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> implements Comparable<MaxArrayDeque>, Deque<T>{
    private Comparator<T> c;


    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.c = c;
    }

    public T max() {
        int maxIndex = 0;

        if (size == 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            int cmp = c.compare(L[maxIndex], L[i]);
            if (cmp < 0) {
                maxIndex = i;
            }
        }
        return L[maxIndex];
    }

    public T max(Comparator<T> c) {
        int maxIndex = 0;

        if (size == 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            int cmp = c.compare(L[maxIndex], L[i]);
            if (cmp < 0) {
                maxIndex = i;
            }
        }
        return L[maxIndex];
    }

    public static void main(String[] args) {

        ItemComparator itemComparator = getItemComparator();
        MaxArrayDeque<Integer> L = new MaxArrayDeque<>(itemComparator);
        MaxArrayDeque<Integer> L2 = new MaxArrayDeque<>(itemComparator);
        L.addLast(0);
        L.addLast(1);
        L.addLast(10);
        L.addLast(4);
        L.addLast(8);
        L.addLast(9);
        L.addLast(3);

        L2.addLast(0);
        L2.addLast(1);
        L2.addLast(12130);
        L2.addLast(4);
        L2.addLast(8);
        L2.addLast(402);
        L2.addLast(32);
        L2.addLast(32);
        System.out.println(L2.max(itemComparator));
        System.out.println(L.max());


        SizeComparator sizeComparator = new SizeComparator();

        System.out.println(sizeComparator.compare(L, L2));

    }

    @Override
    public int compareTo(MaxArrayDeque o) {
        return this.size - o.size;
    }

    private static class SizeComparator implements Comparator<MaxArrayDeque> {
        @Override
        public int compare(MaxArrayDeque o1, MaxArrayDeque o2) {
            return o1.compareTo(o2);
        }
    }

    public static SizeComparator getSizeComparator() {
        return new SizeComparator();
    }


    private static class ItemComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }

    public static ItemComparator getItemComparator() {
        return new ItemComparator();
    }


}
