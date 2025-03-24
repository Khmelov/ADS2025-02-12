package by.it.group451001.smalian.lesson02;

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

        List<Event> starts = instance.calcStartTimes(events, 0, 10);
        System.out.println(starts);
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        //Events - события которые нужно распределить в аудитории
        //в период [from, int] (включительно).
        //оптимизация проводится по наибольшему числу непересекающихся событий.
        //Начало и конец событий могут совпадать.
        List<Event> result;
        result = new ArrayList<>();

        boolean[] us = new boolean[events.length];
        for (int i=0;i<events.length;i++)
            us[i] = false;
        int k=events.length;
        while (k>0){
            int minr=1000000000, minl=1000000000, ind=-1;
            for (int i=0;i<events.length;i++){
                if (us[i])continue;
                if (events[i].stop<minr)
                    minr = events[i].stop;
            }
            for (int i=0;i<events.length;i++){
                if (us[i])continue;
                if ((events[i].stop == minr) && (events[i].start >= from) && (events[i].stop <= to) && (events[i].start<minl)) {
                    ind = i;
                    minl = events[i].start;
                }
            }
            if (ind!=-1){
                result.add(events[ind]);
                from = events[ind].stop;
            }
            k = 0;
            for (int i=0;i<events.length;i++) {
                if (events[i].stop == minr)
                    us[i] = true;
                if (!us[i])
                    k++;
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