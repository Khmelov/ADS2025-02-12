package by.it.group451002.jasko.lesson04;

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
        // Получаем входной поток данных из файла
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        // Создаем экземпляр класса для подсчета инверсий
        C_GetInversions instance = new C_GetInversions();
        // Вызываем метод подсчета инверсий
        int result = instance.calc(stream);
        // Выводим результат
        System.out.print(result);
    }

    int calc(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение размера массива
        int n = scanner.nextInt();
        // Создание и заполнение массива
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызов модифицированной сортировки слиянием с подсчетом инверсий для всего массива
        return mergeSortAndCount(a, 0, a.length - 1);
    }

    // Рекурсивная функция сортировки слиянием с подсчетом инверсий
    private int mergeSortAndCount(int[] array, int left, int right) {
        int inversionCount = 0;

        // Базовый случай: если подмассив содержит более одного элемента
        if (left < right) {
            // Находим середину подмассива
            int mid = left + (right - left) / 2;

            // Рекурсивно считаем инверсии в левой половине
            inversionCount += mergeSortAndCount(array, left, mid);
            // Рекурсивно считаем инверсии в правой половине
            inversionCount += mergeSortAndCount(array, mid + 1, right);

            // Считаем инверсии при слиянии двух половин
            inversionCount += mergeAndCount(array, left, mid, right);
        }
        return inversionCount;
    }

    // Функция слияния двух отсортированных подмассивов с подсчетом инверсий
    private int mergeAndCount(int[] array, int left, int mid, int right) {
        // Размеры временных подмассивов
        int leftSize = mid - left + 1;  // Размер левого подмассива
        int rightSize = right - mid;    // Размер правого подмассива

        // Создаем временные массивы
        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];

        // Копируем данные во временные массивы
        System.arraycopy(array, left, leftArray, 0, leftSize);
        System.arraycopy(array, mid + 1, rightArray, 0, rightSize);

        int i = 0;      // Индекс для левого подмассива
        int j = 0;      // Индекс для правого подмассива
        int k = left;   // Индекс для основного массива
        int swaps = 0;   // Счетчик инверсий

        // Процесс слияния с подсчетом инверсий
        while (i < leftSize && j < rightSize) {
            if (leftArray[i] <= rightArray[j]) {
                // Если элемент из левого подмассива меньше или равен, инверсий не возникает
                array[k++] = leftArray[i++];
            } else {
                // Если элемент из правого подмассива меньше,
                // то он образует инверсии со всеми оставшимися элементами
                // левого подмассива (так как левый подмассив уже отсортирован)
                array[k++] = rightArray[j++];
                swaps += (leftSize - i); // Увеличиваем счетчик инверсий
            }
        }

        // Копируем оставшиеся элементы левого подмассива (если есть)
        while (i < leftSize) {
            array[k++] = leftArray[i++];
        }

        // Копируем оставшиеся элементы правого подмассива (если есть)
        while (j < rightSize) {
            array[k++] = rightArray[j++];
        }

        return swaps; // Возвращаем количество инверсий при этом слиянии
    }
}