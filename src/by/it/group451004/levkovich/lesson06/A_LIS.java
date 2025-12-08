package by.it.group451004.levkovich.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;
/*
Задача на программирование: наибольшая возрастающая подпоследовательность
см.     https://ru.wikipedia.org/wiki/Задача_поиска_наибольшей_увеличивающейся_подпоследовательности
        https://en.wikipedia.org/wiki/Longest_increasing_subsequence

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    где каждый элемент A[i[k]] больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]<A[i[j+1]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    1 3 3 2 6

    Sample Output:
    3
*/

public class A_LIS {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        int l = 0; //максимальная длина подпоследовательности к данному моменту
        int[] m = new int[n + 1];   //m[l] хранит индекс k минимального значения arr[k]=arr[m[l]] такого, что есть
        //возрастающая подпоследовательность длины l оканчивающаяся
        //в arr[k]=arr[m[l]], где k <= i
        //m[0] не определено, нужно пропускать
        Stack<ValInd>[] M = new Stack[n + 1]; //для отладки, хорошо показывает работу

        int[] p = new int[n];       //p[k] - хранит индекс предшественника arr[k] в наибольшей
        //возрастающей подпоследовательности оканчивающейся в arr[k]
        //p[m[l]] - позволяет рекурсивно собрать последовательность
        //arr[m[l]], arr[p[m[l]]], arr[p[p[m[l]]]], arr[p[p[p[m[l]]]]], ...
        //arr[m[1]], arr[m[1]], ..., arr[m[l]] - возрастающая последовательность
        for (int i = 0; i < n + 1; i++) {
            M[i] = new Stack<>();
        }

        for (int i = 0; i < n; i++) {
            int lo = 1; //так как m[0] не определено
            int hi = l; //так как m[] имеет длину l на данной итерации по i

            while (lo <= hi) {      //бинарный поиск минимальной длинны l, для которой arr[m[l]] >= arr[i]
                int mid = lo + (hi - lo) / 2;
                if (arr[m[mid]] < arr[i]) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
            int newL = lo;
            p[i] = m[newL - 1];
            M[newL].push(new ValInd(arr[i], i)); //для наглядности
            m[newL] = i;
            if (newL > l) {
                l = newL;
            }
        }
        return l;
    }

    private class ValInd {
        int value;
        int index;

        ValInd(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }
}