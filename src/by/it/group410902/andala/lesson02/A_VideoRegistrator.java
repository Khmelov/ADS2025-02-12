package by.it.group410902.andala.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class A_VideoRegistrator {

    public static void main(String[] args) {
        // Создаём экземпляр класса для вызова метода calcStartTimes
        A_VideoRegistrator instance = new A_VideoRegistrator();

        // Массив временных меток событий (произвольный неотсортированный набор)
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};

        // Вычисляем моменты включения регистратора (длительность работы = 1 единица времени)
        List<Double> starts = instance.calcStartTimes(events, 1);

        // Выводим результат
        System.out.println(starts);
    }

    /**
     * Метод вычисляет оптимальные моменты включения видеорегистратора,
     * чтобы покрыть все события с минимальным числом включений.
     *
     * @param events       массив временных меток событий
     * @param workDuration длительность работы регистратора после включения
     * @return список моментов времени, когда нужно включить регистратор
     */
    List<Double> calcStartTimes(double[] events, double workDuration) {
        // Результат — список моментов включений
        List<Double> result = new ArrayList<>();

        // Если событий нет, возвращаем пустой список
        if (events.length == 0) {
            return result;
        }

        // Сортируем события по возрастанию времени (для жадного алгоритма)
        Arrays.sort(events);

        // Индекс текущего события
        int i = 0;

        // Проходим по всем событиям
        while (i < events.length) {
            // Фиксируем текущее событие как момент включения регистратора
            double startTime = events[i];
            result.add(startTime);

            // Вычисляем время окончания работы регистратора
            double endTime = startTime + workDuration;

            // Пропускаем все события, которые попадают в интервал [startTime, endTime]
            // (они уже будут покрыты текущим включением)
            while (i < events.length && events[i] <= endTime) {
                i++;
            }
        }

        return result;
    }
}