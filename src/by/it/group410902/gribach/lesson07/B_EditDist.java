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
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length(); // Длина первой строки
        int m = two.length(); // Длина второй строки

        // Создаём таблицу DP, где dp[i][j] — минимальное число операций для первых i символов строки one и j символов строки two
        int[][] dp = new int[n + 1][m + 1];

        // Базовые случаи: преобразование строки в пустую — i удалений или j вставок
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i; // i удалений, чтобы превратить первые i символов в пустую строку
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j; // j вставок, чтобы из пустой строки получить первые j символов
        }

        // Заполняем таблицу DP по строкам
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Если текущие символы совпадают — стоимость замены 0, иначе — 1
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // Три возможные операции:
                int delete = dp[i - 1][j] + 1;         // Удаление символа из строки one
                int insert = dp[i][j - 1] + 1;         // Вставка символа в строку one
                int replace = dp[i - 1][j - 1] + cost; // Замена символа

                // Выбираем минимальное количество операций из трёх
                dp[i][j] = Math.min(delete, Math.min(insert, replace));
            }
        }

        // Ответ — минимальное число операций для преобразования всей строки one в строку two
        return dp[n][m];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}