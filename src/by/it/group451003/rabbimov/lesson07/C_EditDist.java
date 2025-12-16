package by.it.group451003.rabbimov.lesson07;

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

    static int Minimum(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }
    String LevenshteinDistance(String str1, String str2) {
        int n = str1.length() + 1;
        int m = str2.length() + 1;
        int[][] matrixD = new int[n][m];

        for (int i = 0; i < n; i++) {
            matrixD[i][0] = i;
        }

        for (int j = 0; j < m; j++) {
            matrixD[0][j] = j;
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    matrixD[i][j] = matrixD[i - 1][j - 1];
                } else {
                    int deletion = matrixD[i - 1][j] + 1;
                    int insertion = matrixD[i][j - 1] + 1;
                    int substitution = matrixD[i - 1][j - 1] + 1;
                    matrixD[i][j] = Minimum(deletion, insertion, substitution);
                }
            }
        }

        StringBuilder actionsBuilder = new StringBuilder();
        int i = n - 1;
        int j = m - 1;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && str1.charAt(i - 1) == str2.charAt(j - 1)) {
                actionsBuilder.insert(0, "#,");
                i--;
                j--;
            } else if (i > 0 && matrixD[i][j] == matrixD[i - 1][j] + 1) {
                actionsBuilder.insert(0, "-").insert(0, str1.charAt(i - 1)).insert(0, ",");
                i--;
            } else if (j > 0 && matrixD[i][j] == matrixD[i][j - 1] + 1) {
                actionsBuilder.insert(0, "+").insert(0, str2.charAt(j - 1)).insert(0, ",");
                j--;
            } else if (i > 0 && j > 0 && matrixD[i][j] == matrixD[i - 1][j - 1] + 1) {
                actionsBuilder.insert(0, "~").insert(0, str2.charAt(j - 1)).insert(0, ",");
                i--;
                j--;
            }
        }

        return actionsBuilder.toString();
    }

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


        String result = LevenshteinDistance(one, two);
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