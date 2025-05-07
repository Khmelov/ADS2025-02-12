package by.it.group451004.rak.lesson02;
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
Предметы можно резать на кусочки (т.е. Алгоритм будет жадным)
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
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);
        double result = 0;
        sortItems(items);

        int i = 0;
        while (i < items.length) {
            if (items[i].weight <= W) {
                result += items[i].cost;
                W -= items[i].weight;
            } else {
                result += items[i].cost * ((double) W / items[i].weight);
                i = items.length;
            }
            i++;
        }
        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    static void sortItems(Item[] items) {
        for (int i = 0; i < items.length / 2; i++) { //n/2 округляя вниз
            int maxIndex = i;
            int minIndex = items.length - i - 1;
            for (int j = i; j < items.length - i; j++) { //-1 лишнее, но без него ломается
                if (items[j].compareTo(items[maxIndex]) > 0) {
                    maxIndex = j;
                } else if (items[j].compareTo(items[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            if (maxIndex != i) {
                Item temp = items[i];
                items[i] = items[maxIndex];
                items[maxIndex] = temp;
                if (minIndex == i) { // Обновим minIndex, если он совпал с i (то есть переместили максимум, затронув минимум)
                    minIndex = maxIndex;
                }
            }
            if (minIndex != items.length - i - 1) {
                Item temp = items[items.length - i - 1];
                items[items.length - i - 1] = items[minIndex];
                items[minIndex] = temp;
            }
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
            if (((cost / weight) - (o.cost / o.weight)) > 0) {
                return 1;
            } else if (((cost / weight) - (o.cost / o.weight)) < 0) {
                return -1;
            } else {
                return Integer.compare(weight, o.weight);
            }
        }
    }
}