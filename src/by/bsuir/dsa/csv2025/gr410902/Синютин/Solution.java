package by.bsuir.dsa.csv2025.gr410902.Синютин;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Solution {

    static class DynamicStream {
        private final List<Integer> data = new ArrayList<>();

        // Вставка элемента по индексу
        public void insert(int index, int value) {
            data.add(index, value);
        }

        // Удаление элемента по индексу
        public void delete(int index) {
            data.remove(index);
        }

        // Сумма элементов в диапазоне [start, end] включительно
        public long query(int start, int end) {
            long sum = 0;
            // Проходим от start до end включительно
            for (int i = start; i <= end; i++) {
                sum += data.get(i);
            }
            return sum;
        }
    }

    private DynamicStream ds;

    @BeforeEach
    void setUp() {
        ds = new DynamicStream();
    }

    @Test
    public void test1_BasicAppend() {
        ds.insert(0, 10);
        ds.insert(1, 20);
        ds.insert(2, 30);
        // Ожидаем: 10 + 20 + 30 = 60
        assertEquals(60, ds.query(0, 2), "Test 1 Failed: Sum should be 60");
    }

    @Test
    public void test2_ReverseInsert() {
        ds.insert(0, 1);
        ds.insert(0, 2); // [2, 1]
        ds.insert(0, 3); // [3, 2, 1]
        assertEquals(6, ds.query(0, 2), "Test 2 Failed: Sum should be 6");
    }

    @Test
    public void test3_DeleteMiddle() {
        ds.insert(0, 10);
        ds.insert(1, 20);
        ds.insert(2, 30);
        ds.delete(1); // Удаляем 20 -> [10, 30]
        assertEquals(40, ds.query(0, 1), "Test 3 Failed: Total sum mismatch");
        assertEquals(30, ds.query(1, 1), "Test 3 Failed: Element shift mismatch");
    }

    @Test
    public void test4_NegativeNumbers() {
        ds.insert(0, 50);
        ds.insert(1, -100);
        ds.insert(2, 50);
        // 50 + (-100) + 50 = 0
        assertEquals(0, ds.query(0, 2), "Test 4 Failed: Total sum should be 0");
        assertEquals(-100, ds.query(1, 1), "Test 4 Failed: Middle element mismatch");
    }

    @Test
    public void test5_DeleteEnds() {
        ds.insert(0, 1);
        ds.insert(1, 2);
        ds.insert(2, 3);
        ds.insert(3, 4);
        ds.delete(3); // [1, 2, 3]
        ds.delete(0); // [2, 3]
        assertEquals(5, ds.query(0, 1), "Test 5 Failed: Sum should be 5");
    }

    @Test
    public void test6_InsertMiddleSplit() {
        ds.insert(0, 10);
        ds.insert(1, 30);
        ds.insert(1, 20); // Вклиниваем 20 между 10 и 30 -> [10, 20, 30]
        assertEquals(60, ds.query(0, 2), "Test 6 Failed: Total sum mismatch");
        assertEquals(20, ds.query(1, 1), "Test 6 Failed: Middle element mismatch");
    }

    @Test
    public void test7_EmptyAndRefill() {
        ds.insert(0, 777);
        assertEquals(777, ds.query(0, 0));
        ds.delete(0);
        ds.insert(0, 555);
        assertEquals(555, ds.query(0, 0), "Test 7 Failed: Refill mismatch");
    }

    @Test
    public void test8_SubRange() {
        ds.insert(0, 1);
        ds.insert(1, 2);
        ds.insert(2, 3);
        ds.insert(3, 4);
        ds.insert(4, 5); // [1, 2, 3, 4, 5]
        // Сумма с индекса 1 по 3 (значения 2, 3, 4) = 9
        assertEquals(9, ds.query(1, 3), "Test 8 Failed: Range [1-3] sum mismatch");
    }

    @Test
    public void test9_ComplexMix() {
        ds.insert(0, 10);
        ds.insert(1, 20);
        ds.delete(0);     // -> [20]
        ds.insert(0, 5);  // -> [5, 20]
        ds.insert(2, 30); // -> [5, 20, 30]
        ds.delete(1);     // -> [5, 30]
        assertEquals(35, ds.query(0, 1), "Test 9 Failed: Total sum mismatch");
        assertEquals(30, ds.query(1, 1), "Test 9 Failed: Last element mismatch");
    }

    @Test
    public void test10_SingleElementQuery() {
        ds.insert(0, 100);
        ds.insert(1, 200);
        ds.insert(2, 300);
        assertEquals(200, ds.query(1, 1), "Test 10 Failed: Single element query mismatch");
    }
}