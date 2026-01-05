package by.it.group410902.varava.lesson02.lesson01.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        int costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %d (время %d)", costFinal, finishTime - startTime);
    }

    int calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();
        int W = input.nextInt();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        for (int i = 0; i < items.length - 1; i++) {
            for (int j = i + 1; j < items.length; j++) {
                if ((items[j].cost/items[j].weight) > (items[i].cost/items[i].weight)) {
                    Item temp = items[i];
                    items[i] = items[j];
                    items[j] = temp;
                }
            }
        }

        int result = 0;
        List<Item> selectedItems = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            if (items[i].weight <= W && items[i].weight != 0) {
                selectedItems.add(items[i]);
                result += items[i].cost;
                W -= items[i].weight;
            } else if (items[i].weight > W) {
                items[i].cost = (int) Math.round(items[i].cost * ((double) W / items[i].weight));
                items[i].weight = W;
                selectedItems.add(items[i]);
                result += items[i].cost;
                break;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %d\n", result);
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
            return "Item{" + "cost=" + cost + ", weight=" + weight + '}';
        }

        @Override
        public int compareTo(Item o) {
            return Double.compare((double) o.cost / o.weight, (double) this.cost / this.weight);
        }
    }
}
