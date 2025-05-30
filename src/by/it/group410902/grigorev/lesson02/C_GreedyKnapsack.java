package by.it.group410902.grigorev.lesson02;
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

        // Читаем данные о предметах и рюкзаке из файла "greedyKnapsack.txt"
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        // Сканер для чтения данных из входного файла
        Scanner input = new Scanner(inputStream);

        // Читаем количество предметов и объем рюкзака
        int n = input.nextInt();      // Сколько предметов в файле
        int W = input.nextInt();      // Какой вес (объем) у рюкзака

        // Создаем массив объектов Item, представляющих предметы
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt()); // Создаем предмет с ценой и весом
        }

        // Выводим список всех предметов
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // Реализация алгоритма "жадного рюкзака"
        double result = 0;

        // Сортируем предметы по удельной стоимости (цене на единицу веса)
        Arrays.sort(items);

        // Заполняем рюкзак
        int currentWeight = 0; // Текущий вес рюкзака
        for (Item item : items) {
            if (currentWeight + item.weight <= W) {
                // Если предмет помещается целиком, добавляем его стоимость
                result += item.cost;
                currentWeight += item.weight;
            } else {
                // Если предмет не помещается целиком, берем только часть
                int remainingWeight = W - currentWeight; // Остаток места в рюкзаке
                result += item.costPerUnit() * remainingWeight; // Учитываем частичную стоимость
                break; // Рюкзак полностью заполнен
            }
        }

        // Выводим результат
        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result; // Возвращаем итоговую стоимость
    }

    // Класс Item, представляющий предмет
    private static class Item implements Comparable<Item> {
        int cost; // Стоимость предмета
        int weight; // Вес предмета

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        // Метод для вычисления удельной стоимости (цена на единицу веса)
        double costPerUnit() {
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
            // Сортируем предметы по убыванию удельной стоимости
            return Double.compare(o.costPerUnit(), this.costPerUnit());
        }
    }
}