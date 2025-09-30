package by.it.group410901.galitskiy.lesson07;

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
        int n = one.length(); // длина первой строки
        int m = two.length(); // длина второй строки

        // Матрица для хранения расстояний
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация: первая строка и столбец — только вставки/удаления
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        // Заполнение матрицы расстояний
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // Берем минимум из: удаление, вставка, замена/копия
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,      // удаление
                                dp[i][j - 1] + 1),     // вставка
                        dp[i - 1][j - 1] + cost         // замена или копирование
                );
            }
        }

        // Восстановление пути — какие операции были применены
        StringBuilder result = new StringBuilder();
        int i = n;
        int j = m;

        while (i > 0 || j > 0) {
            // Копирование (если символы равны)
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                result.insert(0, "#,");
                i--;
                j--;
            }
            // Замена
            else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                result.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            }
            // Вставка
            else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
            // Удаление
            else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // ab ab → #,#,
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // short ports → -s,~p,#,#,#,+s,
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // distance editing → +e,#,#,-s,#,~i,#,-c,~g,
    }
}