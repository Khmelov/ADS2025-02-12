package by.it.group451002.shulmnn.lesson02;
/*
Даны
1) объем рюкзака 4
2) число возможных предметов 60
3) сам набор предметов
    100 50
    120 30
    100 50
Все это указано в файле (by/it/a_khmelev/lesson02/greedyKnapsack.txt)

Необходимо собрать наиболее дорогой вариант рюкзака для этого объема
Предметы можно резать на кусочки (т.е. алгоритм будет жадным)
 */

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      //сколько предметов в файле
        int W = input.nextInt();      //какой вес у рюкзака
        Item[] items = new Item[n];   //получим список предметов
        for (int i = 0; i < n; i++) { //создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        //покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        //тут необходимо реализовать решение задачи
        //итогом является максимально воможная стоимость вещей в рюкзаке
        //вещи можно резать на кусочки (непрерывный рюкзак)
        double result = 0;
        //тут реализуйте алгоритм сбора рюкзака
        //будет особенно хорошо, если с собственной сортировкой
        //кроме того, можете описать свой компаратор в классе Item
        mergeSort(items);
        for (Item item : items) {
            System.out.println(item);
        }
        int i = 0;
        while (i < items.length && W >= items[i].weight){
            result += items[i].cost;
            W -= items[i].weight;
            i++;
        }
        if(i < items.length)
            result += (double) items[i].cost / items[i].weight * W;
        //ваше решение.


        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }
    void mergeSort(Item[] items){
        mergeSort(items, items.length);
    }
    void mergeSort(Item[] a, int n) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        Item[] l = new Item[mid];
        Item[] r = new Item[n - mid];

        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        mergeSort(l, mid);
        mergeSort(r, n - mid);

        merge(a, l, r, mid, n - mid);
    }
    void merge(
            Item[] a, Item[] l, Item[] r, int left, int right) {

        int i = 0, j = 0;
        while (i < left && j < right) {
            if (l[i].compareTo(r[j]) > 0) {
                a[i + j] = l[i++];
            }
            else {
                a[i + j] = r[j++];
            }
        }
        while (i < left) {
            a[i + j] = l[i++];
        }
        while (j < right) {
            a[i + j] = r[j++];
        }
    }
    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Item{" +
                   "cost=" + cost +
                   ", weight=" + weight +
                   '}';
        }

        @Override
        public int compareTo(Item o) {
            if(this.cost / this.weight > o.cost / o.weight)
                return 1;
            else if(this.cost / this.weight == o.cost / o.weight)
                return 0;
            else
                return -1;
        }
    }
}