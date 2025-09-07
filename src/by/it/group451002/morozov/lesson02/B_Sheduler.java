package by.it.group451002.morozov.lesson02;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
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

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        //Events - события которые нужно распределить в аудитории
        //в период [from, int] (включительно).
        //оптимизация проводится по наибольшему числу непересекающихся событий.
        //Начало и конец событий могут совпадать.
        List<Event> result;
        result = new ArrayList<>();
        //ваше решение.
        
        // Sorting an array (Selection sort)
        for (int i = 0; i < events.length-1; i++) {
        	int minIndex = i;
        	for (int j = i+1; j < events.length; j++) {
        		if (events[j].stop < events[minIndex].stop) {
        			minIndex = j;
        		} 
        	}
        	Event temp = events[i];
        	events[i] = events[minIndex];
        	events[minIndex] = temp;
        }
        
        for (Event e: events) {
        	if (e.start >= from && e.stop <= to) result.add(e);
        }
        
        for (int i = 0; i < result.size(); i++) {
	        int stop = result.get(i).stop;
	        for (int j = i+1; j < result.size(); j++) {
	        	if (result.get(j).start < stop) {
	        		result.remove(j);
	        		j--;
	        	}
	        }
	    }
        
        return result;          //вернем итог
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