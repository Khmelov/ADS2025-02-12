package by.it.group410901.bukshta.lesson04;

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
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        // !!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        // Размер массива
        int n = scanner.nextInt();
        // Сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Подсчёт инверсий с использованием сортировки слиянием
        int result = mergeSortAndCount(a, 0, n - 1);

        // !!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        scanner.close();
        return result;
    }

    // Рекурсивная функция сортировки слиянием с подсчётом инверсий
    private int mergeSortAndCount(int[] array, int left, int right) {
        int invCount = 0;
        if (left < right) {
            int mid = (left + right) / 2;
            // Подсчёт инверсий в левой половине
            invCount += mergeSortAndCount(array, left, mid);
            // Подсчёт инверсий в правой половине
            invCount += mergeSortAndCount(array, mid + 1, right);
            // Подсчёт инверсий при слиянии
            invCount += mergeAndCount(array, left, mid, right);
        }
        return invCount;
    }

    // Функция слияния двух подмассивов с подсчётом инверсий
    private int mergeAndCount(int[] array, int left, int mid, int right) {
        // Размеры подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаём временные массивы
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        // Слияние с подсчётом инверсий
        int i = 0; // Индекс для левого подмассива
        int j = 0; // Индекс для правого подмассива
        int k = left; // Индекс для основного массива
        int invCount = 0;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                // Если leftArray[i] > rightArray[j], то это инверсия
                // Все оставшиеся элементы leftArray[i...n1-1] тоже образуют инверсии
                array[k] = rightArray[j];
                invCount += (n1 - i);
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы левого подмассива, если есть
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы правого подмассива, если есть
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }

        return invCount;
    }
}