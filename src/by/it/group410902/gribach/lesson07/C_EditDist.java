package by.it.group410902.gribach.lesson07;

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
        int n = one.length(); // длина первой строки
        int m = two.length(); // длина второй строки

        // dp[i][j] — минимальное число операций для преобразования первых i символов строки one в первые j символов строки two
        int[][] dp = new int[n + 1][m + 1];

        // op[i][j] — операция, приведшая к состоянию dp[i][j]:
        // 'D' — удаление, 'I' — вставка, 'R' — замена, 'M' — совпадение (match)
        char[][] op = new char[n + 1][m + 1];

        // arg[i][j] — символ, связанный с операцией (например, символ, который вставляем или заменяем)
        char[][] arg = new char[n + 1][m + 1];

        // Инициализация первой колонки:
        // Чтобы преобразовать первые i символов строки one в пустую строку, нужно i удалений
        for (int i = 1; i <= n; i++) {
            dp[i][0] = i;
            op[i][0] = 'D';            // операция — удаление
            arg[i][0] = one.charAt(i - 1); // символ, который удаляем
        }

        // Инициализация первой строки:
        // Чтобы преобразовать пустую строку в первые j символов строки two, нужно j вставок
        for (int j = 1; j <= m; j++) {
            dp[0][j] = j;
            op[0][j] = 'I';            // операция — вставка
            arg[0][j] = two.charAt(j - 1); // символ, который вставляем
        }

        // Заполнение таблиц dp, op и arg
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Начинаем с варианта удаления символа one[i-1]
                int best = dp[i - 1][j] + 1;
                char bestOp = 'D';
                char bestArg = one.charAt(i - 1);

                // Вариант вставки символа two[j-1]
                if (dp[i][j - 1] + 1 < best) {
                    best = dp[i][j - 1] + 1;
                    bestOp = 'I';
                    bestArg = two.charAt(j - 1);
                }

                // Вариант замены или совпадения символов
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                if (dp[i - 1][j - 1] + cost < best) {
                    best = dp[i - 1][j - 1] + cost;
                    bestOp = (cost == 0) ? 'M' : 'R'; // M — совпадение, R — замена
                    bestArg = two.charAt(j - 1);
                }

                dp[i][j] = best;  // минимальное число операций
                op[i][j] = bestOp; // операция, которую мы выбрали
                arg[i][j] = bestArg; // символ, связанный с операцией
            }
        }

        // Восстановление пути преобразования (последовательности операций) с конца в начало
        StringBuilder sb = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            switch (op[i][j]) {
                case 'D': // удаление символа
                    sb.append(",").append(arg[i][j]).append("-");
                    i--;
                    break;
                case 'I': // вставка символа
                    sb.append(",").append(arg[i][j]).append("+");
                    j--;
                    break;
                case 'R': // замена символа
                    sb.append(",").append(arg[i][j]).append("~");
                    i--;
                    j--;
                    break;
                case 'M': // совпадение символов (ничего не меняем)
                    sb.append(",#");
                    i--;
                    j--;
                    break;
            }
        }
        // Переворачиваем строку с операциями, чтобы получить порядок от начала к концу
        sb = sb.reverse();

        return sb.toString();
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