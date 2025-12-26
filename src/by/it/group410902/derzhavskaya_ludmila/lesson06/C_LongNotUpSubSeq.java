package by.it.group410902.derzhavskaya_ludmila.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        // Инициализация структур данных
        int[] dp = new int[n]; // dp[i] - длина ННП для первых i+1 элементов
        int[] pos = new int[n]; // Позиции элементов в ННП
        int[] prev = new int[n]; // Предыдущие элементы для восстановления последовательности
        Arrays.fill(prev, -1);

        int len = 0; // Текущая длина ННП

        for (int i = 0; i < n; i++) {
            // Бинарный поиск места для текущего элемента
            int left = 0, right = len;
            while (left < right) {
                int mid = (left + right) >>> 1;
                if (m[pos[mid]] >= m[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // Обновление dp и pos
            dp[i] = left + 1;
            if (left < len) {
                pos[left] = i;
            } else {
                pos[len++] = i;
            }

            // Запоминаем предыдущий элемент
            if (left > 0) {
                prev[i] = pos[left - 1];
            }
        }

        // Восстановление последовательности
        List<Integer> indices = new ArrayList<>();
        int current = pos[len - 1];
        while (current != -1) {
            indices.add(current + 1); // +1 для индексации с 1
            current = prev[current];
        }

        // Вывод результатов
        System.out.println(len);
        for (int i = indices.size() - 1; i >= 0; i--) {
            System.out.print(indices.get(i) + " ");
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return len;
    }

}