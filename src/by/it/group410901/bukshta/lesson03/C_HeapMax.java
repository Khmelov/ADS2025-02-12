package by.it.group410901.bukshta.lesson03;

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
        System.out.println("MAX=" + instance.findMaxValue(stream)); // Печать результата
    }

    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L; // переменная для хранения максимального найденного элемента
        MaxHeap heap = new MaxHeap(); // наша куча
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt(); // читаем количество операций

        scanner.nextLine(); // КРИТИЧНО! Нужно убрать пустую строку после nextInt(),
        // иначе при чтении nextLine() дальше будет пустая строка ""

        // цикл по количеству операций
        for (int i = 0; i < count; i++) {
            String s = scanner.nextLine(); // читаем строку

            // Если команда extractMax - извлекаем максимум из кучи
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax(); // извлекли макс элемент из кучи
                if (res != null) {
                    System.out.println(res); // выводим его
                    if (res > maxValue) maxValue = res; // обновляем максимум
                }
            }
            // Если команда insert x
            else if (s.contains(" ")) {
                String[] p = s.split(" "); // разделяем команду и число
                if (p[0].equalsIgnoreCase("insert")) {
                    heap.insert(Long.parseLong(p[1])); // добавляем число в кучу
                }
            }
        }
        return maxValue; // возвращаем максимальный найденный элемент
    }

    // Вложенный класс - реализация MaxHeap
    private class MaxHeap {
        private List<Long> heap = new ArrayList<>(); // наша куча хранится в ArrayList

        // Просеивание вверх — при вставке элемента
        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2; // родитель
                if (heap.get(i) > heap.get(parent)) { // если ребёнок больше родителя — меняем их местами
                    swap(i, parent);
                    i = parent;
                } else {
                    break; // иначе заканчиваем просеивание
                }
            }
            return i;
        }

        // Просеивание вниз — при удалении элемента
        int siftDown(int i) {
            int left, right, largest;
            while (2 * i + 1 < heap.size()) { // пока есть хотя бы левый потомок
                left = 2 * i + 1; // левый
                right = 2 * i + 2; // правый
                largest = i; // пока что считаем максимум текущий элемент

                if (left < heap.size() && heap.get(left) > heap.get(largest)) {
                    largest = left; // максимум среди потомков и текущего
                }
                if (right < heap.size() && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }

                if (largest == i) {
                    break; // если максимум — текущий элемент, заканчиваем
                }

                swap(i, largest); // иначе меняем местами и продолжаем просеивание вниз
                i = largest;
            }
            return i;
        }

        // Добавление элемента в кучу
        void insert(Long value) {
            heap.add(value); // вставляем в конец
            siftUp(heap.size() - 1); // просеиваем вверх
        }

        // Извлечение максимального элемента из кучи
        Long extractMax() {
            if (heap.isEmpty()) {
                return null; // если куча пустая — возвращаем null
            }
            Long result = heap.get(0); // максимум — это верхушка кучи
            Long last = heap.remove(heap.size() - 1); // удаляем последний элемент
            if (!heap.isEmpty()) {
                heap.set(0, last); // переносим последний элемент на верхушку
                siftDown(0); // просеиваем вниз
            }
            return result; // возвращаем максимум
        }

        // Вспомогательная функция для обмена элементов местами
        private void swap(int i, int j) {
            Long tmp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, tmp);
        }
    }
}