package by.it.group451003.rashchenya.lesson02;

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

    //Первый запуск регистратора происходит в момент наступления первого события (после сортировки).
    //Затем нужно пропустить все события, которые попадают в интервал работы регистратора после этого запуска
    // (т.е. события, которые происходят в промежутке от startTime до startTime + workDuration).
    //Следующий запуск регистратора происходит в момент наступления первого события,
    //которое не было покрыто предыдущим запуском.
    //Процесс повторяется до тех пор, пока все события не будут покрыты.

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        //events - события которые нужно зарегистрировать
        //timeWorkDuration время работы видеокамеры после старта
        List<Double> result;
        result = new ArrayList<>();
        int i = 0;                              //i - это индекс события events[i]
        //Комментарии от проверочного решения сохранены для подсказки, но вы можете их удалить.
        //Подготовка к жадному поглощению массива событий
        //hint: сортировка Arrays.sort обеспечит скорость алгоритма
        //C*(n log n) + C1*n = O(n log n)
        Arrays.sort(events);
        while (i < events.length) {
            //получим одно событие по левому краю
            double startTime = events[i];
            //и запомним время старта видеокамеры
            result.add(startTime);
            //вычислим момент окончания работы видеокамеры
            double endTime = startTime + workDuration;
            //и теперь пропустим все покрываемые события
            //за время до конца работы, увеличивая индекс
            while (i < events.length && events[i] <= endTime) {
                i++;
            }
        }


        return result;                        //вернем итог
    }
}
