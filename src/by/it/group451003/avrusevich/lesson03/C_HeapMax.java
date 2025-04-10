package by.it.group451003.avrusevich.lesson03;
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

    Long findMaxValue(InputStream stream) {
        long maxValue = 0L;
        C_HeapMax.MaxHeap heap = new C_HeapMax.MaxHeap();
        Scanner scanner = new Scanner(stream);
        int count = scanner.nextInt();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue)
                    maxValue = res;
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

    static private class MaxHeap {
        final private List<Long> heap = new ArrayList<>();

        void siftDown(int i) {
            int left = i*2 + 1;
            int right = i*2 + 2;
            int largest = i;

            if (left < heap.size() && heap.get(left) > heap.get(largest))
                largest = left;
            if (right < heap.size() && heap.get(right) > heap.get(largest))
                largest = right;

            if (largest != i) {
                swap(i, largest);
                siftDown(largest);
            }

        }

        void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;

                if (heap.get(i) > heap.get(parent)) {
                    swap(i, parent);
                    i = parent;
                } else {
                    break;
                }
            }

        }


        void insert(Long value) {
            heap.add(value);

            int index = heap.size() - 1;
            siftUp(index);
        }

        Long extractMax() { //извлечение и удаление максимума
            if ( heap.isEmpty() )
                return null;


            Long result = heap.getFirst();
            heap.addFirst(heap.getLast());
            heap.removeLast();
            if ( !heap.isEmpty() )
                siftDown(0);


            return result;
        }

        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }



}
