package by.it.group410902.gavlev.lesson07;

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
        int[][] matrix = new int[one.length() + 1][two.length() + 1];

        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                matrix[i][j] = -1;
            }
        }

        return getLevenshLen(matrix, one, two, one.length(), two.length());
    }

    int getLevenshLen(int[][] matrix, String one, String two, int i, int j) {
        if (matrix[i][j] != -1) {
            return matrix[i][j];
        }

        if (i == 0) {
            matrix[i][j] = j;
            return j;
        }
        if (j == 0) {
            matrix[i][j] = i;
            return i;
        }

        int delete = getLevenshLen(matrix, one, two, i-1, j) + 1;
        int insert = getLevenshLen(matrix, one, two, i, j-1) + 1;

        int cost = (one.charAt(i-1) == two.charAt(j-1)) ? 0 : 1;
        int replace = getLevenshLen(matrix, one, two, i-1, j-1) + cost;

        // Выбираем минимальное из трех значений
        matrix[i][j] = Math.min(Math.min(delete, insert), replace);

        return matrix[i][j];
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
