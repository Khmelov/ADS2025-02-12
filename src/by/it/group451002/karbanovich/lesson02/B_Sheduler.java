package by.it.group451002.karbanovich.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
/*
Даны интервальные события events
реализуйте метод calcStartTimes, так, чтобы число принятых к выполнению
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

        List<Event> starts = instance.calcStartTimes(events, 0, 10);  //рассчитаем оптимальное заполнение аудитории
        System.out.println(starts);                                 //покажем рассчитанный график занятий
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {

        List<Event> result;
        result = new ArrayList<>();

        List<Event> list_events = new ArrayList<>(Arrays.asList(events));
        list_events.sort(Comparator.comparingInt((Event event) -> event.stop).
                thenComparingInt((Event event) -> event.start));

        // Убираем из List все бесполезные события
        list_events.removeIf(event -> event.start < from);
        list_events.removeIf(event -> event.stop > to);

        // Если в списке не осталось событий, то возвращаем пустой результат
        if (list_events.isEmpty()) return result;

        // Проходимся по списку и добавляем нужные события
        result.add(list_events.getFirst());
        for (int i = 1; i < list_events.size(); i++)
            if (list_events.get(i).stop != result.getLast().stop &&
                    list_events.get(i).start >= result.getLast().stop)
                result.add(list_events.get(i));


        return result;          //вернем итог
    }

    //событие у аудитории(два поля: начало и конец)
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