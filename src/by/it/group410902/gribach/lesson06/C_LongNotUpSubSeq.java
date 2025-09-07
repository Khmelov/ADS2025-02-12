package by.it.group410902.gribach.lesson06;

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
        // Подготовка к чтению данных из файла
        Scanner scanner = new Scanner(stream);

        // Считываем количество элементов в последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];

        // Считываем саму последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // tail[k] — минимально возможный последний элемент невозрастающей подпоследовательности длины (k + 1)
        int[] tail = new int[n];

        // tailIndices[k] — индекс в оригинальном массиве, соответствующий tail[k]
        int[] tailIndices = new int[n];

        // prev[i] — индекс предыдущего элемента в подпоследовательности для элемента i (используется для восстановления пути, если нужно)
        int[] prev = new int[n];

        // Длина наибольшей найденной невозрастающей подпоследовательности
        int length = 0;

        for (int i = 0; i < n; i++) {
            int x = m[i];

            // Бинарный поиск позиции, куда вставить текущий элемент x в tail
            // Поскольку мы ищем НЕВОЗРАСТАЮЩУЮ последовательность, то проверка: tail[mid] >= x
            int l = 0, r = length;
            while (l < r) {
                int mid = (l + r) / 2;
                if (tail[mid] >= x) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }

            int pos = l;

            // Обновляем массив tail: в позицию pos записываем x
            tail[pos] = x;

            // Сохраняем индекс текущего элемента в оригинальной последовательности
            tailIndices[pos] = i;

            // Обновляем массив prev: если это не начало, то сохраняем "предка"
            prev[i] = (pos > 0) ? tailIndices[pos - 1] : -1;

            // Если вставка произошла в конец — увеличиваем длину искомой подпоследовательности
            if (pos == length) {
                length++;
            }
        }

        // Возвращаем длину наибольшей невозрастающей подпоследовательности
        return length;
    }

}