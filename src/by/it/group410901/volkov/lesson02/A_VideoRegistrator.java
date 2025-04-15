package by.it.group410901.volkov.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
Даны события events
реализуйте метод calcStartTimes, так, чтобы число включений регистратора на
заданный период времени (1) было минимальным, а все события events
были зарегистрированы.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1); //рассчитаем моменты старта, с длинной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        List<Double> result = new ArrayList<>();//events - события которые нужно зарегистрировать
        if (events.length == 0) {
            return result;
        }

        Arrays.sort(events); //hint: сортировка Arrays.sort обеспечит скорость алгоритма
        int i = 0;                              //i - это индекс события events[i]
        while (i < events.length) {
            // Текущее событие - начало новой записи
            double startTime = events[i];
            result.add(startTime);

            // Время окончания текущей записи
            double endTime = startTime + workDuration;

            // Пропускаем все события, попадающие в текущий интервал записи
            while (i < events.length && events[i] <= endTime) {
                i++;
            }
        }

        return result;                        //вернем итог
    }
}
