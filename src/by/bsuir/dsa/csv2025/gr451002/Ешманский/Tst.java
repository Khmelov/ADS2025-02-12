package by.bsuir.dsa.csv2025.gr451002.Ешманский;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tst {



    @Test
    public void testBasicOperations() {
        int[] array = {1, 2, 3, 4, 5};
        Solution treap = new Solution(array);

        assertEquals(5, treap.size());
        assertEquals(1, treap.get(0));
        assertEquals(5, treap.get(4));

        treap.insert(2, 10); // [1,2,10,3,4,5]
        assertEquals(10, treap.get(2));

        treap.delete(3); // [1,2,10,4,5]
        assertEquals(4, treap.get(3));

        treap.update(0, 100); // [100,2,10,4,5]
        assertEquals(100, treap.get(0));
    }

    @Test
    public void testLargeData() {
        int n = 100_000;
        Solution treap = new Solution();
        for (int i = 0; i < n; i++) treap.insert(i, i);

        assertEquals(n, treap.size());
        for (int i = 0; i < n; i += 5000) {
            assertEquals(i, treap.get(i));
        }

        // Массовое обновление
        for (int i = 0; i < n; i += 10000) {
            treap.update(i, i + 1);
            assertEquals(i + 1, treap.get(i));
        }
    }
}
