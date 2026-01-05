package by.it.group410902.varava.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

Sample Input:
5
2 3 9 2 9
Sample Output:
2

Головоломка (т.е. не обязательно).
Попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
Докажите рост производительности замерами времени.
Большой тестовый массив можно прочитать свой или сгенерировать его программно.
*/


public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int mergeSort(int array[], int start, int end) {
        int inv = 0;
        if (start < end) {
            int mid = (start + end) / 2;
            inv += mergeSort(array, start, mid);
            inv += mergeSort(array, mid + 1, end);
            inv += merge(array, start, end, mid);
        }
        return inv;
    }


    int merge(int array[], int start, int end, int mid) {
        int inv = 0;
        int[] mergedArray = new int[end - start + 1];
        int i = start, j = mid + 1, k = 0;

        while (i <= mid && j <= end) {
            if (array[i] <= array[j]) { // <= важно, чтобы сохранить стабильность сортировки
                mergedArray[k++] = array[i++];
            } else {
                mergedArray[k++] = array[j++];
                inv += (mid - i + 1); // Подсчет инверсий корректно
            }
        }

        while (i <= mid) {
            mergedArray[k++] = array[i++];
        }
        while (j <= end) {
            mergedArray[k++] = array[j++];
        }

        System.arraycopy(mergedArray, 0, array, start, mergedArray.length); // Копируем обратно

        return inv;
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!O2n!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //       int inv = 0;
        //for(int j = 0; j<n-1; j++){
        //for(int k=j; k<n; k++){

        // if(a[j]>a[k]){
        //     inv++;
        //  }
        // }
        //}
        //result = inv;
        result = mergeSort(a,0,n-1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
