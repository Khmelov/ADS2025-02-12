package by.it.group451004.belkovich.lesson07;

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
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEditing(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEditing(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEditing(scanner.nextLine(), scanner.nextLine()));
    }

    int getDistanceEditing(String one, String two) {
        str1 = one;
        str2 = two;
        distances = new int[str1.length() + 1][str2.length() + 1];
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[0].length; j++) {
                distances[i][j] = -1;
            }
        }
        for (int i = 0; i < distances.length; i++) {
            distances[i][0] = i;
        }
        for (int j = 0; j < distances[0].length; j++) {
            distances[0][j] = j;
        }
        return recursion(str1.length(), str2.length());
    }

    int recursion(int i, int j) {
        if (distances[i][j] != -1) return distances[i][j];

        distances[i][j] = getMin(recursion(i, j - 1) + 1, recursion(i - 1, j) + 1, recursion(i - 1, j - 1) + M(i, j));
        return distances[i][j];
    }

    int getMin(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    int M(int i, int j) {
        if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
            return 0;
        } else {
            return 1;
        }
    }

    static private int distances[][];
    static private String str1 = new String("");
    static private String str2 = new String("");

}
