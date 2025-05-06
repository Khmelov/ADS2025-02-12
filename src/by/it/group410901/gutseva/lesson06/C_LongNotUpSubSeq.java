package by.it.group410901.gutseva.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозростающая подпоследовательность

Дано:
    целое число 1<=n<=1E5 ( ОБРАТИТЕ ВНИМАНИЕ НА РАЗМЕРНОСТЬ! )
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] не больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]>=A[i[j+1]].

    В первой строке выведите её длину k,
    во второй - её индексы i[1]<i[2]<…<i[k]
    соблюдая A[i[1]]>=A[i[2]]>= ... >=A[i[n]].

    (индекс начинается с 1)

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    5 3 4 4 2

    Sample Output:
    4
    1 3 4 5
*/


public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);

        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        // Чтение входных данных
        int n = scanner.nextInt(); // Длина последовательности
        int[] m = new int[n];      // Массив для хранения последовательности

        // Заполнение массива числами из входных данных
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Инициализация структур данных:
        // dp[i] - длина наибольшей невозрастающей подпоследовательности, заканчивающейся в m[i]
        int[] dp = new int[n];

        // prev[i] - индекс предыдущего элемента в подпоследовательности для m[i]
        int[] prev = new int[n];

        // tailIndices[i] - индекс последнего элемента подпоследовательности длины i+1
        int[] tailIndices = new int[n];

        // Инициализация массива предыдущих элементов
        for (int i = 0; i < n; i++) {
            prev[i] = -1; // -1 означает отсутствие предыдущего элемента
        }

        int len = 0; // Текущая максимальная длина подпоследовательности

        // Основной алгоритм:
        // Для каждого элемента в последовательности
        for (int i = 0; i < n; i++) {
            // Бинарный поиск места для текущего элемента m[i] в массиве tailIndices
            // Ищем первый элемент в tailIndices, который МЕНЬШЕ m[i]
            int left = 0;
            int right = len;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (m[tailIndices[mid]] < m[i]) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            // left теперь содержит позицию, куда можно вставить текущий элемент
            // Обновляем tailIndices:
            tailIndices[left] = i;

            // Запоминаем длину подпоследовательности для текущего элемента
            dp[i] = left + 1;

            // Если мы расширили подпоследовательность (нашли более длинную)
            if (left == len) {
                len++;
            }

            // Запоминаем предыдущий элемент в подпоследовательности (если он есть)
            if (left > 0) {
                prev[i] = tailIndices[left - 1];
            }
        }

        // Результат - длина наибольшей невозрастающей подпоследовательности
        int result = len;

        // Восстановление самой подпоследовательности:
        int[] sequence = new int[len]; // Массив для хранения индексов элементов

        // Начинаем с последнего элемента самой длинной подпоследовательности
        int pos = tailIndices[len - 1];

        // Идем по цепочке предыдущих элементов
        for (int i = len - 1; i >= 0; i--) {
            sequence[i] = pos + 1; // +1 для индексации с 1 (по условию задачи)
            pos = prev[pos];      // Переходим к предыдущему элементу
        }

        // Вывод результатов:
        System.out.println(result); // Длина подпоследовательности

        // Вывод индексов элементов подпоследовательности
        for (int i = 0; i < len; i++) {
            System.out.print(sequence[i] + " ");
        }
        System.out.println();

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}