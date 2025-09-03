package by.it.group451002.karbanovich.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.util.Collections.swap;

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
        //тут запишите ваше решение.
        //Будет мало? Ну тогда можете его собрать как Generic и/или использовать в варианте B
        private List<Long> heap = new ArrayList<>();

        // Функция для отсеивания вниз
        int siftDown(int i) {
            // Сравниваем родителя и двух детей и при необходимости меняем родителя с большим ребенком
            while (2 * i + 1 < heap.size()){
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int j = left;
                if (right < heap.size() && heap.get(right) > heap.get(left))
                    j = right;
                if (heap.get(i) >= heap.get(j))
                    break;
                Collections.swap(heap, i, j);
                i = j;
            }
            return i;
        }

        // Функция для отсеивания вверх
        int siftUp(int i) {
            // Сравниваем ребенка и родителя, при необходимости меняем их местами
            while (heap.get(i) > heap.get((i - 1) / 2)){
                Collections.swap(heap, i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
            return i;
        }

        // Процедура для вставки числа по его приоритету (значению)
        void insert(Long value) {
            // Добавляем новое значение
            heap.add(value);

            // Если длина списка больше одного элемента, то просеиваем вверх
            if (heap.size() > 1)
                siftUp(heap.size() - 1);
        }

        // Функция для удаления и извлечения максимума
        Long extractMax() {
            Long result = null;

            // Если список пустой, то возвращаем пустой результат
            if (heap.isEmpty()) return result;

            // Получаем возвращаемый результат/максимальное значение/первый элемент
            result = heap.getFirst();

            // Меняем минимальный и максимальный элементы местами и удаляем максимальный элемент
            Collections.swap(heap, 0, heap.size() - 1);
            heap.removeLast();

            // Просеиваем вниз
            siftDown(0);

            return result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}