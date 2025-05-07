package by.it.group451004.rak.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


// Задача: построить max-кучу = пирамиду = бинарное сбалансированное дерево на массиве.
// ВАЖНО! НЕЛЬЗЯ ИСПОЛЬЗОВАТЬ НИКАКИЕ КОЛЛЕКЦИИ, КРОМЕ ARRAYLIST (его можно, но только для массива)

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

    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
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

    private class MaxHeap {
        private List<Long> heap = new ArrayList<>();

        void siftDown(int i) {
            int kid1 = i * 2 + 1;
            int kid2 = i * 2 + 2;
            if (kid1 < heap.size()){
                if (heap.get(kid1) > heap.get(i))
                {
                    int maxKid = kid1;
                    if (kid2 < heap.size() && heap.get(kid2) > heap.get(kid1)){
                        maxKid = kid2;
                    }
                    Long temp = heap.get(maxKid);
                    heap.set(maxKid, heap.get(i));
                    heap.set(i, temp);
                    siftDown(maxKid);
                } else if (kid2 < heap.size() && heap.get(kid2) > heap.get(i)) {
                    Long temp = heap.get(kid2);
                    heap.set(kid2, heap.get(i));
                    heap.set(i, temp);
                    siftDown(kid2);
                }
            }
        }

        void siftUp(int i) {
            int parent = (i - 1)/ 2;
            while (i > 0 && heap.get(parent) < heap.get(i)) {
                Long temp = heap.get(parent);
                heap.set(parent, heap.get(i));
                heap.set(i, temp);
                i = parent;
                parent = (i - 1) / 2;
            }
        }

        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        Long extractMax() {
            Long result = null;
            result = heap.get(0);
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);
            siftDown(0);
            return result;
        }
    }
}
