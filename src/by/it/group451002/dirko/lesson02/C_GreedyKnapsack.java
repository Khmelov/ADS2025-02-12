package by.it.group451002.dirko.lesson02;
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

        double result = 0;

        // Сортируем предметы по убыванию cost / weight
        Item buf_item;
        for (int i = 0; i < n - 1; i++){
            int max_ind = i;
            for (int j = i + 1; j < n; j++)
                if (items[j].compareTo(items[max_ind]) == 1)
                    max_ind = j;
            buf_item = items[i];
            items[i] = items[max_ind];
            items[max_ind] = buf_item;
        }

        // Собираем рюкзак
        for (int i = 0; i < n; i++) {
            if (items[i].weight < W){
                W -= items[i].weight;
                result += items[i].cost;
            }
            else{
                result += (double) (items[i].cost / items[i].weight) * W;
                break;
            }
        }

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
            if (cost / weight > o.cost / o.weight) return 1;
            return 0;
        }
    }
}