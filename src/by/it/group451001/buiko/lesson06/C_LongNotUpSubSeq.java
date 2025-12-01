package by.it.group451001.buiko.lesson06;

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
        //тут реализуйте логику задачи методами динамического программирования (!!!)
        int[] tails = new int[n]; // Индексы элементов массива m, формирующих подпоследовательность
        int length = 0; // Текущая длина наибольшей невозрастающей подпоследовательности

        for (int i = 0; i < n; i++) {
            int current = m[i];
            int left = 0;
            int right = length;

            // Бинарный поиск позиции для вставки текущего элемента
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (m[tails[mid]] < current) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            // Если найденная позиция равна текущей длине, расширяем подпоследовательность
            if (left == length) {
                tails[length++] = i;
            } else {
                // Заменяем элемент в найденной позиции на текущий индекс
                tails[left] = i;
            }
        }
        int result = length;


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}
//Инициализация массива tails: Хранит индексы элементов исходного массива m,
// которые формируют невозрастающую подпоследовательность.
//Бинарный поиск: Для каждого элемента m[i] определяется позиция в массиве tails,
// где можно вставить текущий элемент, чтобы сохранить невозрастающий порядок.
//Обновление tails: Если позиция равна текущей длине подпоследовательности,
// элемент добавляется в конец, увеличивая длину. В противном случае,
// элемент заменяет существующий в найденной позиции, оптимизируя структуру
// для потенциально более длинных подпоследовательностей.