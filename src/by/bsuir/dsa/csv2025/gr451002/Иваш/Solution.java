package by.bsuir.dsa.csv2025.gr451002.Иваш;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Solution {

    private static final int DEFAULT_CAPACITY = 16;

    private int[] array;
    private int size;

    // Конструкторы
    public Solution() {
        this(DEFAULT_CAPACITY);
    }

    public Solution(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        array = new int[capacity + 1]; // индекс 0 не используем
        size = 0;
    }

    // Создаёт кучу из готового массива за O(n)
    public Solution(int[] source) {
        this(source.length);
        for (int value : source) {
            array[++size] = value;
        }
        buildHeap(); // линейное построение
    }

    // Основные операции

    public void insert(int value) {
        if (size + 1 >= array.length) {
            resize();
        }
        array[++size] = value;
        siftUp(size);
    }

    public int extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        int min = array[1];
        array[1] = array[size--];
        if (size > 0) {
            siftDown(1);
        }
        return min;
    }

    /** Просмотр минимума без удаления — O(1) */
    public int peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return array[1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Вспомогательные методы

    /** Просеивание вверх */
    private void siftUp(int i) {
        while (i > 1) {
            int parent = i / 2;
            if (array[parent] <= array[i]) {
                break; // свойство кучи восстановлено
            }
            swap(parent, i);
            i = parent;
        }
    }

    /** Просеивание вниз */
    private void siftDown(int i) {
        while (2 * i <= size) {
            int left = 2 * i;
            int right = left + 1;
            int smallest = left;

            if (right <= size && array[right] < array[left]) {
                smallest = right;
            }
            if (array[i] <= array[smallest]) {
                break;
            }
            swap(i, smallest);
            i = smallest;
        }
    }

    /** Линейное построение кучи за O(n) — алгоритм Флойда */
    private void buildHeap() {
        for (int i = size / 2; i >= 1; i--) {
            siftDown(i);
        }
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void resize() {
        array = Arrays.copyOf(array, array.length * 2);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 1; i <= size; i++) {
            sb.append(array[i]);
            if (i < size) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Solution heap = new Solution();


        System.out.println("\nEnter the numbers (integers) you want to add to the heap.");
        System.out.println("• Enter one number at a time and press Enter");
        System.out.println("• To complete input, enter 'end' or 'stop'\n");

        System.out.println("Inserting elements into a heap:");

        while (true) {
            System.out.print("→ Enter a number (or 'end' to terminate): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("end") || input.equalsIgnoreCase("stop") || input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                int value = Integer.parseInt(input);
                heap.insert(value);
                System.out.println("   Added: " + value + " → heap: " + heap);
            } catch (NumberFormatException e) {
                System.out.println("   Error: enter correct number!");
            }
        }

        if (heap.isEmpty()) {
            System.out.println("\nYou haven't entered anything. The heap is empty.");
        } else {
            System.out.println("   Extract elements in ascending order:");

            System.out.print("   Result: ");
            while (!heap.isEmpty()) {
                System.out.print(heap.extractMin() + " ");
            }
        }
        scanner.close();
    }
}



/*import org.junit.Test;
import static org.junit.Assert.*;

public class SolutionTest {

    @Test
    public void testInsertAndExtract() {
        Solution heap = new Solution();
        heap.insert(10);
        heap.insert(3);
        heap.insert(7);
        heap.insert(1);
        heap.insert(5);

        assertEquals(1, heap.extractMin());
        assertEquals(3, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertEquals(7, heap.extractMin());
        assertEquals(10, heap.extractMin());
    }

    @Test
    public void testPeekMin() {
        Solution heap = new Solution();
        heap.insert(42);
        heap.insert(7);
        assertEquals(7, heap.peekMin());
        assertEquals(7, heap.extractMin());
        assertEquals(42, heap.peekMin());
    }

    @Test
    public void testBuildFromArray() {
        int[] arr = {9, 1, 8, 2, 7, 3, 6, 4, 5};
        Solution heap = new Solution(arr);

        for (int i = 1; i <= 9; i++) {
            assertEquals(i, heap.extractMin());
        }
    }

    @Test
    public void testEmptyHeap() {
        Solution heap = new Solution();
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testExtractFromEmpty() {
        new Solution().extractMin();
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testPeekFromEmpty() {
        new Solution().peekMin();
    }

    @Test
    public void testLargeAmount() {
        Solution heap = new Solution();
        int n = 10000;
        for (int i = n; i >= 1; i--) {
            heap.insert(i);
        }
        for (int i = 1; i <= n; i++) {
            assertEquals(i, heap.extractMin());
        }
    }

    @Test
    public void testToString() {
        Solution heap = new Solution();
        heap.insert(5);
        heap.insert(1);
        heap.insert(3);
        String s = heap.toString();
        assertTrue(s.contains("1") && s.contains("3") && s.contains("5"));
    }
}*/