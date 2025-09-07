package by.it.group451002.jasko.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Даны интервальные события events.
Реализуйте метод calcStartTimes так, чтобы число принятых к выполнению
непересекающихся событий было максимально.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class B_Sheduler {
    public static void main(String[] args) {
        B_Sheduler instance = new B_Sheduler();
        Event[] events = {new Event(0, 3), new Event(0, 1), new Event(1, 2), new Event(3, 5),
                new Event(1, 3), new Event(1, 3), new Event(1, 3), new Event(3, 6),
                new Event(2, 7), new Event(2, 3), new Event(2, 7), new Event(7, 9),
                new Event(3, 5), new Event(2, 4), new Event(2, 3), new Event(3, 7),
                new Event(4, 5), new Event(6, 7), new Event(6, 9), new Event(7, 9),
                new Event(8, 9), new Event(4, 6), new Event(8, 10), new Event(7, 10)
        };

        // Рассчитаем оптимальное распределение событий в заданном диапазоне [from, to]
        List<Event> starts = instance.calcStartTimes(events, 0, 10);
        System.out.println(starts); // Выведем рассчитанный график занятий
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        // Events - это массив событий, которые нужно распределить в аудитории
        // в период [from, to] (включительно).
        // Цель: выбрать максимальное количество непересекающихся событий.
        // Начало и конец событий могут совпадать.

        List<Event> result; // Список для хранения выбранных событий
        result = new ArrayList<>();

        // Сортируем события по времени начала (start). Это подготовка к жадному выбору.
        Arrays.sort(events);

        // Буфер для хранения текущего рассматриваемого события
        Event buf = events[0];

        // Проходим по всем событиям
        for (Event event : events) {
            // Проверяем условия для добавления события:
            // 1. Событие не должно начинаться в точке from (чтобы избежать дублирования).
            // 2. Событие должно заканчиваться до времени to.
            // 3. Текущее событие не должно пересекаться с последним выбранным.
            if (event.start != from && event.stop <= to && (result.isEmpty() || (buf.stop - event.start) <= 0)) {
                result.add(buf); // Добавляем предыдущее событие в результат
                buf = event; // Обновляем буфер на текущее событие
                from = event.start; // Обновляем отсчет времени
            }

            // Если текущее событие заканчивается раньше, чем у хранящегося в буфере,
            // то обновляем буфер на текущее событие.
            if (event.stop < buf.stop) {
                buf = event;
            }
        }

        // После завершения цикла добавляем последнее выбранное событие
        result.add(buf);

        return result; // Возвращаем итоговый список событий
    }

    // Класс Event представляет интервальное событие с двумя полями: начало (start) и конец (stop)
    static class Event implements Comparable<Event> {
        int start; // Время начала события
        int stop;  // Время окончания события

        Event(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public String toString() {
            return "(" + start + ":" + stop + ")"; // Вывод события в формате "(start:stop)"
        }

        @Override
        public int compareTo(Event e) {
            return start - e.start; // Сортировка событий по времени начала (start)
        }
    }
}