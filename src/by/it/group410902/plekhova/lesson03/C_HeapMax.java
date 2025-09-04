package by.it.group410902.plekhova.lesson03;

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

    //эта процедура читает данные из файла, ее можно не менять.
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println();
                i++;
            }
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1]));
                i++;
                //System.out.println(heap); //debug
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        private List<Long> heap = new ArrayList<>();

/* Этот метод восстанавливает свойства кучи после вставки нового элемента. Он
поднимает элемент вверх по дереву до тех пор, пока он не станет родителем
меньшего элемента или не достигнет корня. */
        int siftUp(int index) {
            while (index > 0) {
                int parentIndex = (index - 1) / 2;
                if (heap.get(index) > heap.get(parentIndex)) {
                    swap(index, parentIndex);
                    index = parentIndex;
                } else {
                    break;
                }
            }
            return index;
        }

        /* Этот метод восстанавливает свойства кучи после извлечения максимального элемента. Он
        опускает корень вниз по дереву до тех пор, пока он не станет родителем большего элемента
        или не достигнет листа */
        int siftDown(int index) {
            int size = heap.size();
            while (index < size) {
                int leftChildIndex = index * 2 + 1;
                int rightChildIndex = index * 2 + 2;
                int largestIndex = index;

                if (leftChildIndex < size && heap.get(leftChildIndex) > heap.get(largestIndex)) {
                    largestIndex = leftChildIndex;
                }
                if (rightChildIndex < size && heap.get(rightChildIndex) > heap.get(largestIndex)) {
                    largestIndex = rightChildIndex;
                }

                if (largestIndex != index) {
                    swap(index, largestIndex);
                    index = largestIndex;
                } else {
                    break;
                }
            }
            return index;
        }

        void insert(Long value) {
            heap.add(value); // Добавляем элемент в конец кучи
            siftUp(heap.size() - 1); // Восстанавливаем свойства кучи
        }

        Long extractMax() {
            if (heap.isEmpty()) return null; // Если куча пуста

            Long maxValue = heap.get(0); // Максимальный элемент - корень кучи
            Long lastValue = heap.remove(heap.size() - 1); // Удаляем последний элемент

            if (!heap.isEmpty()) {
                heap.set(0, lastValue); // Ставим последний элемент на место корня
                siftDown(0); // Восстанавливаем свойства кучи
            }

            return maxValue;
        }

        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }

}
