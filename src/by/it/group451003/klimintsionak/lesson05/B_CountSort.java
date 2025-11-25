package by.it.group451003.klimintsionak.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)

Пример: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
Вольный перевод: http://programador.ru/sorting-positive-int-linear-time/
*/
public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        //читаем точки
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        int min, max = min = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i] < min) {
                min = points[i];
            }
            if (points[i] > max) {
                max = points[i];
            }
        }
        //реализуем сортировку подсчетом
        int[] count = new int[max - min + 1];
        //подсчитываем частоту каждого числа
        for (int i = 0; i < points.length; i++) {
            // подсчитываем сколько раз встречается число,
            // встретилось +1 к счетчику
            count[points[i] - min]++;
        }

        //формируем отсортированный массив
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            // count[0]=1, значит array[0]=0;
            // count[1]=2, значит вставляем два раза array[1]=array[2]=1;
            // count[2]=1, опять только один раз. array[3]=2;
            // count[3]=0, значит ничего не вставляем и т.д.
            for (int j = 0; j < count[i]; j++) {
                points[index++] = i + min;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }
}