package by.it.group451002.gorbach.lesson03;

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
        private final ArrayList<Long> heap = new ArrayList<>();

        // Вспомогательный метод для обмена элементов
        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        // Просеивание элемента вниз (при удалении максимума)
        int siftDown(int i) {
            int size = heap.size();
            while (true) {
                int left = 2 * i + 1;   // Левый потомок
                int right = 2 * i + 2;  // Правый потомок
                int largest = i;        // Предполагаем, что текущий элемент - наибольший

                // Сравниваем с левым потомком
                if (left < size && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }

                // Сравниваем с правым потомком
                if (right < size && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }

                // Если текущий элемент больше потомков - завершаем
                if (largest == i) break;

                // Меняем местами с наибольшим потомком
                swap(i, largest);
                i = largest;  // Продолжаем проверку для нового положения
            }
            return i;
        }

        // Просеивание элемента вверх (при вставке)
        int siftUp(int i) {
            // Пока не дошли до корня и текущий элемент больше родителя
            while (i > 0 && heap.get(i) > heap.get((i - 1) / 2)) {
                swap(i, (i - 1) / 2);  // Меняем с родителем
                i = (i - 1) / 2;       // Переходим к родителю
            }
            return i;
        }

        // Вставка элемента
        void insert(Long value) {
            heap.add(value);       // Добавляем в конец
            siftUp(heap.size() - 1);  // Просеиваем вверх
        }

        // Извлечение максимума
        Long extractMax() {
            if (heap.isEmpty()) {
                return null;  // Куча пуста
            }

            Long max = heap.get(0);               // Запоминаем максимум (корень)
            heap.set(0, heap.get(heap.size() - 1)); // Последний элемент -> в корень
            heap.remove(heap.size() - 1);        // Удаляем последний элемент

            if (!heap.isEmpty()) {
                siftDown(0);  // Просеиваем новый корень вниз
            }

            return max;  // Возвращаем сохранённый максимум
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
