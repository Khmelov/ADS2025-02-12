package by.it.group410902.derzhavskaya_ludmila.lesson02;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
/*
Даны интервальные события events
реализуйте метод calcStartTimes, так, чтобы число принятых к выполнению
непересекающихся событий было максимально.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class B_Sheduler
{
    public static void main(String[] args)
    {
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

    List<Event> calcStartTimes(Event[] events, int from, int to)
    {
        //Events - события которые нужно распределить в аудитории
        //в период [from, int] (включительно).
        //оптимизация проводится по наибольшему числу непересекающихся событий.
        //Начало и конец событий могут совпадать.
        List<Event> result;
        result = new ArrayList<>();

        // Сортировка событий по времени окончания, минимизация перекрытия событий.
        Arrays.sort(events, (e1, e2) -> Integer.compare(e1.stop, e2.stop));

        int currentTime = from;

        // Выбор событий по принципу жадного алгоритма.
        // выбираем только те события, которые начинаются после окончания предыдущего выбранного события.
        for (Event event : events) {
            if (event.start >= currentTime && event.stop <= to) {
                result.add(event);
                currentTime = event.stop; // обновляем текущее время
            }
        }

        return result;          //вернем итог
    }

    //событие у аудитории(два поля: начало и конец)
    static class Event
    {
        int start;
        int stop;

        Event(int start, int stop)
        {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public String toString() {
            return "(" + start + ":" + stop + ")";
        }
    }
}