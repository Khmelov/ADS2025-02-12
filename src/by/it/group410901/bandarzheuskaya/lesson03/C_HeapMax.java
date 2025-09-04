package by.it.group410901.bandarzheuskaya.lesson03;

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

    long findMaxValue(InputStream stream) {
        long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        int count = scanner.nextInt();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                long res = heap.extractMax();
                if (res > maxValue) maxValue = res;
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

    // Внутренний класс MaxHeap теперь статический
    private static final class MaxHeap {
        private final List<Long> heap = new ArrayList<>(); // Поле heap теперь final

        // Просеивание вверх (теперь void, так как возвращаемое значение не используется)
        void siftUp(int i) {
            while (i > 0 && heap.get(i) > heap.get((i - 1) / 2)) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
        }

        // Просеивание вниз (теперь void, так как возвращаемое значение не используется)
        void siftDown() { // Параметр i удален, так как всегда равен 0
            int i = 0;
            while (true) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int largest = i;

                if (left < heap.size() && heap.get(left) > heap.get(largest))
                    largest = left;

                if (right < heap.size() && heap.get(right) > heap.get(largest))
                    largest = right;

                if (largest == i)
                    break;

                swap(i, largest);
                i = largest;
            }
        }

        // Вставка элемента в кучу
        void insert(long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        // Извлечение и удаление максимального элемента
        long extractMax() {
            if (heap.isEmpty()) throw new IllegalStateException("Heap is empty");

            long max = heap.getFirst(); // Используем getFirst() вместо heap.get(0)
            heap.set(0, heap.getLast()); // Используем getLast() вместо heap.get(heap.size() - 1)
            heap.removeLast(); // Используем removeLast() вместо heap.remove(heap.size() - 1)

            if (!heap.isEmpty()) {
                siftDown();
            }

            return max;
        }

        // Вспомогательный метод для обмена элементов
        private void swap(int i, int j) {
            long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }
}