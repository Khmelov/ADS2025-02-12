package by.it.group410902.gavlev.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


        String result = "";

        int[][] matrix = new int[one.length() + 1][two.length() + 1];

        int len1 = one.length();
        int len2 = two.length();

        for (int i = 0; i <= len1; i++) {
            matrix[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            matrix[0][j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (one.charAt(i-1) == two.charAt(j-1)) ? 0 : 1;

                matrix[i][j] = Math.min(Math.min(matrix[i-1][j] + 1, matrix[i][j-1] + 1), matrix[i-1][j-1] + cost);
            }
        }

        List<String> operations = new ArrayList<>();
        int i = len1, j = len2;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                if (matrix[i][j] == matrix[i - 1][j - 1] + cost) {
                    if (cost == 0) {
                        operations.add("#");
                    } else {
                        operations.add("~" + two.charAt(j - 1));
                    }
                    i--;
                    j--;
                    continue;
                }
            }
            if (i > 0 && matrix[i][j] == matrix[i - 1][j] + 1) {
                operations.add("-" + one.charAt(i - 1));
                i--;
            } else if (j > 0 && matrix[i][j] == matrix[i][j - 1] + 1) {
                operations.add("+" + two.charAt(j - 1));
                j--;
            }
        }

        Collections.reverse(operations);
        result = String.join(",", operations) + ",";

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }




    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}