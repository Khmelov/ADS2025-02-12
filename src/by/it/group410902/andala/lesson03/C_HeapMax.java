package by.it.group410902.andala.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class C_HeapMax {

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входных данных из файла
        InputStream stream = C_HeapMax.class.getResourceAsStream("dataC.txt");
        C_HeapMax instance = new C_HeapMax();
        // Нахождение максимального значения через операции с кучей
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    /**
     * Метод для нахождения максимального значения через операции с кучей
     * @param stream поток ввода с командами
     * @return максимальное значение, извлеченное из кучи
     */
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L; // Хранит максимальное найденное значение
        MaxHeap heap = new MaxHeap(); // Создаем max-кучу
        Scanner scanner = new Scanner(stream);

        // Читаем количество команд
        Integer count = scanner.nextInt();
        scanner.nextLine(); // Переходим на следующую строку

        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();

            // Обработка команды extractMax
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) {
                    maxValue = res; // Обновляем максимум
                }
                System.out.println(res); // Выводим извлеченное значение
                i++;
            }

            // Обработка команды insert
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert")) {
                    heap.insert(Long.parseLong(p[1])); // Вставляем значение в кучу
                }
                i++;
            }
        }

        return maxValue;
    }

    /**
     * Класс, реализующий max-кучу на основе ArrayList
     */
    public class MaxHeap {
        private List<Long> heap = new ArrayList<>(); // Основное хранилище элементов кучи

        /**
         * Просеивание элемента вверх (восстановление свойства кучи при добавлении)
         * @param i индекс просеиваемого элемента
         * @return новый индекс элемента
         */
        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2; // Индекс родителя
                if (heap.get(i) <= heap.get(parent)) break; // Свойство кучи не нарушено
                swap(i, parent); // Меняем местами с родителем
                i = parent; // Переходим на уровень выше
            }
            return i;
        }

        /**
         * Просеивание элемента вниз (восстановление свойства кучи при удалении)
         * @param i индекс просеиваемого элемента
         * @return новый индекс элемента
         */
        int siftDown(int i) {
            while (true) {
                int left = 2 * i + 1; // Индекс левого потомка
                int right = 2 * i + 2; // Индекс правого потомка
                int largest = i; // Индекс наибольшего элемента

                // Сравниваем с левым потомком
                if (left < heap.size() && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }

                // Сравниваем с правым потомком
                if (right < heap.size() && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }

                // Если текущий элемент больше потомков - завершаем
                if (largest == i) break;

                swap(i, largest); // Меняем местами с наибольшим потомком
                i = largest; // Продолжаем просеивание вниз
            }
            return i;
        }

        /**
         * Вставка нового элемента в кучу
         * @param value вставляемое значение
         */
        void insert(Long value) {
            heap.add(value); // Добавляем в конец
            siftUp(heap.size() - 1); // Восстанавливаем свойства кучи
        }

        /**
         * Извлечение максимального элемента из кучи
         * @return максимальный элемент или null, если куча пуста
         */
        Long extractMax() {
            if (heap.isEmpty()) return null;

            Long result = heap.get(0); // Запоминаем корневой элемент (максимальный)
            Long last = heap.remove(heap.size() - 1); // Удаляем последний элемент

            if (!heap.isEmpty()) {
                heap.set(0, last); // Помещаем последний элемент в корень
                siftDown(0); // Восстанавливаем свойства кучи
            }

            return result;
        }

        /**
         * Обмен элементов кучи местами
         * @param i индекс первого элемента
         * @param j индекс второго элемента
         */
        private void swap(int i, int j) {
            Long tmp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, tmp);
        }
    }
}