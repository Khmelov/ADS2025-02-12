package by.it.group451002.andreev.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/*
Даны интервальные события events
Реализуйте метод calcStartTimes, чтобы число принятых к выполнению
непересекающихся событий было максимально.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class B_Sheduler {
    public static void main(String[] args) {
        // Создаем экземпляр класса B_Sheduler
        B_Sheduler instance = new B_Sheduler();

        // Определяем список событий (с началом и окончанием)
        Event[] events = {new Event(0, 3), new Event(0, 1), new Event(1, 2), new Event(3, 5),
                new Event(1, 3), new Event(1, 3), new Event(1, 3), new Event(3, 6),
                new Event(2, 7), new Event(2, 3), new Event(2, 7), new Event(7, 9),
                new Event(3, 5), new Event(2, 4), new Event(2, 3), new Event(3, 7),
                new Event(4, 5), new Event(6, 7), new Event(6, 9), new Event(7, 9),
                new Event(8, 9), new Event(4, 6), new Event(8, 10), new Event(7, 10)
        };

        // Рассчитываем оптимальный график непересекающихся событий
        List<Event> starts = instance.calcStartTimes(events, 0, 10);

        // Выводим рассчитанный график событий
        System.out.println(starts);
    }

    // Метод для вычисления максимального числа непересекающихся событий
    List<Event> calcStartTimes(Event[] events, int from, int to) {
        // Events - список событий, которые нужно распределить
        // from и to - период времени, в пределах которого выполняется оптимизация

        List<Event> result;
        result = new ArrayList<>();

        // Сортируем события по времени окончания
        Arrays.sort(events, Comparator.comparing(Event::getStop));

        // Инициализируем индекс для перебора событий
        int i = 0;

        // Пока есть события для обработки
        while (i < events.length) {
            // Добавляем текущее событие в результат
            result.add(events[i]);

            // Пропускаем все пересекающиеся события
            while (i < events.length && events[i].start < result.get(result.size() - 1).stop) {
                i++;
            }
        }

        // Возвращаем список непересекающихся событий
        return result;
    }

    // Класс для представления события (с началом и окончанием)
    static class Event {
        int start; // Начало события
        int stop;  // Конец события

        Event(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        // Метод для получения времени окончания события
        public int getStop() {
            return stop;
        }

        @Override
        public String toString() {
            // Преобразование события в строку (например, "(0:3)")
            return "(" + start + ":" + stop + ")";
        }
    }
}
