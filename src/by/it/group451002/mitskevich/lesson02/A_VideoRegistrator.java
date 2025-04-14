package by.it.group451002.mitskevich.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator(); // создаём экземпляр класса
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7}; // список событий
        List<Double> starts = instance.calcStartTimes(events, 1); // рассчитываем моменты включения камеры с длительностью 1 секунда
        System.out.println(starts); // выводим полученные моменты старта камеры
    }

    List<Double> calcStartTimes(double[] events, double workDuration) {
        List<Double> result = new ArrayList<>(); // список для хранения времени включения камеры

        Arrays.sort(events); // сортируем события по возрастанию времени

        int i = 0; // начальный индекс для прохода по массиву событий
        int n = events.length; // общее количество событий

        while (i < n) { // пока есть необработанные события
            double start = events[i]; // берём текущее самое раннее необработанное событие
            result.add(start); // включаем камеру на этом моменте
            double end = start + workDuration; // вычисляем время выключения камеры

            // пропускаем все события, которые попадают в текущий интервал записи камеры
            while (i < n && events[i] <= end) {
                i++; // переходим к следующему событию
            }
        }

        return result; // возвращаем список всех включений камеры
    }
}

