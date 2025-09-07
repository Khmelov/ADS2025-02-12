package by.it.group451002.dirko.lesson06;

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

        // Создаем массивы lastIndForLength, хранящий последние индексы для определённой длины,
        // и prevIndices, хранящий предыдущие индексы
        int[] lastIndForLength = new int[n + 1];
        int[] prevIndices = new int[n];
        lastIndForLength[0] = -1;

        // Поиск максимальной невозрастающей подпоследовательности
        int maxLen = 0;
        for (int i = 0; i < n; i++) {
            // Ищем последний индекс l, такой что m[lastIndForLength[l]] >= m[I]
            int low = 1, high = maxLen + 1, mid;
            while (low < high) {
                mid = (low + high) / 2;
                if (m[lastIndForLength[mid]] < m[i]) { high = mid; }
                else { low = mid + 1; }
            }

            // Переопределяем новую максимальную длину
            int newMaxLen = low;

            // Обновляем предыдущий и последний индекс для текущей длины
            prevIndices[i] = lastIndForLength[newMaxLen-1];
            lastIndForLength[newMaxLen] = i;

            // Если новая длина больше, то меняем
            if (newMaxLen > maxLen) { maxLen = newMaxLen; }
        }

        // Инициализация массива S для максимальной подпоследовательности и заполнение его с конца
        int[] S = new int[maxLen];
        int k = lastIndForLength[maxLen];
        for (int i = maxLen - 1; i >= 0; i--) {
            S[i] = m[k];
            k = prevIndices[k];
        }

        // Вывод максимальной подпоследовательности
        for (int i = 0; i < maxLen; i++) {
            System.out.print(S[i] + " ");
        }
        System.out.println();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return maxLen;
    }

}