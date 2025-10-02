package by.it.group451001.drzhevetskiy.lesson03;

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
        long maxV = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        int count = scanner.nextInt();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxV)
                    maxV = res;
                i++;
            }
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1]));
                i++;
            }
        }
        return maxV;
    }

    static private class MaxHeap {
        final private List<Long> heap = new ArrayList<>();

        void siftDown(int i) {
            int l = i*2 + 1;
            int r = i*2 + 2;
            int larg = i;

            if (l < heap.size() && heap.get(l) > heap.get(larg))
                larg = l;
            if (r < heap.size() && heap.get(r) > heap.get(larg))
                larg = r;

            if (larg != i) {
                swap(i, larg);
                siftDown(larg);
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

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}