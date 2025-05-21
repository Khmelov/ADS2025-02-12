package by.it.group410901.volkov.lesson02;
/*
Даны
1) объем рюкзака 4
2) число возможных предметов 60
3) сам набор предметов
    60  20
    100 50
    120 30
    100 50
Все это указано в файле (by/it/a_khmelev/lesson02/greedyKnapsack.txt)

Необходимо собрать наиболее дорогой вариант рюкзака для этого объема
Предметы можно резать на кусочки (т.е. алгоритм будет жадным)
 */

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        // Сортируем предметы (по убыванию удельной стоимости)
        sortItems(items);

        // Выводим отсортированные предметы
        System.out.println("\nОтсортированные предметы:");
        for (Item item : items) {
            System.out.println(item);
        }

        double result = 0;
        int remainingWeight = W;

        for (Item item : items) {
            if (remainingWeight <= 0) break;

            if (item.weight <= remainingWeight) {
                // Берем предмет целиком
                result += item.cost;
                remainingWeight -= item.weight;
                System.out.printf("Взяли целиком: %s. Осталось места: %d\n",
                        item, remainingWeight);
            } else {
                // Берем часть предмета
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                System.out.printf("Взяли часть (%.2f) от %s. Сумма: %.2f\n",
                        fraction, item, item.cost * fraction);
                remainingWeight = 0;
            }
        }

        System.out.printf("\nИтоговая стоимость: %.2f\n", result);
        return result;
    }

// Сортировка пузырьком
private void sortItems(Item[] items) {
    for (int i = 0; i < items.length - 1; i++) {
        for (int j = 0; j < items.length - i - 1; j++) {
            if (items[j].compareTo(items[j + 1]) > 0) {
                Item temp = items[j];
                items[j] = items[j + 1];
                items[j + 1] = temp;
            }
        }
    }
}

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        double getValuePerUnit() {
            return (double) cost / weight;
        }



        @Override
        public String toString() {
            return "Item{" +
                   "cost=" + cost +
                   ", weight=" + weight +
                    ", value/kg=" + getValuePerUnit() +
                   '}';
        }

        @Override
        public int compareTo(Item other) {
            return Double.compare(other.getValuePerUnit(), this.getValuePerUnit());
        }
    }
}