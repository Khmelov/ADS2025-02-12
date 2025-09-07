package by.it.group451004.belkovich.lesson04;

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
//        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        InputStream stream = C_GetInversions.class.getResourceAsStream("test_array_100k.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    static long result = 0;

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        long startTime = System.currentTimeMillis();
//        try {
//            mergeSort(a, 0, n - 1);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        mergeSort2(a, 0, n - 1);
        long finishTime = System.currentTimeMillis();
        System.out.printf("StartTime=%d\nFinishTime=%d\nDelta=%d\n", startTime, finishTime, finishTime - startTime);
        System.out.print(result);
        return (int) result;
    }

    void mergeSort2(int[] a, int minA, int maxA) {
        if (maxA - minA < 2) {
            if (a[maxA] < a[minA]) {
                int temp = a[minA];
                a[minA] = a[maxA];
                a[maxA] = temp;
                result++;
            }
        } else {
            int mid = (maxA + minA) / 2;
            mergeSort2(a, minA, mid);
            mergeSort2(a, mid + 1, maxA);
            mergeSubarrays(a, minA, mid, maxA);
        }
    }

    static int threadCount = 0;

    void mergeSort(int[] a, int minA, int maxA) throws InterruptedException {
        if (maxA - minA < 2) {
            if (a[maxA] < a[minA]) {
                int temp = a[minA];
                a[minA] = a[maxA];
                a[maxA] = temp;
                result++;
            }
        } else {
            int mid = (maxA + minA) / 2;

            if (threadCount < 17) {
                Thread left = new Thread(() -> {
                    try {
                        mergeSort(a, minA, mid);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                Thread right = new Thread(() -> {
                    try {
                        mergeSort(a, mid + 1, maxA);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                threadCount++;
                left.start();
                threadCount++;
                right.start();

                left.join();
                threadCount--;
                right.join();
                threadCount--;
            } else {
                mergeSort(a, minA, mid);
                mergeSort(a, mid + 1, maxA);
            }
            mergeSubarrays(a, minA, mid, maxA);
        }
    }

    void mergeSubarrays(int[] source, int minA, int maxA, int maxB) {
        int ai = minA;
        int bi = maxA + 1;
        int length = maxB - minA + 1;
        int[] buf = new int[length];
        int index = 0;

        while (ai <= maxA && bi <= maxB) {
            if (source[ai] <= source[bi]) {
                buf[index++] = source[ai++];
            } else {
                buf[index++] = source[bi++];
                result += (maxA - ai + 1); // все оставшиеся в левой части больше, значит они формируют инверсии
            }
        }
        while (ai <= maxA) {
            buf[index++] = source[ai++];
        }
        while (bi <= maxB) {
            buf[index++] = source[bi++];
        }
        System.arraycopy(buf, 0, source, minA, length);
    }
}
