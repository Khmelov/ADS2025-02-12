package by.it.group451003.yepanchuntsau.lesson02;

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
        System.out.printf("Общая стоимость %f (время %d)\n", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();
        int W = input.nextInt();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        Arrays.sort(items);
        double result = 0;
        int remaining = W;
        for (Item item : items) {
            if (remaining == 0) break;
            if (item.weight <= remaining) {
                result += item.cost;
                remaining -= item.weight;
            } else {
                result += item.cost * (remaining / (double) item.weight);
                remaining = 0;
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
            return "Item{" + "cost=" + cost + ", weight=" + weight + '}';
        }

        @Override
        public int compareTo(Item o) {
            // сравнение по убыванию стоимости на единицу веса
            double r1 = this.cost / (double) this.weight;
            double r2 = o.cost / (double) o.weight;
            return Double.compare(r2, r1);
        }
    }
}
