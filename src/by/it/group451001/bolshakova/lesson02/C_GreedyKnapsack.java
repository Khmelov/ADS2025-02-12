package by.it.group451001.bolshakova.lesson02;
/*
Даны
1) объем рюкзака 4
2) число возможных предметов 60
3) сам набор предметов
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
import java.util.Arrays;

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
        Arrays.sort(items);


        double result = 0.0;
        int currentWeight = 0;

        // Итерация по отсортированным предметам
        for (Item item : items) {
            if (currentWeight < W) {
                // Если можем взять целиком, берём весь предмет
                if (currentWeight + item.weight <= W) {
                    currentWeight += item.weight;
                    result += item.cost;
                } else {
                    // Иначе берём только ту часть, которая помещается в рюкзак
                    int remainingWeight = W - currentWeight;
                    double fraction = ((double)remainingWeight) / item.weight;
                    result += item.cost * fraction;
                    currentWeight = W; // Рюкзак заполнен
                }
            } else {
                break; // Если рюкзак уже заполнен, выходим из цикла
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
        // Метод для вычисления плотности (стоимость за единицу веса)
        private double density() {
            return (double) cost / weight;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    ", density=" + density() +
                    '}';
        }

        @Override
        public int compareTo(Item other) {
            // Сортировка по убыванию плотности.
            // Если плотность текущего предмета больше, чем у другого, то он должен идти раньше.
            double thisDensity = this.density();
            double otherDensity = other.density();
            if (thisDensity < otherDensity)
                return 1;
            else if (thisDensity > otherDensity)
                return -1;
            else
                return 0;
        }
    }
}