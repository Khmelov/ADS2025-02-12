package by.it.group451002.jasko.lesson02;
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

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); // Засекаем время начала выполнения
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream); // Вычисляем максимальную стоимость рюкзака
        long finishTime = System.currentTimeMillis(); // Засекаем время окончания выполнения
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime); // Выводим результат
    }

    double calc(InputStream inputStream) {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      // Считываем количество предметов из файла
        int W = input.nextInt();      // Считываем вместимость рюкзака
        Item[] items = new Item[n];   // Создаем массив для хранения всех предметов
        for (int i = 0; i < n; i++) { // Заполняем массив предметами из файла
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        // Выводим информацию о всех предметах и параметрах задачи
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        double result = 0; // Переменная для хранения общей стоимости рюкзака

        // Сортируем предметы по соотношению cost/weight (от самых ценных к менее ценным)
        Arrays.sort(items);

        // Жадный алгоритм для заполнения рюкзака
        double remainingWeight = W; // Оставшийся вес рюкзака (начинаем с полной вместимости)
        for (Item item : items) { // Проходим по всем предметам
            if (remainingWeight == 0) break; // Если рюкзак уже полон, завершаем процесс

            // Берем максимально возможную часть текущего предмета
            double fraction = Math.min(remainingWeight, item.weight); // Выбираем столько, сколько помещается
            result += fraction * (item.cost / (double) item.weight); // Добавляем стоимость выбранной части
            remainingWeight -= fraction; // Уменьшаем оставшееся место в рюкзаке
        }

        // Выводим итоговую стоимость рюкзака
        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result; // Возвращаем результат
    }

    private static class Item implements Comparable<Item> {
        int cost; // Стоимость предмета
        int weight; // Вес предмета

        // Конструктор для создания предмета с заданной стоимостью и весом
        Item(int cost, int weight) {
            this.cost = cost; // Запоминаем стоимость
            this.weight = weight; // Запоминаем вес
        }

        // Метод для вывода информации о предмете в виде строки
        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost + // Выводим стоимость предмета
                    ", weight=" + weight + // Выводим вес предмета
                    '}';
        }

        @Override
        public int compareTo(Item o) {
            // Сравниваем предметы по их эффективности (cost/weight)
            double ratioThis = (double) this.cost / this.weight; // Эффективность текущего предмета
            double ratioOther = (double) o.cost / o.weight; // Эффективность другого предмета

            // Сортируем предметы по убыванию их эффективности
            return Double.compare(ratioOther, ratioThis);
        }
    }
}