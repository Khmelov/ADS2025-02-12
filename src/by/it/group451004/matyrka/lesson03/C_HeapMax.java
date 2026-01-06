package by.it.group451004.matyrka.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class C_HeapMax {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_HeapMax.class.getResourceAsStream("dataC.txt");
        C_HeapMax instance = new C_HeapMax();
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < n; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("ExtractMax")) {
                Long x = heap.extractMax();
                if (x != null && x > maxValue) maxValue = x;
                System.out.println(x);
                i++;
            } else if (s.startsWith("Insert")) {
                heap.insert(Long.parseLong(s.split(" ")[1]));
                i++;
            }
        }
        return maxValue;
    }

    class MaxHeap {
        List<Long> heap = new ArrayList<>();

        void insert(Long x) {
            heap.add(x);
            siftUp(heap.size() - 1);
        }

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

        void siftUp(int i) {
            while (i > 0) {
                int p = (i - 1) / 2;
                if (heap.get(p) >= heap.get(i)) break;
                Collections.swap(heap, i, p);
                i = p;
            }
        }

        void siftDown(int i) {
            int n = heap.size();
            while (true) {
                int l = 2 * i + 1;
                int r = 2 * i + 2;
                int max = i;

                if (l < n && heap.get(l) > heap.get(max)) max = l;
                if (r < n && heap.get(r) > heap.get(max)) max = r;
                if (max == i) break;

                Collections.swap(heap, i, max);
                i = max;
            }
        }
    }
}
