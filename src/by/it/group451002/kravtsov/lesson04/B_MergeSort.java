package by.it.group451002.kravtsov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

// Читаем размер массива из входных данных
        int n = scanner.nextInt();

// Инициализируем массив размером n и заполняем его
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt(); // Читаем элементы массива
        }

// Итеративная сортировка слиянием (без рекурсии)
// Увеличиваем размер объединяемых подмассивов на каждой итерации
        for (int size = 1; size < n; size *= 2) {
            for (int leftStart = 0; leftStart < n - size; leftStart += 2 * size) {
                int mid = leftStart + size - 1; // Определяем середину
                int rightEnd = Math.min(leftStart + 2 * size - 1, n - 1); // Определяем конец правого подмассива

                // Создаем временные массивы для левой и правой части
                int n1 = mid - leftStart + 1;
                int n2 = rightEnd - mid;

                int[] leftArray = new int[n1];
                int[] rightArray = new int[n2];

                // Копируем данные во временные массивы
                for (int i = 0; i < n1; i++)
                    leftArray[i] = a[leftStart + i];
                for (int j = 0; j < n2; j++)
                    rightArray[j] = a[mid + 1 + j];

                // Объединение двух отсортированных частей
                int i = 0, j = 0, k = leftStart;
                while (i < n1 && j < n2) {
                    if (leftArray[i] <= rightArray[j]) {
                        a[k] = leftArray[i];
                        i++;
                    } else {
                        a[k] = rightArray[j];
                        j++;
                    }
                    k++;
                }

                // Копируем оставшиеся элементы левой части
                while (i < n1) {
                    a[k] = leftArray[i];
                    i++;
                    k++;
                }

                // Копируем оставшиеся элементы правой части
                while (j < n2) {
                    a[k] = rightArray[j];
                    j++;
                    k++;
                }
            }
        }

//!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


        return a;
    }
}
