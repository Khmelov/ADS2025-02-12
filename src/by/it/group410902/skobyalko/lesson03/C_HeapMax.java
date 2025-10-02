package by.it.group410902.skobyalko.lesson03;

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
        scanner.nextLine(); // move to the next line after reading count

        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null) {
                    System.out.println(res);
                    if (res > maxValue) {
                        maxValue = res;
                    }
                }
                i++;
            } else if (s.startsWith("Insert")) {
                String[] parts = s.split(" ");
                heap.insert(Long.parseLong(parts[1]));
                i++;
            }
        }
        return maxValue;
    }
/// ////////////////////////////.
    private static class MaxHeap {
        private final List<Long> heap = new ArrayList<>();

        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        Long extractMax() {
            if (heap.isEmpty()) {
                return null;
            }
            Long result = heap.get(0);
            Long last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                siftDown(0);
            }
            return result;
        }

        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i) > heap.get(parent)) {
                    swap(i, parent);
                    i = parent;
                } else {
                    break;
                }
            }
        }

        private void siftDown(int i) {
            int size = heap.size();
            while (2 * i + 1 < size) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int largest = i;

                if (left < size && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }
                if (right < size && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }

                if (largest != i) {
                    swap(i, largest);
                    i = largest;
                } else {
                    break;
                }
            }
        }


        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }
}
/// /////////////////

