package by.it.group410901.garkusha.lesson02;
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

        // Сортируем предметы по стоимости на единицу веса в порядке убывания
        Arrays.sort(items, Comparator.comparingDouble(Item::getCostPerWeight).reversed());

        double result = 0; // Итоговая стоимость
        int currentWeight = 0; // Текущий вес рюкзака

        for (Item item : items) {
            if (currentWeight + item.weight <= W) {
                // Если весь предмет помещается в рюкзак
                currentWeight += item.weight;
                result += item.cost; // Добавляем полную стоимость предмета
            } else {
                // Если предмет не помещается целиком, берем только часть
                int remainingWeight = W - currentWeight; // Остаток веса в рюкзаке
                result += item.getCostPerWeight() * remainingWeight; // Добавляем стоимость части предмета
                break; // Рюкзак полон
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
        double getCostPerWeight() {
            return (double) cost / weight; // Возвращаем стоимость за единицу веса
        }
        @Override
        public int compareTo(Item o) {
            return Double.compare(this.getCostPerWeight(), o.getCostPerWeight());
        }
    }
}