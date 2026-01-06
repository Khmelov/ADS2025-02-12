package by.it.group451004.matyrka.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class C_GreedyKnapsack {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream =
                C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double result = new C_GreedyKnapsack().calc(inputStream);
        System.out.printf("Общая стоимость %f\n", result);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();
        int W = input.nextInt();

        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        Arrays.sort(items);

        double result = 0;
        int capacity = W;

        for (Item item : items) {
            if (capacity == 0) break;

            int take = Math.min(capacity, item.weight);
            result += take * item.valuePerWeight();
            capacity -= take;
        }
        return result;
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        double valuePerWeight() {
            return (double) cost / weight;
        }

        @Override
        public int compareTo(Item o) {
            return Double.compare(o.valuePerWeight(), this.valuePerWeight());
        }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    '}';
        }
    }
}
