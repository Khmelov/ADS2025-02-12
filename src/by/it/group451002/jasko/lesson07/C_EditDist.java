package by.it.group451002.jasko.lesson07;

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

    // Метод вычисления расстояния Левенштейна и восстановления операций редактирования
    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Получаем длины строк
        int n = one.length(); // Длина первой строки
        int m = two.length(); // Длина второй строки

        // Создаем матрицу dp размером (n+1) x (m+1)
        // dp[i][j] будет содержать расстояние между подстроками one[0..i-1] и two[0..j-1]
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация базовых случаев:

        // Если вторая строка пустая, нужно удалить все символы первой строки
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i; // i операций удаления
        }

        // Если первая строка пустая, нужно вставить все символы второй строки
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j; // j операций вставки
        }

        // Заполняем матрицу dp:
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Проверяем, совпадают ли текущие символы
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Если символы совпадают, стоимость не увеличивается
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Если символы разные, выбираем минимальную стоимость из трех операций:
                    dp[i][j] = Math.min(dp[i - 1][j - 1], // Замена
                            Math.min(dp[i - 1][j],        // Удаление
                                    dp[i][j - 1])) + 1;   // Вставка
                }
            }
        }
        //   0 к и т
        // 0 0 1 2 3
        // к 1 0 1 2
        // о 2 1 1 2
        // т 3 2 2 1

        // Восстанавливаем последовательность операций
        StringBuilder result = new StringBuilder();
        int i = n, j = m;

        // Идем от конца строк к началу
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                // Символы совпадают - операция копирования
                result.insert(0, "#,");
                i--;
                j--;
            }
            else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                // Операция вставки символа из второй строки
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
            else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                // Операция удаления символа из первой строки
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
            else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                // Операция замены символа
                result.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входных данных из файла
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        assert stream != null;
        Scanner scanner = new Scanner(stream);

        // Вычисление и вывод последовательности операций для трех пар строк
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}