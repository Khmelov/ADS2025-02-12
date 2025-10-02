package by.it.group451001.serganovskij.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозрастающая подпоследовательность

Условие:
Найти максимальную по длине подпоследовательность элементов массива,
в которой каждый следующий элемент не больше предыдущего.
Нужно вывести её длину и индексы (индексация с 1), соблюдая порядок.

Пример:
Вход:  5
       5 3 4 4 2
Выход: 4
       1 3 4 5
*/

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        // Получение входного потока из файла
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream); // Запуск метода решения задачи
        System.out.print(result); // Вывод результата (только длины последовательности)
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        // Подготовка к чтению данных из входного потока
        Scanner scanner = new Scanner(stream);

        // !!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Считываем длину массива
        int n = scanner.nextInt();
        int[] m = new int[n]; // Сам массив

        // Считываем сам массив
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Массив dp будет хранить длину наибольшей невозрастающей подпоследовательности,
        // заканчивающейся в каждом элементе массива
        int result = 0; // Итоговая длина наибольшей подпоследовательности
        int[] dp = new int[n]; // dp[i] — максимальная длина подпоследовательности, заканчивающейся на i-м элементе

        dp[0] = 1; // Начинаем с того, что первая позиция — это подпоследовательность из одного элемента

        // Заполняем dp-массив по принципу динамического программирования
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Если текущий элемент m[i] не больше предыдущего m[j], то он может быть добавлен
                // к подпоследовательности, заканчивающейся на j
                if (m[j] >= m[i]) {
                    // Выбираем максимум между уже найденным dp[i] и новой возможной длиной
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            // Обновляем результат, если на текущей позиции длина больше
            result = Math.max(result, dp[i]);
        }

        // !!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Возвращаем только длину наибольшей невозрастающей подпоследовательности
        return result;
    }

}
