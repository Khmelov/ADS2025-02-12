package by.it.group451002.koltsov.lesson02;

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

        double result = 0;
        int maxCoeffIndItem;
        Item tempItem;

        // Сортировка масссива item-ов по убыванию цены одного килограмма item-а
        for (int i = 0; i < items.length; i++)
        {
            maxCoeffIndItem = i;
            for (int k = i; k < items.length; k++)
            {
                if (items[k].cost / items[k].weight > items[maxCoeffIndItem].cost / items[maxCoeffIndItem].weight)
                    maxCoeffIndItem = k;
            }
            tempItem = items[i];
            items[i] = items[maxCoeffIndItem];
            items[maxCoeffIndItem] = tempItem;
        }

        int i = 0;
        int currWeight = 0;

        // проходимся по каждому элементу массива "items" и добавляем предмет в рюкзак целиком,
        // если он помещается.
        // Если нет, то режем предмет и до конца забиваем им рюкзак.
        while (currWeight < W && i < n)
            if (items[i].weight < W - currWeight)
            {
                currWeight += items[i].weight;
                result += items[i].cost;
                i++;
            }
            else
            {
                result += (double)items[i].cost / items[i].weight * (W - currWeight);
                currWeight = W;
            }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item implements Comparable<Item>
    {
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