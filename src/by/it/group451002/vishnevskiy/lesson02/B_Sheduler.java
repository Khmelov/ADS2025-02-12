package by.it.group451002.vishnevskiy.lesson02;
import java.util.ArrayList;
import java.util.Arrays;
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
        //Events - события которые нужно распределить в аудитории
        //в период [from, int] (включительно).
        //оптимизация проводится по наибольшему числу непересекающихся событий.
        //Начало и конец событий могут совпадать.
        List<Event> result;
        result = new ArrayList<>();
        //ваше решение.----------------------------------------------------------------------------------------
        Arrays.sort(events);
        Event buffer = events[0];
        for (Event event : events) { //*Проверка, начало с from, конец в to,
                                     //*Если список результата пуст, всегда добавляем первое событие.
               //*Проверяет, чтобы текущее событие начиналось после завершения предыдущего (buffer.stop).
            if (event.start != from && event.stop < to && (result.isEmpty() || (buffer.stop - event.start) <= 0)) {

                //*Если все условия выполнены, событие добавляется в расписание:
                result.add(buffer);
                buffer = event;
                from = event.start;
            }
              //*Если текущее событие заканчивается раньше, чем событие в buffer, заменяем buffer на текущее событие.
            if (event.stop < buffer.stop)
                buffer = event;
        }
        //*После завершения цикла последнее обработанное событие (buffer) добавляется в результат.
        result.add(buffer);
        return result;          //вернем итог
    }
    //событие у аудитории(два поля: начало и конец)
    static class Event implements Comparable<Event> {
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
        @Override
        public int compareTo(Event e) {
            return start - e.start;
        }
    }
}