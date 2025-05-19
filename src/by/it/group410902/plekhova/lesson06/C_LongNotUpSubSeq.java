package by.it.group410902.plekhova.lesson06;

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
            System.out.print(m[i] + " ");
        }
        System.out.println();
        int[] maxp = new int[n]; // хранит длину макс подпосл для каждого элемента
        int[][] maxind = new int[n][n]; // хранит индексы макс подпослед
        maxp[0] = 1; maxind[0][0] = 1;
        int max = 0, ind, resi = -1; // хранит знач макс пдпослед для данного эл
        int result = 1;
        // на каждом шаге ищем для тек эл тот, который будет меньше его, и чью подпоследовательность
        // будет максимальная
        for (int i = 1; i < n; i++) { // проходит по каждому эл
            max = 0; ind = -1;
            for (int j = i - 1; j > -1; j--) { // ищет макс подпоследовательность
                if (m[i] <= m[j]) {
                    if (maxp[j] > max) {
                        max = maxp[j];
                        ind = j;

                    }
                }
            }
                maxp[i] = max + 1;
            System.out.println("i : " + i);
                for(int k =0; i < ind+1; k++){
                    maxind[i][k] = maxind[ind][k];
                    System.out.print(maxind[i][k] + " ");
                }
                maxind[i][ind+1] = i;

                if (maxp[i] > result) {
                    result = maxp[i];
                    resi = i;
                }

        }


      //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}