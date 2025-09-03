package by.it.group451002.kravtsov.lesson2;

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
        List<Double> starts = instance.calcStartTimes(events, 1);
        System.out.println(starts);
    }

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        // Создаём список для хранения стартовых времён
        List<Double> result = new ArrayList<>();

        // Сортируем массив событий по возрастанию времени
        Arrays.sort(events);
        // Инициализируем индекс для итерации по событиям
        int i = 0;
        // Пока не обработаны все события
        while (i < events.length) {
            // Берём текущее время события как старт нового интервала
            double start = events[i];
            result.add(start); // Добавляем стартовое время в список
            // Рассчитываем время окончания текущего интервала
            double end = start + workDuration;
            // Пропускаем все события, которые попадают в текущий интервал
            while (i < events.length && events[i] <= end) {
                i++;
            }
        }

        // Возвращаем список стартовых времён
        return result;
    }
}