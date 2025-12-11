package by.it.group451002.shutko.lesson06;

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

    // Необходимо найти:
    // Длину самой длинной подпоследовательности, где каждый следующий элемент не больше предыдущего (A[i[j]] ≥ A[i[j+1]])
    // и нужно восстановить эту последовательность и вывести
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
        //тут реализуйте логику задачи методами динамического программирования (!!!)

        // Массив dp - хранит длины наибольших подпоследовательностей для каждого элемента
        // Массив prev - хранит индексы предыдущих элементов в подпоследовательности
        // Переменные для хранения максимальной длины и последнего индекса
        int[] dp = new int[n];
        int[] prev = new int[n];
        int maxLength = 0;
        int lastIndex = 0;

        // Заполнение массивов
        // Для каждого элемента i:
        // Инициализируем минимальную длину 1 (сам элемент)
        // Ищем среди предыдущих элементов j те, где m[j] >= m[i]
        // Обновляем длину и ссылку на предыдущий элемент при нахождении более длинной подпоследовательности
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1;
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстановление подпоследовательности:
        // Создаем массив для хранения индексов
        // Идем по ссылкам prev от последнего элемента к первому
        // Заполняем массив в обратном порядке
        int[] sequence = new int[maxLength];
        int current = lastIndex;
        for (int i = maxLength - 1; i >= 0; i--) {
            sequence[i] = current + 1;
            current = prev[current];
        }

        System.out.println(maxLength);
        for (int i = 0; i < maxLength; i++) {
            System.out.print(sequence[i] + " ");
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return maxLength;
    }

}