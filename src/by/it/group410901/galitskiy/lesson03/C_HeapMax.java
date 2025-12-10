package by.it.group410901.galitskiy.lesson03;

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

    // Обрабатывает операции и возвращает максимальное извлеченное значение
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        int count = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("extractMax")) {
                Long current = heap.extractMax();
                if (current != null && current > maxValue) maxValue = current;
            }
            else if (line.startsWith("Insert ")) {
                heap.insert(Long.parseLong(line.substring(7)));
            }
        }
        return maxValue;
    }

    // Реализация max-кучи на ArrayList
    private class MaxHeap {
        private final List<Long> heap = new ArrayList<>();

        // Восстановление свойств кучи при просеивании вниз
        private void siftDown(int i) {
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
                siftDown(largest);
            }
        }

        // Восстановление свойств кучи при просеивании вверх
        private void siftUp(int i) {
            while (i > 0 && heap.get(i) > heap.get((i - 1) / 2)) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
        }

        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        // Добавление элемента в кучу
        public void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        // Извлечение максимального элемента
        public Long extractMax() {
            if (heap.isEmpty()) return null;

            Long max = heap.get(0);
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);

            if (!heap.isEmpty()) {
                siftDown(0);
            }
            return max;
        }
    }
}