package by.it.group410901.gutseva.lesson03;

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
        // Хранилище элементов кучи. Индексация начинается с 0.
// Для элемента с индексом i:
// - родитель: (i-1)/2
// - левый ребенок: 2i + 1
// - правый ребенок: 2i + 2
        private List<Long> heap = new ArrayList<>();

        // Просеивание элемента вверх для восстановления свойств кучи
// i - индекс просеиваемого элемента
        int siftUp(int i) { //просеивание вверх
            // Пока элемент больше своего родителя
            while (heap.get(i) > heap.get((i - 1)/2)) {
                // Меняем местами с родителем
                Long temp = heap.get((i - 1)/2);
                heap.set((i - 1)/2, heap.get(i));
                heap.set(i, temp);

                // Переходим к индексу родителя
                i = (i - 1)/2;
            }
            return i; // Возвращаем новый индекс элемента
        }

        // Просеивание элемента вниз для восстановления свойств кучи
// i - индекс просеиваемого элемента
        int siftDown(int i) { //просеивание вниз
            // Пока у элемента есть хотя бы один ребенок
            while (2*i + 1 < heap.size()) {
                // Индексы левого и правого ребенка
                Integer left = (2*i + 1);
                Integer right = (2*i + 2);
                Integer j = left; // По умолчанию выбираем левого ребенка

                // Если есть правый ребенок и он больше левого
                if (right < heap.size() && heap.get(left) < heap.get((right))){
                    j = right; // Выбираем правого ребенка
                }

                // Если текущий элемент уже больше наибольшего ребенка - завершаем
                if (heap.get(j) > heap.get(i)){
                    break;
                }

                // Меняем местами с выбранным ребенком
                Long temp = heap.get(j);
                heap.set(j, heap.get(i));
                heap.set(i, temp);

                // Переходим к индексу ребенка
                i = j;
            }
            return i; // Возвращаем новый индекс элемента
        }

        // Добавление нового элемента в кучу
        void insert(Long value) { //вставка
            // Добавляем элемент в конец массива
            heap.add(value);
            // Просеиваем его вверх на нужную позицию
            siftUp(heap.size()-1);
        }

        // Извлечение максимального элемента (корня кучи)
        Long extractMax() { //извлечение и удаление максимума
            Long result = null;
            // Запоминаем максимальный элемент (корень)
            result = heap.get(0);
            // Перемещаем последний элемент в корень
            heap.set(0, heap.get(heap.size()-1));
            // Удаляем последний элемент (он теперь в корне)
            heap.remove(heap.size()-1);
            // Просеиваем новый корень вниз на нужную позицию
            siftDown(0);
            // Возвращаем максимальный элемент
            return result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
