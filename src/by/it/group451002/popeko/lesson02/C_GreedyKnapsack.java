package by.it.group451002.popeko.lesson02;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
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
        int W = input.nextInt();      // вес рюкзака
        Item[] items = new Item[n];   // создаём массив предметов

        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        // Сортируем предметы по убыванию ценности на единицу веса
        Arrays.sort(items, Comparator.comparingDouble(Item::valuePerWeight).reversed());

        double result = 0.0;
        int currentWeight = 0;

        for (Item item : items) {
            if (currentWeight < W) {
                int availableWeight = W - currentWeight;
                if (item.weight <= availableWeight) {
                    result += item.cost;
                    currentWeight += item.weight;
                } else {
                    // Берём часть предмета
                    result += item.valuePerWeight() * availableWeight;
                    currentWeight += availableWeight;
                }
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        // Метод для вычисления ценности на единицу веса
        double valuePerWeight() {
            return (double) cost / weight;
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
