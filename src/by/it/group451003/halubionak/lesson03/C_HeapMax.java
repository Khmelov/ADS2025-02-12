package by.it.group451003.halubionak.lesson03;

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

    // Метод читает команды из файла и обрабатывает их.
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap(); // создаём экземпляр max-кучи

        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt(); // читаем количество операций

        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();

            // Обработка команды ExtractMax
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax(); // извлекаем максимум
                if (res != null && res > maxValue) maxValue = res;
                System.out.println(); // для вывода, как в примере
                i++;
            }

            // Обработка команды Insert x
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1])); // вставка элемента
                i++;
            }
        }

        return maxValue; // возвращает наибольшее значение, которое было извлечено
    }

    // Класс MaxHeap реализует max-кучу
    private class MaxHeap {
        private List<Long> heap = new ArrayList<>(); // сам список, где хранятся элементы кучи

        // Просеивание вверх — восстанавливает свойства кучи после вставки
        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2; // индекс родителя
                if (heap.get(i) <= heap.get(parent)) break; // если порядок верный — завершить
                swap(i, parent); // иначе меняем с родителем
                i = parent; // поднимаемся вверх
            }
            return i;
        }

        // Просеивание вниз — восстанавливает свойства кучи после удаления корня
        int siftDown(int i) {
            int size = heap.size();
            while (true) {
                int left = 2 * i + 1; // левый потомок
                int right = 2 * i + 2; // правый потомок
                int largest = i; // пока что считаем текущий элемент самым большим

                // ищем больший из текущего и двух потомков
                if (left < size && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }
                if (right < size && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }
                if (largest == i) break; // если текущий больше потомков — завершить

                swap(i, largest); // иначе меняем с большим потомком
                i = largest; // продолжаем вниз
            }
            return i;
        }

        // Вставка нового элемента
        void insert(Long value) {
            heap.add(value); // добавляем в конец
            siftUp(heap.size() - 1); // восстанавливаем свойства кучи вверхом
        }

        // Извлечение максимального элемента
        Long extractMax() {
            if (heap.isEmpty()) return null;

            Long result = heap.get(0); // максимум — всегда в корне
            Long last = heap.remove(heap.size() - 1); // убираем последний элемент

            // если куча не пуста, перемещаем последний элемент в корень и восстанавливаем кучу вниз
            if (!heap.isEmpty()) {
                heap.set(0, last);
                siftDown(0);
            }

            System.out.print(result); // выводим извлечённый максимум (по условию задачи)
            return result;
        }

        // Обмен двух элементов местами
        void swap(int i, int j) {
            Long tmp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, tmp);
        }


    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}


    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
