package by.it.group451002.yeshmanski.lesson2;

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
        List<Double> result;
        result = new ArrayList<>();
        int i = 0;                              //i - это индекс события events[i]
        //Комментарии от проверочного решения сохранены для подсказки, но вы можете их удалить.
        //Подготовка к жадному поглощению массива событий
        //hint: сортировка Arrays.sort обеспечит скорость алгоритма
        //C*(n log n) + C1*n = O(n log n)

        //События сортируются по времени их наступления
        Arrays.sort(events);

        //Выбираем самое раннее необработанное событие (events[i]) в качестве времени старта регистратора
        while (i < events.length) {

            double startTime = events[i];

            //время старта
            result.add(startTime);

            //момент окончания работы видеокамеры
            double endTime = startTime + workDuration;

            //Пропускаем все события,
            //которые уже покрыты текущим включением
            while (i < events.length && events[i] <= endTime) {
                i++;
            }
        }


        return result;
    }
}
