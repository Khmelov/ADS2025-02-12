package by.it.group410902.latipov.lesson03;

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

        // Демонстрация работы новых методов
        instance.demoHeapOperations();
    }

    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        scanner.nextLine(); // consume the remaining newline

        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println("Extracted: " + res);
                i++;
            } else if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert")) {
                    heap.insert(Long.parseLong(p[1]));
                    System.out.println("Inserted: " + p[1] + ", Heap size: " + heap.size());
                    i++;
                }
            }

            // Демонстрация метода peek
            Long currentMax = heap.peek();
            System.out.println("Current max (peek): " + currentMax);
        }
        scanner.close();
        return maxValue;
    }

    // Демонстрация работы кучи с новыми методами
    private void demoHeapOperations() {
        System.out.println("\n=== Демонстрация работы MaxHeap ===");
        MaxHeap heap = new MaxHeap();

        // Вставляем элементы
        long[] elements = {30L, 10L, 50L, 20L, 40L};
        for (long element : elements) {
            heap.insert(element);
            System.out.println("Inserted: " + element + ", Size: " + heap.size() +
                    ", Peek: " + heap.peek() + ", Heap: " + heap);
        }

        // Извлекаем элементы
        System.out.println("\n=== Извлечение элементов ===");
        while (heap.size() > 0) {
            Long max = heap.extractMax();
            System.out.println("Extracted: " + max + ", Size: " + heap.size() +
                    ", Peek: " + heap.peek() + ", Heap: " + heap);
        }

        // Попытка извлечения из пустой кучи
        System.out.println("\n=== Попытка операций с пустой кучей ===");
        System.out.println("Extract from empty: " + heap.extractMax());
        System.out.println("Peek from empty: " + heap.peek());
        System.out.println("Size of empty: " + heap.size());
    }

    private class MaxHeap {
        private List<Long> heap = new ArrayList<>();

        // Просеивание вверх (восстановление свойства кучи снизу вверх)
        private void siftUp(int index) {
            if (index == 0) return; // корень не имеет родителя

            int parentIndex = (index - 1) / 2;

            // Если текущий элемент больше родителя, меняем их местами
            if (heap.get(index) > heap.get(parentIndex)) {
                swap(index, parentIndex);
                siftUp(parentIndex); // продолжаем просеивание вверх
            }
        }

        // Просеивание вниз (восстановление свойства кучи сверху вниз)
        private void siftDown(int index) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int largest = index;

            // Ищем наибольший элемент среди текущего узла и его детей
            if (leftChild < heap.size() && heap.get(leftChild) > heap.get(largest)) {
                largest = leftChild;
            }

            if (rightChild < heap.size() && heap.get(rightChild) > heap.get(largest)) {
                largest = rightChild;
            }

            // Если наибольший элемент не текущий, меняем местами и продолжаем просеивание
            if (largest != index) {
                swap(index, largest);
                siftDown(largest);
            }
        }

        // Итеративная версия просеивания вниз (оптимизированная)
        private void siftDownIterative(int index) {
            int current = index;
            while (true) {
                int left = 2 * current + 1;
                int right = 2 * current + 2;
                int largest = current;

                if (left < heap.size() && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }
                if (right < heap.size() && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }

                if (largest == current) break;

                swap(current, largest);
                current = largest;
            }
        }

        // Вспомогательный метод для обмена элементов
        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        // Вставка элемента в кучу
        void insert(Long value) {
            heap.add(value); // добавляем в конец
            siftUp(heap.size() - 1); // восстанавливаем свойства кучи
        }

        // Извлечение и удаление максимального элемента
        Long extractMax() {
            if (heap.isEmpty()) {
                return null;
            }

            Long max = heap.get(0); // максимальный элемент всегда в корне
            Long lastElement = heap.remove(heap.size() - 1); // удаляем последний элемент

            if (!heap.isEmpty()) {
                heap.set(0, lastElement); // помещаем последний элемент в корень
                // Можно использовать любую из двух версий:
                siftDownIterative(0); // используем итеративную версию (оптимизированную)
                // siftDown(0); // или рекурсивную версию
            }

            return max;
        }

        // 1. Метод получения размера кучи
        public int size() {
            return heap.size();
        }

        // 2. Метод просмотра максимального элемента без извлечения
        public Long peek() {
            return heap.isEmpty() ? null : heap.get(0);
        }

        // Для отладки - вывод кучи в виде строки
        @Override
        public String toString() {
            return heap.toString();
        }
    }
}