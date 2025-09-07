package by.it.group451004.akbulatov.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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
        int n = input.nextInt();
        int W = input.nextInt();

        C_GreedyKnapsack.Item[] items = new C_GreedyKnapsack.Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new C_GreedyKnapsack.Item(input.nextInt(), input.nextInt());
        }

        for (C_GreedyKnapsack.Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        double result = 0;

        Arrays.sort(items);
        for (C_GreedyKnapsack.Item item : items) {
            if (item.weight <= W) {
                result += item.cost;
                W -= item.weight;
            } else {
                result += (double) (item.cost * W) / item.weight;
                break;
            }
        }


        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item implements Comparable<C_GreedyKnapsack.Item> {
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
        public int compareTo(C_GreedyKnapsack.Item e) {
            return (e.cost / e.weight) - (this.cost / this.weight);
        }
    }
}