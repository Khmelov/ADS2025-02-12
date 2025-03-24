package by.it.group451002.buyel.lesson02;
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

        // Инициализация
        double result = 0;
        Item firstItem = new Item(60, 20);
        Item secondItem = new Item(100, 50);
        Item thirdItem = new Item(120, 30);
        Item fourthItem = new Item(100, 50);

        Item[] priority = {firstItem, secondItem, thirdItem, fourthItem};
        Item tempSwap;

        // Сортировка по стоимости за 1 кг
        for (int i = 0; i < priority.length-1; i++) {
            for (int j = i+1; j < priority.length; j++) {
                if (priority[i].compareTo(priority[j]) > priority[j].compareTo(priority[i])) {
                    tempSwap = priority[i];
                    priority[i] = priority[j];
                    priority[j] = tempSwap;
                }
            }
        }

        // Записываем в result
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; (j < priority[i].weight) && (0 < 60-k); j++, k++) {
                result += (double) priority[i].cost /priority[i].weight;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
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
            //тут может быть ваш компаратор
            return o.cost / o.weight;
        }
    }
}
