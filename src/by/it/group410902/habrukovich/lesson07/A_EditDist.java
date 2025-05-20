package by.it.group410902.habrukovich.lesson07;

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
private int [][] rast;

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int lenOne = one.length();
        int lenTwo = two.length();
        rast = new int[lenOne + 1][lenTwo + 1];
        for (int i = 0; i <= lenOne; i++) {
            for (int j = 0; j <= lenTwo; j++) {
                rast[i][j] = -1;
            }
        }
        return calculateRast(lenOne, lenTwo, one, two);
    }
    private int calculateRast (int i, int j, String one, String two) {
        if (i==0) return j;
        if (j==0) return i;
        if (rast[i][j]!=-1) return rast[i][j];
        if (one.charAt(i-1)==two.charAt(j-1)) rast[i][j]=calculateRast(i-1,j-1,one,two);
        else {
            int insert = calculateRast(i,j-1,one,two);
            int delete = calculateRast(i-1,j,one,two);
            int replace = calculateRast(i-1,j-1,one,two);
            rast[i][j]=1+ Math.min (insert,Math.min(delete,replace));
        }
        return rast[i][j];
    }



        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!



    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
