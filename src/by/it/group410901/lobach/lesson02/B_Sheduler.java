package by.it.group410901.lobach.lesson02;

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
        B_Sheduler.Event[] events = {new B_Sheduler.Event(0, 3), new B_Sheduler.Event(0, 1), new B_Sheduler.Event(1, 2), new B_Sheduler.Event(3, 5),
                new B_Sheduler.Event(1, 3), new B_Sheduler.Event(1, 3), new B_Sheduler.Event(1, 3), new B_Sheduler.Event(3, 6),
                new B_Sheduler.Event(2, 7), new B_Sheduler.Event(2, 3), new B_Sheduler.Event(2, 7), new B_Sheduler.Event(7, 9),
                new B_Sheduler.Event(3, 5), new B_Sheduler.Event(2, 4), new B_Sheduler.Event(2, 3), new B_Sheduler.Event(3, 7),
                new B_Sheduler.Event(4, 5), new B_Sheduler.Event(6, 7), new B_Sheduler.Event(6, 9), new B_Sheduler.Event(7, 9),
                new B_Sheduler.Event(8, 9), new B_Sheduler.Event(4, 6), new B_Sheduler.Event(8, 10), new B_Sheduler.Event(7, 10)
        };

        List<B_Sheduler.Event> starts = instance.calcStartTimes(events, 0, 10);  //рассчитаем оптимальное заполнение аудитории
        System.out.println(starts);                                 //покажем рассчитанный график занятий
    }

    List<B_Sheduler.Event> calcStartTimes(B_Sheduler.Event[] events, int from, int to) {
        List<B_Sheduler.Event> result;
        result = new ArrayList<>();

        int bothFieldsMinInd;
        B_Sheduler.Event tempEvent;

        // Сортируем массив event-ов по возрастанию времени конца event-a
        // причём при наличии нескольких event-ов с одним и тем же временем конца,
        // раньше ставим тот, у которого раньше время начала (в целях максимизации расхода времени
        // на заданном промежутке)
        for (int i = 0; i < events.length; i++) {
            bothFieldsMinInd = i;

            for (int i1 = i + 1; i1 < events.length; i1++)
                if (events[i1].stop <= events[bothFieldsMinInd].stop)
                    if (events[i1].stop == events[bothFieldsMinInd].stop && events[i1].start < events[bothFieldsMinInd].start)
                        bothFieldsMinInd = i1;
                    else if (events[i1].stop < events[bothFieldsMinInd].stop)
                        bothFieldsMinInd = i1;

            tempEvent = events[i];
            events[i] = events[bothFieldsMinInd];
            events[bothFieldsMinInd] = tempEvent;
        }

        // проходимся по каждому event-у и фиксируем его, если его начало и конец лежат в диапазоне
        // [currTime, to], присваиваем currTime время конца фиксируемого event-а
        int currTime = from;
        for (int i = 0; i < events.length; i++)
            if (events[i].start >= currTime && events[i].stop <= to) {
                result.add(events[i]);
                currTime = events[i].stop;
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