package by.it.group451002.hodysh.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};

        // Пример использования разных значений workDuration
        double[] durations = new double[]{1, 2, 3}; // Разные длины сеанса
        for (double duration : durations) {
            List<Double> starts = instance.calcStartTimes(events, duration);
            System.out.printf("Start times with duration %.1f: %s%n", duration, starts);
        }
    }

    // Модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        List<Double> result = new ArrayList<>();

        // Сортируем массив событий по времени
        Arrays.sort(events);

        int i = 0;
        int n = events.length;

        while (i < n) {
            // Находим первый момент старта для регистратора
            double startTime = events[i];
            result.add(startTime);

            // Видеокамера будет работать до момента startTime + workDuration
            double endTime = startTime + workDuration;

            // Пропускаем все события, которые укладываются в этот интервал
            while (i < n && events[i] <= endTime) {
                i++;
            }
        }

        return result; // Возвращаем итоговые моменты старта
    }
}

