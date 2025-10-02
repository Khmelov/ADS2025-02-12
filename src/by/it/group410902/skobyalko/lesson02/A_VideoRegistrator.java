package by.it.group410902.skobyalko.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1);
        System.out.println(starts); // Ожидаемый вывод: минимальное количество стартов
    }

    List<Double> calcStartTimes(double[] events, double workDuration) {
        List<Double> result = new ArrayList<>();
        if (events == null || events.length == 0) return result;

        Arrays.sort(events); // сортировка событий по возрастанию
        int i = 0;

        while (i < events.length) {
            double start = events[i]; // старт регистратора по первому непокрытому событию
            result.add(start);
            double endTime = start + workDuration;

            // Пропускаем все события, попавшие в интервал работы регистратора
            while (i < events.length && events[i] <= endTime) {
                i++;
            }
        }

        return result;
    }
}
/// /
