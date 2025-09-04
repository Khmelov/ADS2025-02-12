package by.it.group410902.siomchena.lesson05;

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
        //тут реализуйте логику задачи с применением сортировки подсчетом
        int[]k=new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i=0; i<points.length; i++)
        {
            switch (points[i]) {
                case 1: k[0]++; break;
                case 2: k[1]++; break;
                case 3: k[2]++; break;
                case 4: k[3]++; break;
                case 5: k[4]++; break;
                case 6: k[5]++; break;
                case 7: k[6]++; break;
                case 8: k[7]++; break;
                case 9: k[8]++; break;
                case 10: k[9]++; break;
            }
        }
       /* for (int i=0; i<points.length; i++)
        {
            for (int j=0; j<10; j++)
            {
                while (k[j]!=0)
                {
                    points[i]=j+1;
                    k[j]--;
                    i++;
                }
            }
        }*/
        int index = 0;
        for (int j = 0; j < 10; j++) {
            while (k[j] > 0) {
                points[index++] = j + 1;
                k[j]--;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
