package by.it.group451001.alexandrovich.lesson02;

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

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        events = SortToEnd(events);
        //в период [from, int] (включительно).
        //оптимизация проводится по наибольшему числу непересекающихся событий.
        //Начало и конец событий могут совпадать.
        List<Event> result;
        result = new ArrayList<>();
        int time = from;
        int i = 0;
        while (time <= to){
            while (time > events[i].start){
                i++;
                if (i > events.length-1){
                    return  result;
                }
            }
            result.add(events[i]);
            time = events[i].stop;
        }
        //ваше решение.


        return result;          //вернем итог
    }

    static Event[] SortToEnd(Event[] events){
        int min;
        int minI;
        Event temp;
        for (var i = 0; i< events.length-1; i++){
            minI = i;
            min = events[i].stop;
            for (var j = i+1; j < events.length; j++){
                if (events[j].stop < min){
                    min = events[j].stop;
                    minI = j;
                }
            }
            temp = events[i];
            events[i] = events[minI];
            events[minI] = temp;
        }
        return events;
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