package by.it.group451002.Osadchy.lesson02;
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

        // Сортируем предметы по убыванию удельной стоимости (cost / weight)
        Arrays.sort(items, (o1, o2) -> {
            double ratio1 = (double) o1.cost / o1.weight;
            double ratio2 = (double) o2.cost / o2.weight;
            return Double.compare(ratio2, ratio1); // Сортировка по убыванию
        });

        double result = 0;
        int remainingWeight = W; // Оставшийся вес в рюкзаке

        for (Item item : items) {
            if (remainingWeight <= 0) {
                break; // Рюкзак заполнен
            }
            if (item.weight <= remainingWeight) {
                // Берем предмет целиком
                result += item.cost;
                remainingWeight -= item.weight;
            } else {
                // Берем часть предмета
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                remainingWeight = 0;
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
            double ratio1 = (double) this.cost / this.weight;
            double ratio2 = (double) o.cost / o.weight;
            return Double.compare(ratio2, ratio1); // Сортировка по убыванию
        }
    }
}