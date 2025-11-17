package by.it.group451002.shutko.lesson05;

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
//Создается массив count размером 11 (индексы от 0 до 10). Индекс 0 не будет использоваться, так как числа начинаются с 1.
//Каждый индекс массива count будет хранить количество вхождений соответствующего числа из массива points.
//Подсчет вхождений:
//В цикле проходит по каждому числу в массиве points и увеличивает соответствующий индекс в массиве count на 1.
// Это позволяет быстро подсчитать, сколько раз каждое число встречается в исходном массиве.
//Заполнение отсортированного массива:
//После подсчета вхождений, второй цикл проходит по массиву count от 1 до 10.
//Для каждого числа, которое встречается более одного раза, заполняет массив points отсортированными значениями,
//добавляя число столько раз, сколько оно встречалось в исходном массиве.
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
        //тут реализуйте логику задачи с применением сортировки подсчетом

        // Поскольку числа не превышают 10, создаем массив счетчиков размером 11
        // (индексы от 0 до 10, хотя 0 не будет использоваться)
        int[] count = new int[11];

        // Подсчитываем количество вхождений каждого числа
        for (int num : points) {
            count[num]++;
        }

        // Заполняем исходный массив отсортированными числами
        int index = 0;
        for (int num = 1; num <= 10; num++) {
            while (count[num] > 0) {
                points[index++] = num;
                count[num]--;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
