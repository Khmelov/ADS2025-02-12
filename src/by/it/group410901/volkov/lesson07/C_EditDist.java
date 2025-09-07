package by.it.group410901.volkov.lesson07;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Получаем длины строк
        int m = one.length();
        int n = two.length();

        // Создаем матрицу для хранения расстояний
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация базовых случаев
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Удаление всех символов первой строки
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Вставка всех символов второй строки
        }

        // Заполнение матрицы расстояний
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // Совпадение символов
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }

        // Восстановление редакционных предписаний
        StringBuilder operations = new StringBuilder();
        int i = m, j = n;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                operations.insert(0, "#,"); // Совпадение символов
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                operations.insert(0, "-" + one.charAt(i - 1) + ","); // Удаление
                i--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                operations.insert(0, "+" + two.charAt(j - 1) + ","); // Вставка
                j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                operations.insert(0, "~" + two.charAt(j - 1) + ","); // Замена
                i--;
                j--;
            }
        }

        String result = operations.toString();
        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Создаем поток для чтения данных из файла
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        // Создаем экземпляр класса
        C_EditDist instance = new C_EditDist();
        // Инициализируем сканер для чтения данных
        Scanner scanner = new Scanner(stream);

        // Читаем и выводим результаты для трех пар строк
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}