package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class testThreeAddThreeRemove {
    @Test
    public void test1() {
        AListNoResizing<Integer> List1 = new AListNoResizing<>();
        BuggyAList<Integer> List2 = new BuggyAList<>();
        boolean flag = true;
        List1.addLast(0);
        List1.addLast(1);
        List1.addLast(2);

        List2.addLast(0);
        List2.addLast(1);
        List2.addLast(2);
        if (List1.size() != List2.size())  flag = true;
        for (int i = 0; i < List1.size(); i++) {
            if (List1.get(i) != List2.get(i)) flag = false;
        }

        assertEquals(true, flag);


    }
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> L2 = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                L2.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size1 = L.size();
                int size2 = L2.size();
                assertEquals(size1, size2);
                System.out.println("size: " + size1 + " " + size2);
            } else {
                if (L.size() > 0) {
                    assertEquals(L.getLast(), L2.getLast());
                    System.out.println("getLast(): " + L.getLast() + " " + L2.getLast());
                    int l1 = L.removeLast();
                    int l2 = L2.removeLast();
                    assertEquals(l1, l2);
                    System.out.println("removeLast(): " + l1 + " " + l2);
                }

            }
        }
    }



}
