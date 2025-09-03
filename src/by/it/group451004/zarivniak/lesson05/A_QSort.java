package by.it.group451004.zarivniak.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();

        int m = scanner.nextInt();

        List<Event> events = new ArrayList<>(n * 2 + m);
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            events.add(new Event(start, EventType.START));
            events.add(new Event(stop, EventType.END));
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
            events.add(new Event(points[i], EventType.POINT, i));
        }

        Collections.sort(events, Comparator.comparingInt(Event::getTime)
                .thenComparing(e -> e.type.order));

        int activeCameras = 0;
        for (Event event : events) {
            switch (event.type) {
                case START:
                    activeCameras++;
                    break;
                case END:
                    activeCameras--;
                    break;
                case POINT:
                    result[event.pointIndex] = activeCameras;
                    break;
            }
        }

        return result;
    }

    private enum EventType {
        START(-1), POINT(0), END(1);

        final int order;

        EventType(int order) {
            this.order = order;
        }
    }

    private static class Event {
        int time;
        EventType type;
        int pointIndex;

        Event(int time, EventType type) {
            this.time = time;
            this.type = type;
        }

        Event(int time, EventType type, int pointIndex) {
            this.time = time;
            this.type = type;
            this.pointIndex = pointIndex;
        }

        int getTime() {
            return time;
        }
    }
}