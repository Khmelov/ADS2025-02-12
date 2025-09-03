package by.it.group451002.sidarchuk.lesson03;

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

        // Вставка элемента

        int siftDown(int i) { //просеивание вниз
            int size = heap.size();
            while (true) {
                int largestIndex = i; // предполагаем, что текущий элемент наибольший
                int leftChildIndex = 2*i+1; // Индекс левого ребенка
                int rightChildIndex = 2*i+2;// Индекс правого ребенка
                // проверяем, есть ли левый ребенок и больше ли он текущего
                if (leftChildIndex<size && heap.get(leftChildIndex)>heap.get(largestIndex)){
                    largestIndex=leftChildIndex;
                }
                // проверяем, есть ли правый ребенок и больше ли он текущего
                if (rightChildIndex<size && heap.get(rightChildIndex)> heap.get(largestIndex)){
                    largestIndex=rightChildIndex;
                }
                // если текущий элемент больше или равен наибольшему, выходим
                if (largestIndex==i){
                    break;
                }
                // Меняем местами текущий элемент и наибольший
                Long temp = heap.get(i);
                heap.set(i,heap.get(largestIndex));
                heap.set(largestIndex,temp);
                i=largestIndex; // переходим к наибольшему
            }
            return i;
        }

        int siftUp(int i) { //просеивание вверх
            while (i>0) {
                int parentIndex = (i-1)/2; // индекс родителя
                if (heap.get(i)>heap.get(parentIndex)) {
                    // меняем местами текущий элемент и его родителя
                    Long temp = heap.get(i);
                    heap.set(i, heap.get(parentIndex));
                    heap.set(parentIndex, temp);
                    i = parentIndex; // переходим к родителю
                } else {
                    break; // Если текущий элемент меньше или равен родителю, выходим
                }
            }
            return i;
        }
    // вставка элемента
    void insert(Long value) {
        heap.add(value); // Добавляем элемент в конец
        siftUp(heap.size()-1); // применяем siftUp для поддержания структуры кучи
    }
        Long extractMax() { //извлечение и удаление максимума
            if (heap.size()==0) return null; // если куча пуста
            Long max = heap.get(0);//максимальный элемент
            Long last = heap.remove(heap.size()-1); // удаляем последний элемент
            if (heap.size()>0) {
                heap.set(0, last); // перемещаем последний элемент на место корня
                siftDown(0);// применяем siftDown для поддержания структуры кучи
            }
            return max;
        }
}
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
