package by.it.group451002.mitskevich.lesson02; // Указываем пакет, в котором находится файл

import java.util.ArrayList;     // Импортируем ArrayList для хранения результата
import java.util.Arrays;        // Импортируем Arrays для сортировки массива
import java.util.Comparator;    // Импортируем Comparator для сортировки по времени окончания
import java.util.List;          // Импортируем интерфейс List

public class B_Sheduler { // Главный класс задачи

    public static void main(String[] args) { // Главный метод, с которого запускается программа
        B_Sheduler instance = new B_Sheduler(); // Создаём экземпляр класса

        Event[] events = { // Массив событий с интервалами [start, stop]
                new Event(0, 3), new Event(0, 1), new Event(1, 2), new Event(3, 5),
                new Event(1, 3), new Event(1, 3), new Event(1, 3), new Event(3, 6),
                new Event(2, 7), new Event(2, 3), new Event(2, 7), new Event(7, 9),
                new Event(3, 5), new Event(2, 4), new Event(2, 3), new Event(3, 7),
                new Event(4, 5), new Event(6, 7), new Event(6, 9), new Event(7, 9),
                new Event(8, 9), new Event(4, 6), new Event(8, 10), new Event(7, 10)
        };

        List<Event> optimalSchedule = instance.calcStartTimes(events, 0, 10); // Вызываем метод, передаём список событий и границы интервала
        System.out.println(optimalSchedule); // Печатаем оптимальное расписание непересекающихся событий
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) { // Метод для выбора максимального количества непересекающихся событий
        List<Event> result = new ArrayList<>(); // Список выбранных событий

        Arrays.sort(events, Comparator.comparingInt(e -> e.stop));

        int currentTime = from; // Устанавливаем текущее время в начало интервала

        for (Event event : events) { // Перебираем все отсортированные события
            if (event.start >= currentTime && event.stop <= to) { // Если событие не пересекается с уже выбранными
                result.add(event); // Добавляем событие в расписание
                currentTime = event.stop; // Обновляем текущее время на конец этого события
            }
        }

        return result; // Возвращаем полученный список оптимальных событий
    }

    static class Event { // Вспомогательный класс события
        int start; // Время начала
        int stop;  // Время окончания

        Event(int start, int stop) { // Конструктор класса
            this.start = start;
            this.stop = stop;
        }

        @Override
        public String toString() { // Метод для красивого вывода события
            return "(" + start + ":" + stop + ")";
        }
    }
}

