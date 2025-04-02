package by.it.group410901.kliaus.lesson02;
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

    double calc(InputStream input) throws FileNotFoundException {
        Scanner scanner = new Scanner(input);
        int itemsCount = scanner.nextInt();
        int capacity = scanner.nextInt();
        Item[] items = new Item[itemsCount];

        for (int i = 0; i < itemsCount; i++) {
            items[i] = new Item(scanner.nextInt(), scanner.nextInt());
        }

        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", itemsCount, capacity);

        Arrays.sort(items);
        double maxValue = 0;
        int currentWeight = 0;

        for (Item item : items) {
            if (currentWeight + item.weight > capacity) {
                double fraction = (double)(capacity - currentWeight) / item.weight;
                maxValue += item.cost * fraction;
                break;
            }
            maxValue += item.cost;
            currentWeight += item.weight;
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", maxValue);
        return maxValue;
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
            return "Item{cost=" + cost + ", weight=" + weight + "}";
        }

        @Override
        public int compareTo(Item other) {
            double ratioThis = (double)cost / weight;
            double ratioOther = (double)other.cost / other.weight;
            return Double.compare(ratioOther, ratioThis);
        }
    }
}