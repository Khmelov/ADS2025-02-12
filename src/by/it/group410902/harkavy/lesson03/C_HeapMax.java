package by.it.group410902.harkavy.lesson03;

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

    // Метод, считывающий команды из входного файла и возвращающий максимальное значение,
    // извлечённое из кучи.
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        int count = scanner.nextInt();
        scanner.nextLine(); // переходим на новую строку после считывания числа операций
        for (int i = 0; i < count; i++) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue)
                    maxValue = res;
                System.out.println(res);
            } else if (s.startsWith("Insert")) {
                // Формат строки: "Insert x"
                String[] parts = s.split(" ");
                heap.insert(Long.parseLong(parts[1]));
            }
        }
        scanner.close();
        return maxValue;
    }

    // Реализация max-кучи
    private class MaxHeap {
        // Используем ArrayList для хранения элементов кучи (индексация с 0)
        private List<Long> heap = new ArrayList<>();

        // siftDown – просеивание вверх (бабблинг вверх): используется после вставки нового элемента.
        // Элемент, добавленный в конец, поднимается вверх, пока не окажется меньше или равен своему родителю.
        int siftDown(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(parent) < heap.get(i)) {
                    Long temp = heap.get(parent);
                    heap.set(parent, heap.get(i));
                    heap.set(i, temp);
                    i = parent;
                } else {
                    break;
                }
            }
            return i;
        }

        // siftUp – просеивание вниз (бабблинг вниз): используется после извлечения максимального элемента.
        // Элемент, перемещённый в корень, опускается вниз, пока не станет больше своих детей.
        int siftUp(int i) {
            int n = heap.size();
            while (true) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int largest = i;
                if (left < n && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }
                if (right < n && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }
                if (largest != i) {
                    Long temp = heap.get(i);
                    heap.set(i, heap.get(largest));
                    heap.set(largest, temp);
                    i = largest;
                } else {
                    break;
                }
            }
            return i;
        }

        // Вставка нового элемента в кучу: добавляем в конец и поднимаем его вверх.
        void insert(Long value) {
            heap.add(value);
            siftDown(heap.size() - 1);
        }

        // Извлечение максимального элемента:
        // Если куча пуста — возвращаем null.
        // Иначе, сохраняем корневой элемент (максимум), переносим последний элемент в корень,
        // удаляем последний элемент и опускаем новый корень вниз.
        Long extractMax() {
            if (heap.isEmpty())
                return null;
            Long max = heap.get(0);
            Long last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                siftUp(0);
            }
            return max;
        }
    }
}