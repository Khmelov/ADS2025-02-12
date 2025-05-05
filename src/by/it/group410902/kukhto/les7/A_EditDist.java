package by.it.group410902.kukhto.les7;

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

    private int[][] memo;
    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        memo = new int[one.length() + 1][two.length() + 1];


        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                memo[i][j] = -1;
            }
        }

        return calculateDistance(one, two, one.length(), two.length());


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    }
    private int calculateDistance(String s1, String s2, int i, int j) {
        // Если значение уже вычислено, возвращаем его
        if (memo[i][j] != -1) {
            return memo[i][j];
        }


        // Если первая строка пустая, нужно вставить все символы второй строки
        if (i == 0) {
            memo[i][j] = j;
            return j;
        }
        // Если вторая строка пустая, нужно удалить все символы первой строки
        if (j == 0) {
            memo[i][j] = i;
            return i;
        }

        // Если последние символы совпадают, переходим к следующим
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            memo[i][j] = calculateDistance(s1, s2, i - 1, j - 1);
            return memo[i][j];
        }


        int insert = calculateDistance(s1, s2, i, j - 1) + 1;
        int delete = calculateDistance(s1, s2, i - 1, j) + 1;
        int replace = calculateDistance(s1, s2, i - 1, j - 1) + 1;

        memo[i][j] = Math.min(Math.min(insert, delete), replace);
        return memo[i][j];
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
