package by.it.group451002.popeko.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1); // рассчитаем моменты старта с длительностью сеанса 1
        System.out.println(starts);                              // покажем моменты старта
    }

    List<Double> calcStartTimes(double[] events, double workDuration) {
        // Список для хранения моментов включения регистратора
        List<Double> result = new ArrayList<>();

        // Шаг 1: Сортировка событий
        Arrays.sort(events);

        // Шаг 2: Жадный алгоритм для выбора времени запуска регистратора
        int i = 0; // индекс текущего события
        while (i < events.length) {
            // Выбираем событие по левому краю
            double startTime = events[i];
            result.add(startTime); // добавляем время старта в результат

            // Вычисляем время завершения работы регистратора
            double endTime = startTime + workDuration;

            // Пропускаем все события, которые покрываются текущим интервалом
            while (i < events.length && events[i] <= endTime) {
                i++;
            }
        }

        return result; // Возвращаем список моментов включения регистратора
    }
}
