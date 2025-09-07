package by.it.group451002.vishnevskiy.lesson02;
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
        double i = -1;                              //*i - 1 чтобы показать, что камера пока не работает
        //Комментарии от проверочного решения сохранены для подсказки, но вы можете их удалить.
        //Подготовка к жадному поглощению массива событий
        //hint: сортировка Arrays.sort обеспечит скорость алгоритма
        //C*(n log n) + C1*n = O(n log n)
        //пока есть незарегистрированные события
        //получим одно событие по левому краю
        //и запомним время старта видеокамеры
        //вычислим момент окончания работы видеокамеры
        //и теперь пропустим все покрываемые события
        //за время до конца работы, увеличивая индекс
        Arrays.sort(events);

        //*Проверяет, покрывается ли текущее событие временем работы регистратора.
        //*Если событие event больше времени окончания текущего регистратора i, то регистратор нужно включить.
        for (double event : events)
            if (event > i) {
                result.add(event);
                //*i = event + workDuration:
                //Обновляет время окончания работы регистратора.
                //*Теперь регистратор будет работать до времени event + workDuration.
                i = event + workDuration;
            }
        return result;                        //вернем итог
    }
}