package by.it.group451002.mitskevich.lesson02; // Указываем, в каком пакете находится этот класс

import java.io.InputStream;     // Для чтения входного потока (файла)
import java.util.Arrays;        // Для сортировки массива
import java.util.Scanner;       // Для считывания данных из файла

public class C_GreedyKnapsack { // Главный класс для решения задачи о дробном рюкзаке

    public static void main(String[] args) { // Точка входа в программу
        long startTime = System.currentTimeMillis(); // Засекаем время начала выполнения

        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt"); // Открываем файл как поток
        double costFinal = 0; // Переменная для хранения финальной стоимости набора предметов

        try {
            costFinal = new C_GreedyKnapsack().calc(inputStream); // Вызываем основной метод calc
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage()); // Обрабатываем ошибки чтения
        }

        long finishTime = System.currentTimeMillis(); // Засекаем время завершения
        System.out.printf("Общая стоимость %f (время %d мс)\n", costFinal, finishTime - startTime); // Выводим результат
    }

    double calc(InputStream inputStream) {
        Scanner input = new Scanner(inputStream); // Создаём сканнер для чтения данных из потока

        int n = input.nextInt();      // Читаем количество предметов
        int W = input.nextInt();      // Читаем вместимость рюкзака (максимальный вес)

        Item[] items = new Item[n];   // Создаём массив предметов

        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt()); // Читаем стоимость и вес каждого предмета
        }

        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W); // Выводим базовую информацию

        Arrays.sort(items); // Сортируем предметы по убыванию удельной стоимости (стоимость / вес)

        double result = 0; // Общая стоимость, которую мы соберём
        int capacity = W;  // Оставшаяся вместимость рюкзака

        for (Item item : items) { // Перебираем отсортированные предметы
            if (capacity == 0) break; // Если рюкзак уже заполнен — прекращаем

            if (item.weight <= capacity) { // Если весь предмет помещается
                result += item.cost;       // Добавляем полную стоимость
                capacity -= item.weight;   // Уменьшаем оставшуюся вместимость
            } else {
                // Если предмет не влезает полностью — берём только часть
                double fraction = (double) capacity / item.weight; // Вычисляем долю предмета, которую можно взять
                result += item.cost * fraction; // Добавляем часть стоимости
                capacity = 0; // Рюкзак полностью заполнен
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result); // Выводим итоговую стоимость
        return result; // Возвращаем результат
    }

    // Внутренний класс, представляющий предмет
    private static class Item implements Comparable<Item> {
        int cost;             // Стоимость предмета
        int weight;           // Вес предмета
        double valuePerWeight; // Удельная стоимость (стоимость за 1 кг)

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
            this.valuePerWeight = (double) cost / weight; // Вычисляем удельную стоимость при создании
        }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    ", valuePerWeight=" + String.format("%.2f", valuePerWeight) +
                    '}'; // Метод для вывода информации о предмете
        }

        @Override
        public int compareTo(Item o) {
            // Сортировка по убыванию удельной стоимости
            return Double.compare(o.valuePerWeight, this.valuePerWeight);
        }
    }
}

