package by.it.group410902.andala.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        // Замер времени выполнения
        long startTime = System.currentTimeMillis();

        // Чтение входных данных из файла
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");

        // Вычисление максимальной стоимости
        double costFinal = new C_GreedyKnapsack().calc(inputStream);

        // Вывод результатов
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    /**
     * Метод для решения задачи о рюкзаке жадным алгоритмом
     * @param inputStream поток ввода с данными о предметах
     * @return максимальная стоимость предметов в рюкзаке
     * @throws FileNotFoundException если файл не найден
     */
    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);

        // Чтение количества предметов и вместимости рюкзака
        int n = input.nextInt();      // Количество предметов
        int W = input.nextInt();      // Вместимость рюкзака в кг

        // Создание массива предметов
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt()); // Стоимость и вес
        }

        // Вывод информации о предметах
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // Сортируем предметы по убыванию удельной стоимости (стоимость/вес)
        Arrays.sort(items);

        double result = 0;            // Общая стоимость предметов в рюкзаке
        int remainingWeight = W;      // Оставшаяся вместимость рюкзака

        // Жадный алгоритм - берем самые ценные предметы сначала
        for (Item item : items) {
            if (remainingWeight <= 0) break;  // Рюкзак заполнен

            if (item.weight <= remainingWeight) {
                // Если предмет помещается целиком - берем его
                result += item.cost;
                remainingWeight -= item.weight;
            } else {
                // Если не помещается - берем часть предмета
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                remainingWeight = 0;  // Рюкзак заполнен полностью
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    /**
     * Класс, представляющий предмет для рюкзака
     */
    private static class Item implements Comparable<Item> {
        int cost;    // Стоимость предмета
        int weight; // Вес предмета

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
            // Сравнение по убыванию удельной стоимости (стоимость/вес)
            double thisValuePerWeight = (double) this.cost / this.weight;
            double otherValuePerWeight = (double) o.cost / o.weight;
            return Double.compare(otherValuePerWeight, thisValuePerWeight);
        }
    }
}