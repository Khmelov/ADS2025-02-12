package by.it.group451001.bolshakova.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class C_HeapMax {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_HeapMax.class.getResourceAsStream("dataC.txt");
        C_HeapMax instance = new C_HeapMax();
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    // Этот метод читает входные данные и выполняет операции над кучей.
    // При этом, для каждой операции "ExtractMax" выводится извлечённое значение,
    // а методом возвращается максимальное значение, которое встретилось.
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        scanner.nextLine(); // переход на новую строку после чтения числа

        for (int i = 0; i < count; ) {
            String s = scanner.nextLine().trim();
            // Пропускаем пустые строки, если они встречаются
            if (s.isEmpty()) continue;

            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) {
                    maxValue = res;
                }
                System.out.println(res);
                i++;
            } else if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert")) {
                    heap.insert(Long.parseLong(p[1]));
                }
                i++;
                // Для отладки можно выводить содержимое кучи:
                // System.out.println(heap);
            }
        }
        return maxValue;
    }

    // Реализация max-кучи с методами для вставки и извлечения максимума.
    private class MaxHeap {
        // Куча представлена в виде динамического массива.
        // Элемент с индексом 0 – корень (максимальный элемент).
        // Для узла с индексом i: левый потомок имеет индекс 2*i + 1,
        // а правый – 2*i + 2.
        private List<Long> heap = new ArrayList<>();

        /**
         * Метод siftDown (просеивание вниз).
         * Его вызывают, когда элемент (например, помещённый в корень после извлечения максимума)
         * может нарушать свойство кучи и должен опуститься вниз.
         *
         * @param i индекс элемента, который нужно "опустить" вниз.
         * @return конечный индекс элемента после просеивания.
         */
        int siftDown(int i) {
            int n = heap.size();
            while (true) {
                int left = 2 * i + 1;    // индекс левого потомка
                int right = 2 * i + 2;   // индекс правого потомка
                int largest = i;         // по умолчанию, текущий элемент самый большой

                // Если левый потомок существует и больше текущего элемента
                if (left < n && heap.get(left) > heap.get(largest))
                    largest = left;
                // Если правый потомок существует и больше текущего элемента (или левого потомка)
                if (right < n && heap.get(right) > heap.get(largest))
                    largest = right;

                // Если самый большой элемент не совпадает с текущим,
                // меняем их местами и продолжаем просеивание
                if (largest != i) {
                    swap(i, largest);
                    i = largest;
                } else {
                    break; // Если нарушений нет, выходим из цикла.
                }
            }
            return i;
        }

        /**
         * Метод siftUp (просеивание вверх).
         * Его вызывают сразу после вставки нового элемента в конец массива.
         * Если новый элемент больше своего родителя, он поднимается вверх до восстановления
         * свойства кучи.
         *
         * @param i индекс вставленного элемента.
         * @return конечный индекс элемента после просеивания вверх.
         */
        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2; // индекс родителя
                if (heap.get(i) > heap.get(parent)) {
                    swap(i, parent);
                    i = parent; // переходим к родителю для дальнейшего сравнения
                } else {
                    break;
                }
            }
            return i;
        }

        // Метод для обмена значений двух элементов в куче по их индексам.
        void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        /**
         * Метод insert добавляет новое значение в кучу.
         * Элемент помещается в конец массива, после чего происходит просеивание вверх,
         * чтобы восстановить свойство max-кучи.
         *
         * @param value число, которое требуется добавить.
         */
        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        /**
         * Метод extractMax извлекает (и удаляет) максимальный элемент из кучи.
         * Максимальный элемент всегда находится в корне (индекс 0).
         * После его удаления последний элемент перемещается в корень и просеивается вниз.
         *
         * @return максимальное значение или null, если куча пуста.
         */
        Long extractMax() {
            if (heap.isEmpty()) return null;
            Long max = heap.get(0);
            Long last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                siftDown(0);
            }
            return max;
        }
    }
}
