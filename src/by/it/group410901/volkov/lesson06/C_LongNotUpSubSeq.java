package by.it.group410901.volkov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        // Создаем поток для чтения данных из файла
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        // Создаем экземпляр класса
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        // Получаем результат - длину наибольшей невозрастающей подпоследовательности
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        // Подготовка сканера для чтения данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение длины последовательности
        int n = scanner.nextInt();
        // Создание массива для хранения последовательности
        int[] m = new int[n];

        // Чтение всех элементов последовательности
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Инициализация массивов для динамического программирования
        int[] dp = new int[n]; // dp[i] - длина наибольшей невозрастающей подпоследовательности, заканчивающейся в m[i]
        int[] prev = new int[n]; // Массив для хранения предыдущих индексов

        // Заполняем массивы начальными значениями
        Arrays.fill(dp, 1); // Каждый элемент сам по себе является подпоследовательностью длины 1
        Arrays.fill(prev, -1); // -1 означает отсутствие предыдущего элемента

        // Переменные для хранения максимальной длины и индекса последнего элемента
        int maxLength = 1;
        int lastIndex = 0;

        // Заполнение массивов dp и prev
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Если текущий элемент не больше предыдущего и можем увеличить длину подпоследовательности
                if (m[i] <= m[j] && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j; // Запоминаем предыдущий индекс
                }
            }
            // Обновляем максимальную длину и индекс последнего элемента
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстановление подпоследовательности по массиву prev
        List<Integer> indices = new ArrayList<>();
        int current = lastIndex;
        while (current != -1) {
            indices.add(current + 1); // +1 потому что индексы должны начинаться с 1
            current = prev[current];
        }
        Collections.reverse(indices); // Переворачиваем, так как добавляли с конца

        // Вывод результата
        System.out.println(maxLength); // Длина подпоследовательности
        for (int i = 0; i < indices.size(); i++) {
            System.out.print(indices.get(i) + " "); // Индексы элементов
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return maxLength;
    }
}