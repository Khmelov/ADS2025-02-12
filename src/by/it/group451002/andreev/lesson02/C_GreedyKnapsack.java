package by.it.group451002.andreev.lesson02;

/*
 * Задача: Собрать наиболее дорогой вариант рюкзака для заданного объема.
 * Предметы можно резать на кусочки. Алгоритм жадный.
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
        int n = input.nextInt(); // количество предметов в файле
        int W = input.nextInt(); // вес рюкзака
        Item[] items = new Item[n]; // массив предметов
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        // вывод предметов
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        double result = 0; // итоговая стоимость
        mergeSort(items); // сортировка предметов по убыванию ценности на единицу веса
        for (Item item : items) {
            System.out.println(item);
        }
        int i = 0;
        // добавление целых предметов в рюкзак
        while (i < items.length && W >= items[i].weight) {
            result += items[i].cost;
            W -= items[i].weight;
            i++;
        }
        // добавление части последнего предмета
        if (i < items.length)
            result += (double) items[i].cost / items[i].weight * W;

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    // методы для сортировки слиянием
    void mergeSort(Item[] items) {
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

    void merge(Item[] a, Item[] l, Item[] r, int left, int right) {
        int i = 0, j = 0;
        while (i < left && j < right) {
            // компаратор для сравнения ценности на единицу веса
            if (l[i].compareTo(r[j]) > 0) {
                a[i + j] = l[i++];
            } else {
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

    // класс для предметов
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
            // сравнение по ценности на единицу веса
            if ((double) this.cost / this.weight > (double) o.cost / o.weight)
                return 1;
            else if ((double) this.cost / this.weight == (double) o.cost / o.weight)
                return 0;
            else
                return -1;
        }
    }
}
