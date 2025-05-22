package by.it.group451002.morozov.lesson03;

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

        int siftDown(int i) { //просеивание вниз
        	if (i < 0) return (this.heap.size()-1)/2;
	        int j = 0;
	        while (i*2+2 < this.heap.size()) {
		        long l = this.heap.get(i*2+1);
		        long r = this.heap.get(i*2+2);
	
		        long max = l > r ? l : r;
		        int maxInd = l > r ? i*2+1 : i*2+2;
		        if (this.heap.get(i) < max) {
		        	long temp = max;
		        	this.heap.set(maxInd, this.heap.get(i));
		        	this.heap.set(i, temp);
		        	
		        	i *= 2;
		        } else {
		        	j = i;
		        	i = this.heap.size();
		        }
		    }
	        return j;
        }

        int siftUp(int i) { //просеивание вверх
        	if ((i < 0) || (i >= this.heap.size())) return 0;
        	
        	int j = 0;
        	while (i > 0) {
		        if (this.heap.get(i/2) < this.heap.get(i)) {
		        	long temp = this.heap.get(i/2);
		        	this.heap.set(i/2, this.heap.get(i));
		        	this.heap.set(i, temp);
		        	i /= 2;
		        } else {
		        	j = i;
		        	i = 0; 
		        }
        	}
        	return j;
        }

        void insert(Long value) { //вставка
        	this.heap.add(value);
        	this.siftUp(this.heap.size()-1);
        }

        Long extractMax() { //извлечение и удаление максимума
            if (this.heap.size() != 0) return this.heap.get(0);
            else return 0L;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
