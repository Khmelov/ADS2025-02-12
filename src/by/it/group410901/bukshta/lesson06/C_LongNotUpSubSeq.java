package by.it.group410901.bukshta.lesson06;

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
        // Исправлено: используем правильный класс для чтения потока
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        // Тест ожидает только возвращаемое значение, вывод осуществляется в методе
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

        // Массив dp[i] хранит длину наибольшей невозрастающей подпоследовательности,
        // заканчивающейся на элементе m[i]
        int[] dp = new int[n];

        // Массив prev[i] хранит индекс предыдущего элемента в подпоследовательности,
        // заканчивающейся на m[i], для восстановления ответа
        int[] prev = new int[n];

        // Инициализируем массивы
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Каждый элемент — подпоследовательность длины 1
            prev[i] = -1; // Изначально нет предыдущего элемента
        }

        // Для каждого элемента проверяем все предыдущие элементы
        // Если текущий элемент не больше предыдущего, можно продлить подпоследовательность
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (m[i] <= m[j] && dp[j] + 1 > dp[i]) {
                    // Обновляем dp[i] и сохраняем индекс предыдущего элемента
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
        }

        // Находим максимальную длину подпоследовательности и её последний индекс
        int maxLen = 0;
        int lastIndex = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                lastIndex = i;
            }
        }

        // Восстанавливаем индексы подпоследовательности
        int[] indices = new int[maxLen];
        int currentIndex = lastIndex;
        for (int i = maxLen - 1; i >= 0; i--) {
            indices[i] = currentIndex + 1; // Индексы начинаются с 1
            currentIndex = prev[currentIndex];
        }

        // Выводим результат в требуемом формате
        System.out.println(maxLen);
        for (int i = 0; i < maxLen; i++) {
            System.out.print(indices[i] + " ");
        }

        // Возвращаем длину подпоследовательности для теста
        return maxLen;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }
}