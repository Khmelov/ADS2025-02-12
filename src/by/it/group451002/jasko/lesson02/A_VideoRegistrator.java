package by.it.group451002.jasko.lesson02;

/*
Даны события events
реализуйте метод calcStartTimes, так, чтобы число включений регистратора на
заданный период времени (1) было минимальным, а все события events
были зарегистрированы.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

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
        // Events - это массив событий, которые нужно зарегистрировать

        List<Double> result = new ArrayList<>();
        double lastEnd = Double.NEGATIVE_INFINITY; // Время окончания последнего сеанса

        // Шаг 1: Сортируем события по возрастанию времени
        Arrays.sort(events);

        // Шаг 2: Проходим по всем событиям
        for (double event : events) {

            if (event > lastEnd) {
                // Если текущее событие происходит позже окончания последнего сеанса,
                // значит его нужно покрыть новым сеансом

                result.add(event);
                // Добавляем момент начала нового сеанса в результат.
                // Новый сеанс начинается именно в момент текущего события

                lastEnd = event + 1;
                // Устанавливаем время окончания нового сеанса (каждый сеанс длится 1 единицу времени, event - время его начала).
            }
            // Если событие находится внутри текущего сеанса (event <= lastEnd),
            // то оно уже покрыто, и ничего делать не нужно
        }

        return result; // Возвращаем итоговый список моментов старта
    }
}