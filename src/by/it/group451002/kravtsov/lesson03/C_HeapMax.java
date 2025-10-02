package by.it.group451002.kravtsov.lesson03;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        // Реализация MaxHeap для работы с приоритетной очередью на основе массива (List).
        private List<Long> heap = new ArrayList<>(); // Хранилище элементов MaxHeap

        // Метод для просеивания вниз (siftDown), чтобы восстановить свойства кучи
        int siftDown(int i) {
            boolean is_sifted = false; // Флаг завершения просеивания
            while (!is_sifted && 2 * i + 1 < heap.size()) { // Пока не дошли до конца дерева
                int child = 2 * i + 1; // Левый ребёнок
                // Выбираем большего ребёнка (левый или правый)
                if (child + 1 < heap.size() && heap.get(child + 1) > heap.get(child)) {
                    child++;
                }
                // Если ребёнок больше родителя, меняем их местами
                if (heap.get(child) > heap.get(i)) {
                    long temp = heap.get(child);
                    heap.set(child, heap.get(i));
                    heap.set(i, temp);
                    i = child; // Продолжаем просеивать вниз
                } else {
                    is_sifted = true; // Просеивание завершено
                }
            }
            return i;
        }

        // Метод для просеивания вверх (siftUp), чтобы восстановить свойства кучи
        int siftUp(int i) {
            // Пока текущий элемент больше родителя, меняем их местами
            while (i > 0 && heap.get(i) > heap.get((i - 1) / 2)) {
                long temp = heap.get(i);
                heap.set(i, heap.get((i - 1) / 2));
                heap.set((i - 1) / 2, temp);
                i = (i - 1) / 2; // Продолжаем подниматься в верх
            }
            return i;
        }

        // Метод для вставки нового элемента в MaxHeap
        void insert(Long value) {
            heap.add(value); // Добавляем элемент в конец списка
            siftUp(heap.size() - 1); // Восстанавливаем свойства кучи
        }

        // Метод для извлечения и удаления максимального элемента из MaxHeap
        Long extractMax() {
            Long result = heap.get(0); // Максимальный элемент (корень)
            heap.set(0, heap.get(heap.size() - 1)); // Перемещаем последний элемент в корень
            heap.remove(heap.size() - 1); // Удаляем последний элемент
            siftDown(0); // Восстанавливаем свойства кучи
            return result; // Возвращаем максимальный элемент
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }
}

// РЕМАРКА. Это задание исключительно учебное.
// Свои собственные кучи нужны довольно редко.
// В реальном приложении все иначе. Изучите и используйте коллекции
// TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
