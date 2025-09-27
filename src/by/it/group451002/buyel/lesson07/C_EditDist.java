package by.it.group451002.buyel.lesson07;

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

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int[][] matrix = new int[one.length() + 1][two.length() + 1];
        StringBuilder result = new StringBuilder();

        matrix[0][0] = 0;

        // 0-ой столбец матрицы от 1 до one.length()
        for (int j = 1; j <= two.length(); j++) {
            matrix[0][j] = matrix[0][j-1] + 1;
        }

        // 0-ая строка матрицы от 1 до two.length()
        for (int i = 1; i <= one.length(); i++) {
            matrix[i][0] = matrix[i-1][0] + 1;

            // i - индекс строки
            // j - индекс столбца
            // Минимальное значение из ([i-1] + 1), ([j-1] + 1) и ([i-1, j-1] + (1 или 0)) присваиваем к [i, j]
            for (int j = 1; j <= two.length(); j++) {
                int add = (one.charAt(i - 1) != two.charAt(j - 1)) ? 1 : 0;
                matrix[i][j] = Math.min(
                        Math.min(matrix[i - 1][j] + 1, matrix[i][j - 1] + 1),
                        matrix[i - 1][j - 1] + add);
            }
        }

        // Идём "с конца" матрицы к точке [0; 0] для вывода
        int i = one.length(), j = two.length();
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && matrix[i][j] == matrix[i - 1][j - 1] + (one.charAt(i - 1) != two.charAt(j - 1) ? 1 : 0)) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    result.insert(0, "#,");
                } else {
                    result.insert(0, "~" + two.charAt(j - 1) + ",");
                }
                i--;
                j--;
            } else if (i > 0 && matrix[i][j] == matrix[i - 1][j] + 1) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            } else {
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result.toString();
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