package by.it.group410901.kovalevich.lesson02;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
            double costFinal = new C_GreedyKnapsack().calc(inputStream);
            long finishTime = System.currentTimeMillis();
            System.out.printf("Общая стоимость %f (время %d ms)%n", costFinal, finishTime - startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    double calc(InputStream inputStream) {
        Scanner input = new Scanner(inputStream);

        int n = input.nextInt(); // количество предметов
        int W = input.nextInt(); // вместимость рюкзака

        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        // Показать предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.%n", n, W);

        // Сортировка предметов по убыванию ценности на единицу веса
        Arrays.sort(items);

        double result = 0; // Итоговая стоимость
        int currentWeight = 0; // Текущий вес рюкзака

        for (Item item : items) {
            if (currentWeight + item.weight <= W) {
                // Если предмет помещается целиком
                currentWeight += item.weight;
                result += item.cost;
            } else {
                // Если предмет помещается частично
                int remainingWeight = W - currentWeight;
                result += item.valuePerWeight() * remainingWeight;
                break; // Рюкзак заполнен
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f%n", result);
        return result;
    }

    // Класс для хранения информации о предмете
    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        // Стоимость на единицу веса
        double valuePerWeight() {
            return (double) cost / weight;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    ", valuePerWeight=" + valuePerWeight() +
                    '}';
        }

        @Override
        public int compareTo(Item o) {
            // Сортировка по убыванию ценности на единицу веса
            return Double.compare(o.valuePerWeight(), this.valuePerWeight());
        }
    }
}