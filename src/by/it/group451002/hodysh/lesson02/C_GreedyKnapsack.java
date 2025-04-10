package by.it.group451002.hodysh.lesson02;

import java.io.InputStream;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");

        // Проверка на null
        if (inputStream == null) {
            throw new IllegalArgumentException("Файл 'greedyKnapsack.txt' не найден.");
        }

        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      // сколько предметов в файле
        int W = input.nextInt();      // какой вес у рюкзака
        Item[] items = new Item[n];   // получим список предметов

        for (int i = 0; i < n; i++) { // создаем каждый предмет
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        // Покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // Сортируем предметы по убыванию стоимости за единицу веса
        java.util.Arrays.sort(items, (item1, item2) -> Double.compare(item2.costPerWeight(), item1.costPerWeight()));

        // Алгоритм жадного выбора для рюкзака
        double result = 0;
        int remainingWeight = W;  // Оставшийся вес рюкзака

        for (Item item : items) {
            if (remainingWeight == 0) {
                break; // Рюкзак уже полный
            }

            if (item.weight <= remainingWeight) {
                // Если предмет помещается полностью
                result += item.cost;
                remainingWeight -= item.weight;
            } else {
                // Если предмет не помещается полностью, берем только часть
                result += item.costPerWeight() * remainingWeight;
                remainingWeight = 0;  // Рюкзак больше не может вмещать другие предметы
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

        // Метод для вычисления стоимости на единицу веса
        public double costPerWeight() {
            return (double) cost / weight;
        }

        @Override
        public int compareTo(Item o) {
            // Сортировка по стоимости на единицу веса
            return Double.compare(o.costPerWeight(), this.costPerWeight());
        }
    }
}