package by.it.group410902.latipov.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

        List<Event> starts = instance.calcStartTimes(events, 0, 10);
        System.out.println(starts);
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        List<Event> result = new ArrayList<>();

        // Filter events that are within the [from, to] range
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.start >= from && event.stop <= to) {
                filteredEvents.add(event);
            }
        }

        // If no valid events, return empty list
        if (filteredEvents.isEmpty()) {
            return result;
        }

        // Convert to array for sorting
        Event[] eventsArray = filteredEvents.toArray(new Event[0]);

        // Sort events by end time (stop)
        Arrays.sort(eventsArray, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return Integer.compare(e1.stop, e2.stop);
            }
        });

        // Greedy selection
        int lastEndTime = -1;
        for (Event event : eventsArray) {
            if (event.start >= lastEndTime) {
                result.add(event);
                lastEndTime = event.stop;
            }
        }

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