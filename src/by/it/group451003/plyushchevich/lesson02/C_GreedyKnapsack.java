package by.it.group451003.plyushchevich.lesson02;

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
        System.out.printf("Общая стоимость %f (время %d мс)", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      // число предметов
        int W = input.nextInt();      // вместимость рюкзака (вес)
        Item[] items = new Item[n];   // создаем массив предметов
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        // Вывод исходных предметов
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // Сортировка предметов по убыванию отношения cost/weight
        Arrays.sort(items);

        double result = 0;
        int remainingCapacity = W;
        for (Item item : items) {
            if (remainingCapacity == 0) break;
            if (item.weight <= remainingCapacity) {
                result += item.cost;
                remainingCapacity -= item.weight;
            } else {
                result += item.costPerWeight() * remainingCapacity;
                remainingCapacity = 0;
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

        double costPerWeight() { return (double) cost / weight; }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    ", cost/weight=" + costPerWeight() +
                    '}';
        }

        @Override
        public int compareTo(Item other) {
            return -Double.compare(this.costPerWeight(), other.costPerWeight());
        }
    }
}
