package by.it.group451002.andreev.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Даны события events.
Реализуйте метод calcStartTimes так, чтобы минимизировать число включений регистратора на заданный период времени,
при этом регистратор должен охватить все события.
Используется жадный алгоритм.
*/

public class A_VideoRegistrator {

    public static void main(String[] args) {
        // Создаем экземпляр класса A_VideoRegistrator
        A_VideoRegistrator instance = new A_VideoRegistrator();

        // Определяем массив событий (времена их начала)
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};

        // Рассчитываем моменты старта регистратора с длиной сеанса 1
        List<Double> starts = instance.calcStartTimes(events, 1);

        // Выводим рассчитанные моменты старта
        System.out.println(starts);
    }

    // Метод для расчета минимальных моментов старта регистратора
    List<Double> calcStartTimes(double[] events, double workDuration) {
        // events - список событий, которые нужно зарегистрировать
        // workDuration - время работы регистратора после его включения

        List<Double> result; // Список моментов старта регистратора
        result = new ArrayList<>(); // Инициализируем список

        int i = 0; // Индекс текущего события

        // Сортируем события по времени их начала
        Arrays.sort(events);

        // Пока есть незарегистрированные события
        while (i < events.length) {
            // Добавляем текущее событие как момент старта регистратора
            result.add(events[i]);

            // Вычисляем момент окончания работы регистратора
            double end = events[i] + workDuration;

            // Пропускаем все события, которые покрываются текущим сеансом
            while (i < events.length && events[i] <= end) {
                i++;
            }
        }

        // Возвращаем список моментов старта регистратора
        return result;
    }
}
