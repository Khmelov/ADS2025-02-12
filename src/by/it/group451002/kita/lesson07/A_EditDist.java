package by.it.group451002.kita.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int [][] D = new int [one.length()+1][two.length()+1];

        //инициализируем массив D значением бесконечности
        for (int i = 0; i <= one.length();i++){
            for (int j = 0; j <= two.length();j++){
                D[i][j] = Integer.MAX_VALUE;
            }
        }
        int result = 0;

        for (int i = 0; i <= one.length();i++){
            for (int j = 0; j <= two.length();j++){
                D[i][j] = editDistTD(D, i, j, one, two);
            }
        }
        result = D[one.length()][two.length()];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    static int diff(char a, char b){
        if (a == b){
            return 0;
        }
        else {
            return 1;
        }
    }

    private static int editDistTD(int[][] D, int i, int j, String one, String two){
        if (D[i][j] == Integer.MAX_VALUE){
            if (i == 0){
                D[i][j] = j;
            }
            else if (j == 0){
                D[i][j] = i;
            }
            else {
                int ins = editDistTD(D,i,j-1, one, two)+1;//вставка
                int del = editDistTD(D,i-1,j, one, two)+1;//удаление
                int sub = editDistTD(D,i-1,j-1, one, two) + diff(one.charAt(i-1),two.charAt(j-1));//совпадение
                D[i][j] = Math.min(ins,Math.min(del,sub));
            }

        }
        return D[i][j];
    }



    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
