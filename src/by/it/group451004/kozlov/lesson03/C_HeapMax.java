package by.it.group451004.kozlov.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class C_HeapMax {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_HeapMax.class.getResourceAsStream("dataC.txt");
        C_HeapMax instance = new C_HeapMax();
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println(res);
                i++;
            }
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1]));
                i++;
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        private List<Long> heap = new ArrayList<>();

        int siftDown(int i) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            if (left < heap.size() && heap.get(left) > heap.get(largest)) {
                largest = left;
            }

            if (right < heap.size() && heap.get(right) > heap.get(largest)) {
                largest = right;
            }

            if (largest != i) {
                swap(i, largest);
                return siftDown(largest);
            }
            return i;
        }

        int siftUp(int i) {
            while (i > 0 && heap.get(i) > heap.get((i - 1) / 2)) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
            return i;
        }

        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        Long extractMax() {
            if (heap.isEmpty()) {
                return null;
            }

            Long result = heap.get(0);
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);

            if (!heap.isEmpty()) {
                siftDown(0);
            }

            return result;
        }

        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }
}