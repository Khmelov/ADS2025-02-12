package by.it.group451004.rak.lesson02;

import java.util.ArrayList;
import java.util.List;

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
        int start = from;
        int optimalEnd;
        int optimalStart;
        int optimalIndex = 0;
        boolean isFound;
        do {
            optimalEnd = to;
            optimalStart = to;
            isFound = false;
            for (int i = 0; i < events.length; i++) {
                if (events[i].stop <= optimalEnd && events[i].start >= start && events[i].start <= optimalStart) {
                    optimalEnd = events[i].stop;
                    optimalStart = events[i].start;
                    optimalIndex = i;
                    isFound = true;
                }
            }
            if (isFound) {
                result.add(events[optimalIndex]);
                start = events[optimalIndex].stop;
            }
        } while (isFound);
        return result;
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