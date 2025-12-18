package by.it.group410901.korneew.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Lesson 3. C_Heap.
// Задача: построить max-кучу = пирамиду = бинарное сбалансированное дерево на массиве.

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
                System.out.println(res); // Выводим максимальное значение
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

        // Метод для просеивания вниз
        int siftDown(int i) {
            int largest = i; // Инициализируем largest как корень
            int left = 2 * i + 1; // Левый дочерний элемент
            int right = 2 * i + 2; // Правый дочерний элемент

            // Если левый дочерний элемент больше корня
            if (left < heap.size() && heap.get(left) > heap.get(largest)) {
                largest = left;
            }

            // Если правый дочерний элемент больше текущего largest
            if (right < heap.size() && heap.get(right) > heap.get(largest)) {
                largest = right;
            }

            // Если largest не корень
            if (largest != i) {
                Long swap = heap.get(i);
                heap.set(i, heap.get(largest));
                heap.set(largest, swap);
                // Рекурсивно просеиваем вниз
                siftDown(largest);
            }
            return i;
        }

        // Метод для просеивания вверх
        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                // Если текущий элемент больше родительского
                if (heap.get(i) > heap.get(parent)) {
                    Long temp = heap.get(i);
                    heap.set(i, heap.get(parent));
                    heap.set(parent, temp);
                    i = parent; // Переходим на уровень выше
                } else {
                    break;
                }
            }
            return i;
        }

        // Метод для вставки элемента
        void insert(Long value) {
            heap.add(value); // Добавляем элемент в конец
            siftUp(heap.size() - 1); // Просеиваем вверх
        }

        // Метод для извлечения и удаления максимума
        Long extractMax() {
            if (heap.size() == 0) return null; // Если куча пуста

            Long maxValue = heap.get(0); // Сохраняем максимальное значение
            heap.set(0, heap.get(heap.size() - 1)); // Перемещаем последний элемент на корень
            heap.remove(heap.size() - 1); // Удаляем последний элемент
            siftDown(0); // Просеиваем вниз
            return maxValue;
        }
    }
}