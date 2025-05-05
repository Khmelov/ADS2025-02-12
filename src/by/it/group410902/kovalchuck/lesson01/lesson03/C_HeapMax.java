package by.it.group410902.kovalchuck.lesson01.lesson03;

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
        //тут запишите ваше решение.
        //Будет мало? Ну тогда можете его собрать как Generic и/или использовать в варианте B
        private List<Long> heap = new ArrayList<>();

        int siftDown(int i) { // Просеивание вниз
            int largest = i; // Устанавливаем текущий элемент как наибольший
            int left_Child = 2 * i + 1; // Индекс левого потомка
            int right_Child = 2 * i + 2; // Индекс правого потомка

            // Проверяем, существует ли левый потомок и больше ли его значение текущего элемента
            if (left_Child < heap.size() && heap.get(left_Child) > heap.get(largest)) {
                largest = left_Child; // Если больше, обновляем индекс наибольшего элемента
            }

            // Проверяем, существует ли правый потомок и больше ли его значение текущего элемента
            if (right_Child < heap.size() && heap.get(right_Child) > heap.get(largest)) {
                largest = right_Child; // Если больше, обновляем индекс наибольшего элемента
            }

            // Если индекс наибольшего элемента изменился, выполняем обмен
            if (largest != i) {
                long temp = heap.get(i); // Сохраняем значение текущего элемента
                heap.set(i, heap.get(largest)); // Перемещаем значение наибольшего элемента наверх
                heap.set(largest, temp); // Устанавливаем значение текущего элемента в его место

                siftDown(largest); // Рекурсивно вызываем просеивание вниз для нового положения
            }
            return i; // Возвращаем текущий индекс
        }

        int siftUp(int i) { // Просеивание вверх
            while (i > 0 && i > 1) { // Пока узел не является корнем и может переместиться вверх
                int parent = (i - 1) / 2; // Вычисляем индекс родителя

                // Если значение текущего элемента больше значения родителя
                if (heap.get(i) > heap.get(parent)) {
                    long temp = heap.get(i); // Сохраняем значение текущего элемента
                    heap.set(i, heap.get(parent)); // Перемещаем родителя вниз
                    heap.set(parent, temp); // Перемещаем текущий элемент вверх

                    i = parent; // Обновляем индекс текущего элемента для следующей итерации
                } else {
                    break; // Если текущий элемент не больше родителя, завершаем цикл
                }
            }
            return i; // Возвращаем текущий индекс
        }

        void insert(Long value) { // Вставка элемента
            heap.add(value); // Добавляем новый элемент в конец кучи
            siftUp(heap.size() - 1); // Выполняем просеивание вверх для добавленного элемента
        }

        Long extractMax() { // Извлечение и удаление максимума
            Long result = heap.get(0); // Максимум всегда находится на вершине (корень кучи)
            heap.remove(0); // Удаляем корневой элемент
            return result; // Возвращаем значение удалённого максимума
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
