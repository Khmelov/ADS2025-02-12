package by.it.group451001.serganovskij.lesson03;

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
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    //эта процедура читает данные из файла, ее можно не менять.
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        //прочитаем строку для кодирования из тестового файла
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
                //System.out.println(heap); //debug
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение.
        //Будет мало? Ну тогда можете его собрать как Generic и/или использовать в варианте B
        private List<Long> heap = new ArrayList<>();

        int maxValue = -1;

        int siftDown(int i) { //просеивание вверх
            int downIndex = i*2+1;
            if (maxValue > downIndex) //сущ 2 элемента ниже
            {
                if (heap.get(downIndex) > heap.get(downIndex+1))
                {
                    heap.set(i, heap.get(downIndex));
                    return downIndex;
                }
                else
                {
                    heap.set(i, heap.get(downIndex+1));
                    return downIndex+1;
                }
            }
            else if (maxValue == downIndex)
            {
                heap.set(i, heap.get(downIndex));
                return downIndex;
            }

            return i;
        }

        //берём индекс элемента смотрим кто над ним возращаем индекс его позиции
        int siftUp(int i) { //просеивание вниз
            if (i > 0)
            {
                int upIndex = (i-1)/2;
                if (heap.get(upIndex) < heap.get(i))
                {
                    heap.set(upIndex, heap.set(i, heap.get(upIndex)));
                    return upIndex;
                }
            }
            return i;
        }

        void insert(Long value) { //вставка
            heap.add(value); //добавили в конец
            maxValue++;
            int sift, preSift;
            sift = maxValue;
            do
            {
                preSift = sift;
                sift = siftUp(sift);
            }
            while (preSift != sift);
        }

        Long extractMax() { //извлечение и удаление максимума
            Long result;
            if (heap.size() > 0) {result = heap.get(0);}
            else {result = null;}
            int sift, preSift;
            sift = 0;
            do
            {
                preSift = sift;
                sift = siftDown(sift);
            }
            while (preSift != sift);
            heap.remove(maxValue);
            maxValue--;

            return result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
