package by.it.group410901.kalach.lesson06;

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
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt"); // Исправлено: B_LongDivComSubSeq → C_LongNotUpSubSeq
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        instance.getNotUpSeqSize(stream); // Исправлено: возвращаемое значение не используется, так как выводим внутри метода
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
        //тут реализуйте логику задачи методами динамического программирования (!!!)

        // dp[i] хранит длину наибольшей невозрастающей подпоследовательности, оканчивающейся на индексе i
        int[] dp = new int[n];
        // prev[i] хранит индекс предыдущего элемента в подпоследовательности, оканчивающейся на i
        int[] prev = new int[n];

        // Инициализация массивов
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Каждый элемент сам по себе — подпоследовательность длины 1
            prev[i] = -1; // Изначально предыдущего элемента нет
        }

        int maxLength = 1; // Длина наибольшей подпоследовательности
        int lastIndex = 0; // Индекс, на котором заканчивается наибольшая подпоследовательность

        // Динамическое программирование
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Если текущий элемент можно добавить к подпоследовательности, оканчивающейся на j
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            // Обновляем максимальную длину и индекс конца, если текущая подпоследовательность длиннее
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстановление подпоследовательности
        int[] indices = new int[maxLength];
        int currentIndex = lastIndex;
        for (int i = maxLength - 1; i >= 0; i--) {
            indices[i] = currentIndex + 1; // Индексы начинаются с 1
            currentIndex = prev[currentIndex];
        }

        // Вывод результата
        System.out.println(maxLength);
        for (int i = 0; i < maxLength; i++) {
            System.out.print(indices[i] + " ");
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return maxLength; // Возвращаем длину, хотя в текущей реализации она не используется в main
    }
}