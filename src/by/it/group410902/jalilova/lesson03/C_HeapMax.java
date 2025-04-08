package by.it.group410902.jalilova.lesson03;

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

        //просеивание вверх
        int siftDown(int i) {
            int leftChild = 2 * i + 1;  //индекс левого ребенка
            int rightChild = 2 * i + 2; //индекс правого ребенка
            int largest = i;  //изначально максимальный элемент - это сам i

            //сравниваем с левым ребенком
            if (leftChild < heap.size() && heap.get(leftChild) > heap.get(largest)) {
                largest = leftChild;
            }

            //сравниваем с правым ребенком
            if (rightChild < heap.size() && heap.get(rightChild) > heap.get(largest)) {
                largest = rightChild;
            }

            //если максимальный элемент - это сам i, то кучу не нужно восстанавливать
            if (largest == i) return i;

            //меняем местами текущий элемент с наибольшим
            Long temp = heap.get(i);
            heap.set(i, heap.get(largest));
            heap.set(largest, temp);

            //рекурсивно просеиваем вниз, начиная с нового индекса
            return siftDown(largest);
        }

        //просеивание вниз
        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;  //индекс родительского элемента
                //если элемент на позиции i больше родителя, меняем их местами
                if (heap.get(i) > heap.get(parent)) {
                    Long temp = heap.get(i);
                    heap.set(i, heap.get(parent));
                    heap.set(parent, temp);
                    i = parent;  //переходим к родительскому элементу
                } else {
                    break;  //если элемент на i не больше родителя, остановим
                }
            }
            return i;  //возвращаем позицию элемента после "просеивания"
        }

        //вставка нового элемента в кучу
        void insert(Long value) {
            heap.add(value);  //добавляем элемент в конец
            siftUp(heap.size() - 1);  //просеиваем элемент вверх, начиная с конца
        }

        //извлечение и удаление максимума
        Long extractMax() {
            if (heap.isEmpty()) return null;  //если куча пуста, возвращаем null

            Long result = heap.get(0);  //максимум всегда на корне
            Long lastElement = heap.remove(heap.size() - 1);  //удаляем последний элемент

            if (!heap.isEmpty()) {
                heap.set(0, lastElement);  //перемещаем последний элемент на корень
                siftDown(0);  //просеиваем его вниз, чтобы восстановить структуру кучи
            }
            return result;  //возвращаем максимальное значение
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
