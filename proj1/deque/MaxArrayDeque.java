package deque;


import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> implements Iterable<T>, Comparable<MaxArrayDeque>{
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
            int cmp = c.compare(L[i], L[i+1]);
            if (cmp > 0) {
                maxIndex = i + 1;
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
            int cmp = c.compare(L[i], L[i+1]);
            if (cmp > 0) {
                maxIndex = i + 1;
            }
        }
        return L[maxIndex];
    }
    /**
    public SizeComparator<T> getItemComparator() {
        return new SizeComparator<T>();
    }

    private static class SizeComparator<T> implements Comparator<MaxArrayDeque> {
        public int compare(MaxArrayDeque o1, MaxArrayDeque o2) {
            return o1.compareTo(o2);
        }

    }

    public int compareTo(MaxArrayDeque o1) {
        return size - o1.size;
    }
    **/

    private static class ItemComparator implements Comparator<MaxArrayDeque> {


        public int compare(MaxArrayDeque o1, MaxArrayDeque o2) {
            return o1.;
        }
    }

    public int compareTo(MaxArrayDeque o, int i) {
        return this.L[i] - o.L[i];
    }

    public static void main(String[] args) {
        ItemComparator comparator = new ItemComparator();

        MaxArrayDeque<Integer> list = new MaxArrayDeque<Integer>(comparator);
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        list.addLast(40);
        list.addLast(50);

        MaxArrayDeque<Integer> list2 = new MaxArrayDeque<Integer>(comparator);
        list2.addLast(10);
        list2.addLast(20);
        list2.addLast(30);
        list2.addLast(50);
        list2.addLast(50);
        System.out.println(comparator.compare(list, list2));

    }

}
