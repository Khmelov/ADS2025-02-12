package by.it.group451002.jasko.lesson06;

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
        // Получаем входной поток данных из файла
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        // Получаем результат и выводим его
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    // Метод для нахождения наибольшей невозрастающей подпоследовательности
    int getNotUpSeqSize(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение длины последовательности
        int n = scanner.nextInt();
        // Создание массива для хранения последовательности чисел
        int[] sequence = new int[n];

        // Чтение последовательности чисел
        for (int i = 0; i < n; i++) {
            sequence[i] = scanner.nextInt();
        }

        // Массив dp для хранения длин наибольших невозрастающих подпоследовательностей
        // dp[i] - длина наибольшей невозрастающей подпоследовательности,
        // оканчивающейся на элементе sequence[i]
        int[] dp = new int[n];

        // Массив prev для хранения индексов предыдущих элементов в подпоследовательности
        // prev[i] - индекс предыдущего элемента в подпоследовательности
        int[] prev = new int[n];

        // Инициализация массивов:
        // Каждый элемент сам по себе является подпоследовательностью длины 1
        // Изначально у элементов нет предыдущих в подпоследовательности
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1;
        }

        // Заполнение массивов dp и prev:
        // Для каждого элемента sequence[i] проверяем все предыдущие элементы sequence[j]
        // (где j < i), и если sequence[i] <= sequence[j], то возможно увеличить dp[i]
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Проверяем два условия:
                // 1. Текущий элемент меньше или равен предыдущему
                // 2. Текущая длина подпоследовательности может быть увеличена
                if (sequence[i] <= sequence[j] && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1; // Обновляем длину подпоследовательности
                    prev[i] = j; // Сохраняем индекс предыдущего элемента
                }
            }
        }

        // Находим максимальное значение в массиве dp и индекс последнего элемента
        int maxLength = 0;
        int lastIndex = -1;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстанавливаем индексы элементов наибольшей невозрастающей подпоследовательности
        int[] indices = new int[maxLength];
        int currentIndex = lastIndex;
        for (int i = maxLength - 1; i >= 0; i--) {
            indices[i] = currentIndex + 1; // +1 потому что индексы в задаче начинаются с 1
            currentIndex = prev[currentIndex]; // Переходим к предыдущему элементу
        }

        // Выводим результат
        System.out.println(maxLength); // Длина подпоследовательности
        for (int index : indices) {
            System.out.print(index + " "); // Индексы элементов
        }
        System.out.println();

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return maxLength;
    }
}