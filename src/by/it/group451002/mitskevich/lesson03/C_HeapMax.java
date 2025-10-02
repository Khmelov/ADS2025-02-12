package by.it.group451002.mitskevich.lesson03;

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

    // Эта процедура читает данные из файла, её можно не менять.
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        // Прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println(res);
                i++;
            }
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1]));
                i++;
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        private List<Long> heap = new ArrayList<>();

        // Просеивание вниз (siftDown)
        int siftDown(int i) {
            int size = heap.size();
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            if (left < size && heap.get(left) > heap.get(largest)) {
                largest = left;
            }

            if (right < size && heap.get(right) > heap.get(largest)) {
                largest = right;
            }

            if (largest != i) {
                // Меняем местами
                long temp = heap.get(i);
                heap.set(i, heap.get(largest));
                heap.set(largest, temp);
                siftDown(largest); // Рекурсивный вызов для восстановления кучи
            }

            return i;
        }

        // Просеивание вверх (siftUp)
        int siftUp(int i) {
            while (i > 0 && heap.get(i) > heap.get((i - 1) / 2)) {
                // Меняем местами
                long temp = heap.get(i);
                heap.set(i, heap.get((i - 1) / 2));
                heap.set((i - 1) / 2, temp);
                i = (i - 1) / 2; // Переходим к родительскому элементу
            }
            return i;
        }

        // Вставка
        void insert(Long value) {
            heap.add(value); // Добавляем элемент в конец
            siftUp(heap.size() - 1); // Восстанавливаем свойства кучи
        }

        // Извлечение максимума
        Long extractMax() {
            if (heap.size() == 0) {
                return null; // Если куча пуста, возвращаем null
            }

            // Максимум - это корень кучи
            long max = heap.get(0);

            // Перемещаем последний элемент в корень
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);

            // Восстанавливаем свойства кучи
            siftDown(0);

            return max;
        }
    }
}
