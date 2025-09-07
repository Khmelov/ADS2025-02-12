package by.it.group451002.kravtsov.lesson2;
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
        // Вывод общей информации о количестве предметов и вместимости рюкзака
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // Результат, который будем вычислять (суммарная стоимость собранных предметов)
        double result = 0;
        Arrays.sort(items);

        // Текущий вес рюкзака (начинается с 0)
        int currentWeight = 0;

        // Перебираем отсортированные предметы
        for (Item item : items) {
            // Если предмет полностью помещается в оставшееся место рюкзака
            if (currentWeight + item.weight <= W) {
                result += item.cost;             // Увеличиваем итоговую стоимость
                currentWeight += item.weight;   // Увеличиваем текущий вес рюкзака
            } else {
                // Если предмет не помещается полностью, добавляем только его часть
                int remainingWeight = W - currentWeight; // Сколько места осталось
                result += item.cost * ((double) remainingWeight / item.weight); // Пропорционально весу добавляем стоимость
                break; // Рюкзак полностью заполнен, выходим из цикла
            }
        }

        // Выводим итоговую стоимость собранного рюкзака
        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);

        // Возвращаем итоговую стоимость собранных предметов
        return result;
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

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

        @Override
        public int compareTo(Item o) {
            //тут может быть ваш компаратор


            return Double.compare(o.valuePerWeight(), this.valuePerWeight());
        }
    }
}