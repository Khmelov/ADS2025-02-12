package by.it.group451003.kharkevich.lesson06;

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
    public static int[] findLDS(int[] sequence) {
        int[] lengthsDS = new int[sequence.length];
        int[] previousIndices = new int[sequence.length];
        for (int i = 0; i < lengthsDS.length; i++) {
            lengthsDS[i] = 1;
            previousIndices[i] = -1;
            for (int j = 0; j < i; j++)
                if (sequence[j] >= sequence[i] && lengthsDS[j] + 1 > lengthsDS[i]) {
                    lengthsDS[i] = lengthsDS[j] + 1;
                    previousIndices[i] = j;
                }
        }

        int lengthLDS = 0;
        int endIndex = 0;
        for (int i = 0; i < lengthsDS.length; i++)
            if (lengthsDS[i] > lengthLDS) {
                lengthLDS = lengthsDS[i];
                endIndex = i;
            }

        int[] LDS = new int[lengthLDS];
        int currentIndex = lengthLDS - 1;
        while (endIndex >= 0) {
            LDS[currentIndex] = sequence[endIndex];
            currentIndex--;
            endIndex = previousIndices[endIndex];
        }

        return LDS;
    }
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
        int result = findLDS(m).length;


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}