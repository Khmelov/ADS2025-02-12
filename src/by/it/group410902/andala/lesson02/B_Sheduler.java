package by.it.group410902.andala.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class B_Sheduler {
    public static void main(String[] args) {
        B_Sheduler instance = new B_Sheduler();

        // Массив событий, каждое имеет время начала и окончания
        Event[] events = {new Event(0, 3), new Event(0, 1), new Event(1, 2), new Event(3, 5),
                new Event(1, 3), new Event(1, 3), new Event(1, 3), new Event(3, 6),
                new Event(2, 7), new Event(2, 3), new Event(2, 7), new Event(7, 9),
                new Event(3, 5), new Event(2, 4), new Event(2, 3), new Event(3, 7),
                new Event(4, 5), new Event(6, 7), new Event(6, 9), new Event(7, 9),
                new Event(8, 9), new Event(4, 6), new Event(8, 10), new Event(7, 10)
        };

        // Вычисляем оптимальное расписание событий в интервале от 0 до 10
        List<Event> starts = instance.calcStartTimes(events, 0, 10);
        System.out.println(starts);  // Выводим результат
    }

    /**
     * Метод для вычисления оптимального расписания событий (задача о выборе заявок)
     *
     * @param events массив событий (каждое имеет start и stop)
     * @param from   начало временного интервала
     * @param to     конец временного интервала
     * @return список событий, которые можно выполнить без пересечений
     */
    List<Event> calcStartTimes(Event[] events, int from, int to) {
        List<Event> result = new ArrayList<>();
        if (events.length == 0) {
            return result;  // Если событий нет, возвращаем пустой список
        }

        // Сортируем события по времени окончания (чтобы выбирать самые ранние завершающиеся)
        Arrays.sort(events, Comparator.comparingInt(e -> e.stop));

        // Первое событие - то, что заканчивается раньше всех
        result.add(events[0]);
        int lastEnd = events[0].stop;  // Запоминаем время его окончания

        // Проходим по остальным событиям
        for (int i = 1; i < events.length; i++) {
            // Если текущее событие начинается после окончания последнего выбранного
            if (events[i].start >= lastEnd) {
                result.add(events[i]);  // Добавляем его в расписание
                lastEnd = events[i].stop;  // Обновляем время окончания
            }
        }

        return result;
    }

    /**
     * Вложенный класс для представления события
     */
    static class Event {
        int start;  // Время начала события
        int stop;   // Время окончания события

        Event(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public String toString() {
            return "(" + start + ":" + stop + ")";  // Формат вывода события
        }
    }
}