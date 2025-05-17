package by.it.group410902.kavtsevich.lesson03;

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

        int siftDown(int i) { //просеивание вниз
            int maxIndex = i;
            int left = 2 * i + 1; // левый потомок
            int right = 2 * i + 2; // правый потомок

            if (left < heap.size() && heap.get(left) > heap.get(maxIndex)) {
                maxIndex = left;
            }
            if (right < heap.size() && heap.get(right) > heap.get(maxIndex)) {
                maxIndex = right;
            }

            if (i != maxIndex) {
                // Меняем местами элементы
                Long temp = heap.get(i);
                heap.set(i, heap.get(maxIndex));
                heap.set(maxIndex, temp);
                // Продолжаем просеивание вниз
                siftDown(maxIndex);
            }
            return i;
        }

        int siftUp(int i) { //просеивание вверх
            while (i > 0) {
                int parent = (i - 1) / 2; // родитель
                if (heap.get(i) > heap.get(parent)) {
                    // Меняем местами элементы
                    Long temp = heap.get(i);
                    heap.set(i, heap.get(parent));
                    heap.set(parent, temp);
                    i = parent; // продолжаем на уровне родителя
                } else {
                    break;
                }
            }
            return i;
        }

        void insert(Long value) {
            heap.add(value);
            // Просеивание вверх для сохранения свойства Max-кучи
            siftUp(heap.size() - 1);//вставка
        }

        Long extractMax() { //извлечение и удаление максимума
            Long result = null;
            if (heap.isEmpty()) {
                return null; // если куча пустая, возвращаем null
            }
            Long result1 = heap.get(0); // Максимум — это корень (первый элемент)
            heap.set(0, heap.get(heap.size() - 1)); // Перемещаем последний элемент в корень
            heap.remove(heap.size() - 1); // Удаляем последний элемент
            // Просеивание вниз для восстановления свойства Max-кучи
            siftDown(0);
            return result1;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
