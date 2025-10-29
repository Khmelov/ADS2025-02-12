package by.it.group410902.latipov.lesson02;

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
        int n = input.nextInt();      //сколько предметов в файле
        int W = input.nextInt();      //какой вес у рюкзака
        Item[] items = new Item[n];   //получим список предметов
        for (int i = 0; i < n; i++) { //создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        //покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // Реализация жадного алгоритма для непрерывного рюкзака
        double result = 0;

        // Сортируем предметы по убыванию стоимости за единицу веса
        Arrays.sort(items);

        int remainingWeight = W; // оставшаяся вместимость рюкзака

        // Проходим по отсортированным предметам и добавляем в рюкзак
        for (Item item : items) {
            if (remainingWeight <= 0) break; // если рюкзак заполнен

            if (item.weight <= remainingWeight) {
                // Если предмет полностью помещается - берем целиком
                result += item.cost;
                remainingWeight -= item.weight;
            } else {
                // Если не помещается - берем часть предмета
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                remainingWeight = 0; // рюкзак заполнен
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

        // Метод для вычисления стоимости за единицу веса
        public double getCostPerUnit() {
            return (double) cost / weight;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    ", costPerUnit=" + String.format("%.2f", getCostPerUnit()) +
                    '}';
        }

        @Override
        public int compareTo(Item o) {
            // Сортируем по убыванию стоимости за единицу веса
            // Чтобы сначала брать самые "выгодные" предметы
            return Double.compare(o.getCostPerUnit(), this.getCostPerUnit());
        }
    }
}