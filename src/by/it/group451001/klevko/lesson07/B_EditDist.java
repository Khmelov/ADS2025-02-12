package by.it.group451001.klevko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int[][] fieldMatrix = new int[one.length()+1][two.length()+1];
        for (int i = 0; i < fieldMatrix.length; i++) {fieldMatrix[i][0] = i;}
        for (int i = 1; i < fieldMatrix[0].length; i++) {fieldMatrix[0][i] = i;}

        for (int i = 1; i <= one.length(); i++) {
            for (int j = 1; j <= two.length(); j++) {
                int min = Math.min(fieldMatrix[i-1][j]+1, fieldMatrix[i][j-1]+1);
                int item;
                if (one.charAt(i-1) == two.charAt(j-1)) item = 0;
                else item = 1;
                min = Math.min(min, fieldMatrix[i-1][j-1] + item);
                fieldMatrix[i][j] = min;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return fieldMatrix[one.length()][two.length()];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}