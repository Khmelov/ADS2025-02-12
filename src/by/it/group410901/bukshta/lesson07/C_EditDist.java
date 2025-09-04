package by.it.group410901.bukshta.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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
        // Получаем длины строк
        int m = one.length();
        int n = two.length();

        // Создаем таблицу dp для вычисления расстояния Левенштейна
        int[][] dp = new int[m + 1][n + 1];

        // Инициализируем первую строку и первый столбец
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Удаление i символов из первой строки
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Вставка j символов во вторую строку
        }

        // Заполняем таблицу dp
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Если символы совпадают, копируем значение без изменений
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Иначе выбираем минимум из трех операций
                    dp[i][j] = 1 + Math.min(
                            Math.min(dp[i - 1][j - 1], // замена
                                    dp[i][j - 1]),      // вставка
                            dp[i - 1][j]                // удаление
                    );
                }
            }
        }

        // Восстанавливаем последовательность операций
        ArrayList<String> operations = new ArrayList<>();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i == 0) {
                // Вставка символа из второй строки
                operations.add("+" + two.charAt(j - 1));
                j--;
            } else if (j == 0) {
                // Удаление символа из первой строки
                operations.add("-" + one.charAt(i - 1));
                i--;
            } else if (one.charAt(i - 1) == two.charAt(j - 1)) {
                // Совпадение символов
                operations.add("#");
                i--;
                j--;
            } else {
                // Выбираем операцию, которая привела к dp[i][j]
                int min = Math.min(
                        Math.min(dp[i - 1][j - 1], dp[i][j - 1]),
                        dp[i - 1][j]
                );
                if (min == dp[i - 1][j - 1]) {
                    // Замена
                    operations.add("~" + two.charAt(j - 1));
                    i--;
                    j--;
                } else if (min == dp[i - 1][j]) {
                    // Удаление
                    operations.add("-" + one.charAt(i - 1));
                    i--;
                } else {
                    // Вставка
                    operations.add("+" + two.charAt(j - 1));
                    j--;
                }
            }
        }

        // Формируем строку результата, переворачивая операции
        StringBuilder result = new StringBuilder();
        for (int k = operations.size() - 1; k >= 0; k--) {
            result.append(operations.get(k)).append(",");
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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