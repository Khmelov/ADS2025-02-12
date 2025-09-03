package by.it.group451001.yarkovich.lesson02;

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
        int n = input.nextInt();      // количество предметов
        int W = input.nextInt();      // вместимость рюкзака
        Item[] items = new Item[n];   // массив предметов
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        // Показ предметов
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // Сортируем предметы по убыванию удельной стоимости (cost / weight)
        Arrays.sort(items);

        double result = 0;  // Максимальная стоимость собранного рюкзака
        int remainingWeight = W;  // Остаток вместимости рюкзака

        for (Item item : items) {
            if (remainingWeight == 0) break;  // Если рюкзак полон, завершаем
            if (item.weight <= remainingWeight) {
                // Если предмет полностью помещается в рюкзак
                result += item.cost;
                remainingWeight -= item.weight;
            } else {
                // Если предмет можно взять только частично
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                remainingWeight = 0;  // Рюкзак заполнен
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    // Класс предметов с компаратором по удельной стоимости
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
            // Сравнение по удельной стоимости (по убыванию)
            double thisValuePerWeight = (double) this.cost / this.weight;
            double otherValuePerWeight = (double) o.cost / o.weight;
            return Double.compare(otherValuePerWeight, thisValuePerWeight);
        }
    }
}
