package by.it.group410901.kliaus.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
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

    class EventByEndTimeComparator implements Comparator<Event> {
        public int compare(Event first, Event second) {
            if (first.stop < second.stop) return -1;
            else if (first.stop == second.stop) return 0;
            else return 1;
        }
    }

    List<Event> calcStartTimes(Event[] eventArray, int startTime, int endTime) {
        //Events - события которые нужно распределить в аудитории
        //в период [from, int] (включительно).
        //оптимизация проводится по наибольшему числу непересекающихся событий.
        //Начало и конец событий могут совпадать.
        List<Event> selectedEvents;
        selectedEvents = new ArrayList<>();

        Arrays.sort(eventArray, new EventByEndTimeComparator());

        for (int idx = 0; idx < eventArray.length && eventArray[idx].stop <= endTime; idx++) {
            if (eventArray[idx].start >= startTime) {
                selectedEvents.add(eventArray[idx]);
                startTime = eventArray[idx].stop;
            }
        }

        return selectedEvents;
    }

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