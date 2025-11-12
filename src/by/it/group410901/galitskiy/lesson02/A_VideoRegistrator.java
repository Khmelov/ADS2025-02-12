package by.it.group410901.galitskiy.lesson02;

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
        List<Double> result = new ArrayList<>();

        // Сортируем события
        Arrays.sort(events);

        int i = 0; // индекс события
        int n = events.length;

        while (i < n) {
            // Запоминаем текущее событие как начало
            double start = events[i];
            result.add(start);

            // Определяем время окончания работы видеорегистратора
            double end = start + workDuration;

            // Пропускаем все события, которые покрываются текущим запуском
            while (i < n && events[i] <= end) {
                i++;
            }
        }

        return result; // возвращаем итоговые моменты старта                     //вернем итог
    }
}
