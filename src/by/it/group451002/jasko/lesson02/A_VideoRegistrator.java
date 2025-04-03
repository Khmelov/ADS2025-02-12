package by.it.group451002.jasko.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events); // Рассчитаем моменты старта с длиной сеанса 1
        System.out.println(starts); // Покажем моменты старта
    }

    // Модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events) {
        // Events - это массив событий, которые нужно зарегистрировать.
        // workDuration - время работы видеорегистратора после старта.

        List<Double> result = new ArrayList<>();
        double lastEnd = Double.NEGATIVE_INFINITY; // Время окончания последнего сеанса

        // Шаг 1: Сортируем события по возрастанию времени
        Arrays.sort(events);

        // Шаг 2: Проходим по всем событиям
        for (double event : events) {
            if (event > lastEnd) { // Если текущее событие не покрыто последним сеансом
                result.add(event); // Запускаем новый сеанс
                lastEnd = event + 1; // Обновляем время окончания сеанса
            }
        }

        return result; // Возвращаем итоговый список моментов старта
    }
}