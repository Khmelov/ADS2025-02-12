package by.it.group410902.kovalchuck.lesson01.lesson02;
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

        // Показ предметов
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        Arrays.sort(items);
        //тут необходимо реализовать решение задачи
        //итогом является максимально воможная стоимость вещей в рюкзаке
        //вещи можно резать на кусочки (непрерывный рюкзак)
        double result = 0;
        //тут реализуйте алгоритм сбора рюкзака
        //будет особенно хорошо, если с собственной сортировкой
        //кроме того, можете описать свой компаратор в классе Item

        //ваше решение.

        // Вычисление стоимости за единицу веса для каждого предмета
        double[] valuePerWeight = new double[n]; // Массив для хранения стоимости за единицу веса каждого предмета
        for (int i = 0; i < n; i++) {
            valuePerWeight[i] = (double) items[i].cost / items[i].weight;
            // Делим стоимость предмета (items[i].cost) на его вес (items[i].weight), чтобы получить стоимость за единицу веса
        }

// Сортировка предметов по убыванию стоимости за единицу веса
        Integer[] indices = new Integer[n]; // Массив индексов, чтобы отслеживать исходные предметы после сортировки
        for (int i = 0; i < n; i++) {
            indices[i] = i; // Инициализация индексов: каждый индекс соответствует предмету из массива items
        }

        Arrays.sort(indices, (i, j) -> Double.compare(valuePerWeight[j], valuePerWeight[i]));
// Сортируем массив индексов по убыванию стоимости за единицу веса (valuePerWeight).
// сравнивает элементы valuePerWeight для индексов j и i.
// В результате самые "выгодные" предметы (с максимальной стоимостью за единицу веса) окажутся в начале массива indices.

        for (int i = 0; i < n; i++) {
            int index = indices[i]; // Берем индекс очередного предмета из отсортированного массива индексов
            if (W >= items[index].weight) {
                // Если текущий предмет полностью помещается в оставшееся свободное место (W):
                W -= items[index].weight; // Уменьшаем доступный вес на вес добавленного предмета
                result += items[index].cost; // Добавляем стоимость предмета к общему результату
            } else {
                // Если текущий предмет не помещается полностью:
                result += valuePerWeight[index] * W;
                // Добавляем часть стоимости предмета, пропорциональную доступному весу (W).
                break; // Завершаем цикл, так как дальнейшее добавление невозможно
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

        @Override
        public int compareTo(Item o) {
            //тут может быть ваш компаратор
            return 0;
        }
    }
}