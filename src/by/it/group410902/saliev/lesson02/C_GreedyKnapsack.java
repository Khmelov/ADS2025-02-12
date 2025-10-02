package by.it.group410902.saliev.lesson02;

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
        int n = input.nextInt();      // сколько предметов
        int W = input.nextInt();      // вместимость рюкзака
        Item[] items = new Item[n];   // массив предметов

        // считываем все предметы
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        // показываем предметы
        for (Item item : items) {
            System.out.println(item);
        }

        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        double result = 0; // итоговая стоимость

        // сортируем предметы по убыванию "ценности" (стоимость за 1 кг)
        Arrays.sort(items);

        int currentWeight = 0;

        for (Item item : items) {
            if (currentWeight + item.weight <= W) {
                // полностью кладём предмет
                result += item.cost;
                currentWeight += item.weight;
            } else {
                // кладём только часть предмета
                int remaining = W - currentWeight;
                result += item.costPerKg() * remaining;
                break; // рюкзак заполнен
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

        // возвращает стоимость за 1 кг
        double costPerKg() {
            return (double) cost / weight;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    ", cost/kg=" + String.format("%.2f", costPerKg()) +
                    '}';
        }

        @Override
        public int compareTo(Item o) {
            // сортировка по убыванию стоимости за кг
            return Double.compare(o.costPerKg(), this.costPerKg());
        }
    }
}
