package by.it.group410901.zaverach.lesson02;
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
import java.util.Arrays;
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

        //ваше решение.


        for (int i = 0; i <= n-1; i++) {
            items[i].price = items[i].cost / items[i].weight;
        }
        quickSort(items, 0, items.length - 1);
        double sumw=0;
        int i=0;
        while (sumw!=W) {
            if (W - sumw > items[i].weight) {
                sumw += items[i].weight;
                result +=items[i].cost;
            } else {
                result +=items[i].price*(W-sumw);
                sumw +=W-sumw;

            }
            i++;
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    public static void quickSort(Item[] items, int low, int high) {
        if (low < high) {
            // Находим индекс опорного элемента (pivot)
            int pivotIndex = partition(items, low, high);

            // Рекурсивно сортируем левую и правую части
            quickSort(items, low, pivotIndex - 1);
            quickSort(items, pivotIndex + 1, high);
        }
    }

    private static int partition(Item[] items, int low, int high) {
        // Используем последний элемент как опорный (pivot)
        Item pivot = items[high];
        int i = low - 1; // Индекс меньшего элемента

        for (int j = low; j < high; j++) {
            // Если текущий элемент меньше или равен pivot
            if (items[j].compareTo(pivot) <= 0) {
                i++;
                // Меняем местами items[i] и items[j]
                swap(items, i, j);
            }
        }

        // Меняем местами items[i + 1] и pivot (items[high])
        swap(items, i + 1, high);

        return i + 1; // Возвращаем индекс опорного элемента
    }

    private static void swap(Item[] items, int i, int j) {
        Item temp = items[i];
        items[i] = items[j];
        items[j] = temp;
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;
        double price;

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
            // Компаратор для сортировки в убывающем порядке
            return Double.compare(o.price, this.price);
        }
    }
}