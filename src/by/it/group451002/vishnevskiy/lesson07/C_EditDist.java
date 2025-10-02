package by.it.group451002.vishnevskiy.lesson07;

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
    Sample Output 3:
    +e,#,#,-s,#,~i,#,-c,~g,
*/

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        // Создаем таблицу расстояний
        int[][] dp = new int[m + 1][n + 1];

        // Заполняем базовые случаи
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        // Основной цикл для заполнения таблицы расстояний
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // символы равны, копирование
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j], // удаление
                            Math.min(
                                    dp[i][j - 1], // вставка
                                    dp[i - 1][j - 1] // замена
                            )
                    );
                }
            }
        }

        // Восстанавливаем путь (редакционное предписание)
        StringBuilder result = new StringBuilder();
        int i = m;
        int j = n;

        while (i > 0 || j > 0) {
            // Копирование
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
            // Удаление
            else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
            // Вставка
            else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
