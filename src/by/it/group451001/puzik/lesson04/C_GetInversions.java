package by.it.group451001.puzik.lesson04;

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
        
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        // Используем модифицированную сортировку слиянием для подсчета инверсий
        int[] temp = new int[n];
        int result = mergeSortAndCount(a, temp, 0, n - 1);
        
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    
    // Функция для сортировки и подсчета инверсий
    private int mergeSortAndCount(int[] arr, int[] temp, int left, int right) {
        int invCount = 0;
        
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            invCount += mergeSortAndCount(arr, temp, left, mid);
            invCount += mergeSortAndCount(arr, temp, mid + 1, right);
            
            invCount += mergeAndCount(arr, temp, left, mid, right);
        }
        
        return invCount;
    }
    
    // Функция для слияния и подсчета инверсий
    private int mergeAndCount(int[] arr, int[] temp, int left, int mid, int right) {
        int invCount = 0;
        
        for (int i = left; i <= right; i++) {
            temp[i] = arr[i];
        }
        
        int i = left;     // Индекс для левой части
        int j = mid + 1;  // Индекс для правой части
        int k = left;     // Индекс для основного массива
        
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                // Если элемент из правой части меньше, то это инверсия
                // для всех оставшихся элементов в левой части
                arr[k++] = temp[j++];
                invCount += (mid - i + 1);
            }
        }
        
        while (i <= mid) {
            arr[k++] = temp[i++];
        }
        
        return invCount;
    }
}
