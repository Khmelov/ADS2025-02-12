package by.it.group451003.plyushchevich.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Lesson 3. C_Heap.
// Задача: построить max-кучу = пирамиду = бинарное сбалансированное дерево на массиве.
// ВАЖНО! НЕЛЬЗЯ ИСПОЛЬЗОВАТЬ НИКАКИЕ КОЛЛЕКЦИИ, КРОМЕ ARRAYLIST (его можно, но только для массива)

//      Проверка проводится по данным файла
//      Первая строка входа содержит число операций 1 ≤ n ≤ 100000.
//      Каждая из последующих nn строк задают операцию одного из следующих двух типов:

//      Insert x, где 0 ≤ x ≤ 1000000000 — целое число;
//      ExtractMax.

//      Первая операция добавляет число x в очередь с приоритетами,
//      вторая — извлекает максимальное число и выводит его.

//      Sample Input:
//      6
//      Insert 200
//      Insert 10
//      ExtractMax
//      Insert 5
//      Insert 500
//      ExtractMax
//
//      Sample Output:
//      200
//      500

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
        int count = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null) {
                    System.out.println(res);
                    if (res > maxValue) maxValue = res;
                }
                i++;
            }
            else if (s.startsWith("Insert") || s.startsWith("insert")) {
                String[] p = s.split("\\s+");
                heap.insert(Long.parseLong(p[1]));
                i++;
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        private final List<Long> heap = new ArrayList<>();

        // Просеивание вверх: поправить позицию i, если элемент больше родителя
        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i) > heap.get(parent)) {
                    swap(i, parent);
                    i = parent;
                } else {
                    break;
                }
            }
            return i;
        }

        // Просеивание вниз: поправить позицию i, если элемент меньше любого из детей
        int siftDown(int i) {
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
                    swap(i, largest);
                    i = largest;
                } else {
                    break;
                }
            }
            return i;
        }

        // Вставка нового элемента: добавляем в конец и просеиваем вверх
        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        // Извлечение максимума: меняем корень и последний, убираем последний и просеиваем вниз
        Long extractMax() {
            if (heap.isEmpty()) {
                return null;
            }
            Long result = heap.get(0);
            int lastIndex = heap.size() - 1;
            // Переместить последний элемент в корень
            heap.set(0, heap.get(lastIndex));
            heap.remove(lastIndex);
            // Просеять новый корень вниз
            if (!heap.isEmpty()) {
                siftDown(0);
            }
            return result;
        }

        private void swap(int i, int j) {
            Long tmp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, tmp);
        }
    }
}
