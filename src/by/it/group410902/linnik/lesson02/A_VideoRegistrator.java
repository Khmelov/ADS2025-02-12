package by.it.group410902.linnik.lesson02;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
/*
Даны события events
реализуйте метод calcStartTimes, так, чтобы число включений регистратора на
заданный период времени (1) было минимальным, а все события events
были зарегистрированы.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1); //рассчитаем моменты старта, с длинной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        //events - события которые нужно зарегистрировать
        //timeWorkDuration время работы видеокамеры после старта
        Arrays.sort(events);
        //for (double event : events) System.out.printf(String.valueOf(event));
        List<Double> result;
        result = new ArrayList<>();
        int i = 0;                              //i - это индекс события events[i]
        while (i < events.length) {
            double startTime = events[i];
            double endTime = startTime + workDuration;
            //System.out.printf(String.valueOf(endTime));

            result.add(events[i]);
            while (i < events.length && events[i] <= endTime){
                i++;
            }
        }
        //Комментарии от проверочного решения сохранены для подсказки, но вы можете их удалить.
        //Подготовка к жадному поглощению массива событий
        //hint: сортировка Arrays.sort обеспечит скорость алгоритма
        //C*(n log n) + C1*n = O(n log n)
        return result;                        //вернем итог
    }
}
