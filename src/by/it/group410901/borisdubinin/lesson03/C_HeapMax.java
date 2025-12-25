package by.it.group410901.borisdubinin.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Lesson 3. C_Heap.
// Задача: построить max-кучу = пирамиду = бинарное сбалансированное дерево на массиве.
// ВАЖНО! НЕЛЬЗЯ ИСПОЛЬЗОВАТЬ НИКАКИЕ КОЛЛЕКЦИИ, КРОМЕ ARRAYLIST (его можно, но только для массива)

//      Проверка проводится по данным файла
//      Первая строка входа содержит число операций 1 ≤ n ≤ 100000.
//      Каждая из последующих nn строк задают операцию одного из следующих двух типов:

//      Insert x, где 0 ≤ x ≤ 1000000000 — целое число;
//      ExtractMax.

//      Первая операция добавляет число x в очередь с приоритетами,
//      вторая — извлекает максимальное число и выводит его.

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
        System.out.println("MAX=" + instance.findMax(stream));
    }

    Long findMax(InputStream stream) {
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
                Long t = heap.get(parent);
                heap.set(parent, heap.get(i));
                heap.set(i, t);
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
