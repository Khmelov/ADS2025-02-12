package by.it.group451004.rak.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<Double> starts = instance.calcStartTimes(events, 1); //рассчитаем моменты старта, с длиной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        List<Double> result;
        result = new ArrayList<>();
        int i = 0;
        Arrays.sort(events);
        while (i < events.length) {
            int j = 1;
            while ((i + j < events.length) && ((events[i + j] - events[i]) <= workDuration)) {
                j++;
            }
            result.add(events[i]);
            i += j;
        }
        //своеобразная задача об минимальном покрытии
        //жадный алгоритм в данном случае должен за 1 шаг покрыть максимальное количество событий, e_j-e_k =< workDuration
        return result;
    }
}
