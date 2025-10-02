package by.it.group451004.akbulatov.lesson03;

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
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        int count = scanner.nextInt();
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
            }
        }
        return maxValue;
    }

    private static class MaxHeap {
        private List<Long> heap = new ArrayList<>();
        int lastIndex = -1;

        void bubbleUp(int childIndex) {
            if (childIndex > 0) {
                int parentIndex = (childIndex - 1) / 2;
                Long parentValue = heap.get(parentIndex);
                if (parentValue < heap.get(childIndex)) {
                    heap.set(parentIndex, heap.set(childIndex, parentValue));
                    bubbleUp(parentIndex);
                }
            }
        }

        void bubbleDown() {
            int parentIndex = 0;
            while (true) {
                int rightIndex = 2 * parentIndex + 2;
                int leftIndex = 2 * parentIndex + 1;
                int largestIndex = parentIndex;

                if (leftIndex <= lastIndex && heap.get(leftIndex) > heap.get(largestIndex))
                    largestIndex = leftIndex;

                if (rightIndex <= lastIndex && heap.get(rightIndex) > heap.get(largestIndex))
                    largestIndex = rightIndex;

                if (largestIndex == parentIndex)
                    return;
                else
                    heap.set(parentIndex, heap.set(largestIndex, heap.get(parentIndex)));

                parentIndex = largestIndex;
            }
        }


        void insert(Long value) {
            lastIndex++;
            heap.add(value);
            bubbleUp(lastIndex);
        }

        Long extractMax() {
            if (heap.isEmpty())
                return null;

            Long result = heap.getFirst();
            heap.set(0, heap.remove(lastIndex--));
            bubbleDown();

            return result;
        }
    }
}
