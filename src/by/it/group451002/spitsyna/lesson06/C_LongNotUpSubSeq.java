package by.it.group451002.spitsyna.lesson06;

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
        int result = 0;

        //создадим массив d, каждый элемент d[i] которого содержит длину максимальной невозрастающей
        //последовательности, заканчивающейся m[i]
        int[] d = new int[n];
        //создадим массив prev, каждый элемент prev[i] которого содержит индекс элемента, на который
        //заканчивается максимальная невозрастающая последовательность длины d[i](без учета самого элемента)
        int[] prev = new int[n];

        for (int i = 0; i <= n-1; i++){
            d[i] = 1;
            prev[i] = -1;
            for (int j = 0; j <= i-1; j++){
                if ((m[j] >= m[i]) && (d[j]+1 > d[i])){
                    d[i] = d[j]+1;
                    prev[i] = j;
                }
            }
        }

        //находим максимальную длину невозрастающей последовательности
        result = d[0];
        int ind = 0;
        for (int i = 0; i <= n-1; i++){
            if (d[i] > result){
                ind = i; //индекс элемента, на котором была достигнута максимальная длина
                result = d[i];
            }
        }

        //восстановление результата
        String resInd = "";
        while (ind != -1){
            resInd = resInd + String.valueOf(ind+1);
            ind = prev[ind];
        }

        //переворачиваем строку, так как шли с конца при ее восстановлении
        resInd = new StringBuilder(resInd).reverse().toString();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}