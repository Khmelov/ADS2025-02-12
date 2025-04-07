package by.it.group451002.sidarchuk.lesson02;

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
        List<Double> starts = instance.calcStartTimes(events, 1); //рассчитаем моменты старта, с длинной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        //events - события которые нужно зарегистрировать
        //timeWorkDuration время работы видеокамеры после старта

        List<Double> result = new ArrayList<>();
        Arrays.sort(events); // сортируем события
        int i = 0;     //i - это индекс события events[i]
        int n = events.length; // количество событий

        //Комментарии от проверочного решения сохранены для подсказки, но вы можете их удалить.
        //Подготовка к жадному поглощению массива событий
        //hint: сортировка Arrays.sort обеспечит скорость алгоритма
        //C*(n log n) + C1*n = O(n log n)

        //пока есть незарегистрированные события
        while (i<n){
        //получим одно событие по левому краю
            double start = events[i];
        //и запомним время старта видеокамеры
            result.add(start);
        //вычислим момент окончания работы видеокамеры
            double end = start + workDuration;
        //и теперь пропустим все покрываемые события
            while (i<n && events[i]<=end) {
                i++;
            }
        }
        //за время до конца работы, увеличивая индекс


        return result;                        //вернем итог
    }
}
