package by.it.group451004.akbulatov.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class B_Sheduler {
    public static void main(String[] args) {
        B_Sheduler instance = new B_Sheduler();
        B_Sheduler.Event[] events = {new B_Sheduler.Event(0, 3), new B_Sheduler.Event(0, 1), new B_Sheduler.Event(1, 2), new B_Sheduler.Event(3, 5),
                new B_Sheduler.Event(1, 3), new B_Sheduler.Event(1, 3), new B_Sheduler.Event(1, 3), new B_Sheduler.Event(3, 6),
                new B_Sheduler.Event(2, 7), new B_Sheduler.Event(2, 3), new B_Sheduler.Event(2, 7), new B_Sheduler.Event(7, 9),
                new B_Sheduler.Event(3, 5), new B_Sheduler.Event(2, 4), new B_Sheduler.Event(2, 3), new B_Sheduler.Event(3, 7),
                new B_Sheduler.Event(4, 5), new B_Sheduler.Event(6, 7), new B_Sheduler.Event(6, 9), new B_Sheduler.Event(7, 9),
                new B_Sheduler.Event(8, 9), new B_Sheduler.Event(4, 6), new B_Sheduler.Event(8, 10), new B_Sheduler.Event(7, 10)
        };

        List<B_Sheduler.Event> starts = instance.calcStartTimes(events, 0, 10);
        System.out.println(starts);
    }

    List<B_Sheduler.Event> calcStartTimes(B_Sheduler.Event[] events, int from, int to) {
        B_Sheduler.Event buf = events[0];

        List<B_Sheduler.Event> result;
        result = new ArrayList<>();

        Arrays.sort(events);
        for (B_Sheduler.Event event : events) {
            if (event.start != from && event.stop < to && (result.isEmpty() || (buf.stop - event.start) <= 0)) {
                from = event.start;
                result.add(buf);
                buf = event;
            }
            if (event.stop < buf.stop)
                buf = event;
        }

        result.add(buf);

        return result;
    }

    static class Event implements Comparable<B_Sheduler.Event> {
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
        public int compareTo(B_Sheduler.Event e) {
            return start - e.start;
        }
    }
}