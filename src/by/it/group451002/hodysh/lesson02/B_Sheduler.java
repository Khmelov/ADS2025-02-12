package by.it.group451002.hodysh.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

public class B_Sheduler {
    public static void main(String[] args) {
        B_Sheduler instance = new B_Sheduler();
        Event[] events = {
                new Event(0, 3), new Event(0, 1), new Event(1, 2), new Event(3, 5),
                new Event(1, 3), new Event(1, 3), new Event(1, 3), new Event(3, 6),
                new Event(2, 7), new Event(2, 3), new Event(2, 7), new Event(7, 9),
                new Event(3, 5), new Event(2, 4), new Event(2, 3), new Event(3, 7),
                new Event(4, 5), new Event(6, 7), new Event(6, 9), new Event(7, 9),
                new Event(8, 9), new Event(4, 6), new Event(8, 10), new Event(7, 10)
        };

        // Проверяем входные параметры и используем их для расчета
        int from = 0; // Начало диапазона
        int to = 10;  // Конец диапазона

        // Проверка на наличие аргументов командной строки
        if (args.length >= 2) {
            try {
                from = Integer.parseInt(args[0]);
                to = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат входных параметров. Используются значения по умолчанию.");
            }
        }

        List<Event> starts = instance.calcStartTimes(events, from, to);  // Рассчитаем оптимальное заполнение аудитории
        System.out.println(starts);  // Покажем рассчитанный график занятий
    }

    // Метод для вычисления максимально возможного числа непересекающихся событий
    List<Event> calcStartTimes(Event[] events, int from, int to) {
        List<Event> result = new ArrayList<>();

        // Сортируем события по времени их окончания с использованием Comparator.comparingInt
        Arrays.sort(events, Comparator.comparingInt(e -> e.stop));

        int lastEndTime = from;  // Время завершения последнего выбранного события
        for (Event event : events) {
            // Если событие начинается после или в момент завершения последнего выбранного
            if (event.start >= lastEndTime && event.stop <= to) {
                result.add(event);  // Добавляем событие
                lastEndTime = event.stop;  // Обновляем время завершения
            }
        }

        return result;  // Возвращаем список выбранных событий
    }

    // Событие у аудитории (два поля: начало и конец)
    static class Event {
        int start;
        int stop;

        Event(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public String toString() {
            return "(" + start + ":" + stop + ")";
        }
    }
}

