package by.it.group451004.belkovich.lesson02;

import java.util.ArrayList;
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
    public static void insertionSort(Event[] events) {
        for (int i = 1; i < events.length; i++) {
            Event current = events[i];
            int j = i - 1;

            while (j >= 0 && (events[j].start > current.start ||
                    (events[j].start == current.start && events[j].stop > current.stop))) {
                events[j + 1] = events[j];
                j--;
            }

            events[j + 1] = current;
        }
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        List<Event> result;
        result = new ArrayList<>();
        int best = 0;
        int bestSize = 0;

        insertionSort(events);

        for (int i = 0; i < events.length-1; i++) {
            if (events[i].start < from)
                continue;

            List<Event> tryArr;
            tryArr = new ArrayList<>();

            tryArr.add(events[i]);
            int j = i + 1;
            int time = events[i].stop;
            while (j < events.length) {
                if (events[j].stop > to)
                    break;

                if (events[j].start >= time) {
                    time = events[j].stop;
                    tryArr.add(events[j]);
                }

                j++;
            }

            if (tryArr.size() > bestSize) {
                best = i;
                bestSize = tryArr.size();
            }

        }

        result.add(events[best]);
        int j = best + 1;
        int time = events[best].stop;
        while (j < events.length) {
            if (events[j].stop > to)
                break;

            if (events[j].start >= time) {
                time = events[j].stop;
                result.add(events[j]);
            }
            j++;
        }

        return result;

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