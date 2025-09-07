package by.it.group451002.kravtsov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.*;

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
        // наибольшую невозрастающую подпоследовательность
        Scanner scanner = new Scanner(stream);

        // Считываем длину массива
        int n = scanner.nextInt();
        int[] A = new int[n];

        // Заполняем массив числами из входного потока
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }

        if (n == 0) return 0; // Проверка на пустую последовательность

        int[] dp = new int[n]; // dp[i] — длина наибольшей невозрастающей подпоследовательности
        Arrays.fill(dp, 1); // Минимальная длина подпоследовательности = 1 (каждый элемент сам по себе)

        // Массив предшественников для восстановления индексов
        int[] prev = new int[n];
        Arrays.fill(prev, -1);

        int maxLength = 1, lastIndex = 0;

        // Динамическое программирование для поиска невозрастающей подпоследовательности
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (A[j] >= A[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            // Если нашли более длинную последовательность, обновляем её параметры
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстанавливаем индексы подпоследовательности
        List<Integer> sequenceIndexes = new ArrayList<>();
        while (lastIndex != -1) {
            sequenceIndexes.add(lastIndex + 1); // Индекс начинается с 1
            lastIndex = prev[lastIndex];
        }

        // Выводим длину подпоследовательности
        System.out.println(maxLength);

        // Выводим индексы в обратном порядке (так как восстанавливались с конца)
        Collections.reverse(sequenceIndexes);
        for (int index : sequenceIndexes) {
            System.out.print(index + " ");
        }

        return maxLength; // Возвращаем длину подпоследовательности
    }
}
