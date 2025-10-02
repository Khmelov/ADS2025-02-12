package by.it.group451002.andreev.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Класс C_HeapMax реализует структуру данных "Максимальная куча".
 * Позволяет вставлять элементы и извлекать максимальный.
 */
public class C_HeapMax {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataC.txt"
        InputStream stream = C_HeapMax.class.getResourceAsStream("dataC.txt");
        C_HeapMax instance = new C_HeapMax();

        // Вызываем метод поиска максимального элемента
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    /**
     * Метод findMaxValue обрабатывает входные данные и определяет максимальное значение в куче.
     * @param stream Входной поток данных
     * @return Максимальное найденное значение
     */
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap(); // Создание экземпляра MaxHeap
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        scanner.nextLine(); // Поглощаем оставшийся перевод строки

        // Читаем команды из входных данных и выполняем соответствующие операции
        for (int i = 0; i < count; i++) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                // Извлекаем максимальный элемент из кучи
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println(res);
            }
            else if (s.startsWith("Insert ")) {
                // Извлекаем число из строки и вставляем его в кучу
                heap.insert(Long.parseLong(s.substring(7)));
            }
        }
        return maxValue;
    }

    /**
     * Класс MaxHeap реализует максимальную кучу.
     */
    private class MaxHeap {
        private List<Long> heap = new ArrayList<>(); // Лист для хранения элементов кучи

        /**
         * Метод swap меняет местами два элемента в куче.
         * @param i индекс первого элемента
         * @param j индекс второго элемента
         */
        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        /**
         * Метод siftUp выполняет просеивание вверх при добавлении элемента.
         * @param i индекс вставленного элемента
         * @return финальный индекс элемента после просеивания
         */
        int siftUp(int i) {
            while (i > 0 && heap.get(i) > heap.get((i - 1) / 2)) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2; // Перемещаемся выше по дереву
            }
            return i;
        }

        /**
         * Метод siftDown выполняет просеивание вниз после удаления элемента.
         * @param i индекс начального элемента
         * @return финальный индекс элемента после просеивания
         */
        int siftDown(int i) {
            int left, right, largest;
            while (true) {
                left = 2 * i + 1;
                right = 2 * i + 2;
                largest = i;

                // Проверяем левый потомок
                if (left < heap.size() && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }

                // Проверяем правый потомок
                if (right < heap.size() && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }

                // Если максимальный элемент уже в нужном месте, завершаем
                if (largest == i) break;

                swap(i, largest);
                i = largest;
            }
            return i;
        }

        /**
         * Метод insert добавляет новый элемент в кучу.
         * @param value вставляемое значение
         */
        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1); // Выполняем просеивание вверх
        }

        /**
         * Метод extractMax удаляет и возвращает максимальный элемент кучи.
         * @return максимальный элемент или null, если куча пустая
         */
        Long extractMax() {
            if (heap.isEmpty()) return null;

            Long max = heap.get(0);
            heap.set(0, heap.get(heap.size() - 1)); // Перемещаем последний элемент на вершину
            heap.remove(heap.size() - 1); // Удаляем последний элемент

            if (!heap.isEmpty()) {
                siftDown(0); // Просеиваем вниз, чтобы сохранить структуру кучи
            }

            return max;
        }
    }
}
