package by.it.group451002.spizharnaya.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозрастающая подпоследовательность

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
        int result = 0;

        //Создаем массив, каждый элемент d[i] которого содержит максимальную длину
        //невозрастающей последовательности, заканчивающейся в m[i]
        int[] d = new int[n];

        for (int i = 0; i <= n-1; i++){
            d[i] = 1;
            for (int j = 0; j <= i-1; j++){
                //если элемент >= m[i] и длина заканчивающейся
                //в нем последовательности +1 больше d[i]
                if ((m[j]>=m[i]) && (d[j]+1 > d[i])){
                    d[i] = d[j]+1;
                }
            }
        }
        //Находим максимальный элемент в массиве d
        int ind=0;
        result = d[0];
        for (int i = 1; i <= n-1; i++){
            if (d[i] > result) {
                result = d[i];
                ind = i;
            }
        }
        //ind - индекс первого с конца элемента последовательности
        String ResInd = String.valueOf(ind+1);
        for (int i = ind-1; i>=0; i--){
            if (m[i]>=m[ind]){        //ind - индекс предыдущего элемента последовательности
                ind = i;
                ResInd = ResInd + String.valueOf(ind+1);
            }
        }
        //переворачиваем строку, так как шли с конца при ее восстановлении
        ResInd = new StringBuilder(ResInd).reverse().toString();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}