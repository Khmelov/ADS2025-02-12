package by.it.group451004.kahnovich.lesson01.lesson07;

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
        // Метод вычисляет расстояние Левенштейна — минимальное количество операций
        // вставки, удаления и замены символов, необходимых для преобразования строки one в строку two

        int result = 0;
        int n = one.length(), m = two.length();

        // Создаем двумерный массив dp размером (n+1) x (m+1),
        // где dp[i][j] будет хранить минимальное расстояние между
        // первыми i символами строки one и первыми j символами строки two
        int[][] dp = new int[n + 1][m + 1];

        // Заполняем первый ряд — расстояния от пустой строки one до первых i символов two
        // Для этого нужно i операций вставки
        for (int i = 0; i <= m; i++) {
            dp[0][i] = i;
        }

        // Заполняем первый столбец — расстояния от первых j символов one до пустой строки two
        // Для этого нужно j операций удаления
        for (int j = 0; j <= n; j++) {
            dp[j][0] = j;
        }

        // Проходим по всему массиву dp, начиная с i=1 и j=1
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int diff = 1; // Стоимость замены символа по умолчанию — 1

                // Если символы совпадают, замена не нужна — стоимость 0
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    diff = 0;
                }

                // Выбираем минимальное из трех вариантов:
                // 1) Удаление символа из one: dp[i-1][j] + 1
                // 2) Вставка символа в one: dp[i][j-1] + 1
                // 3) Замена символа (или отсутствие замены, если символы совпадают): dp[i-1][j-1] + diff
                dp[i][j] = Math.min(
                        dp[i - 1][j] + 1,
                        Math.min(
                                dp[i][j - 1] + 1,
                                dp[i - 1][j - 1] + diff
                        )
                );
            }
        }

        // Итоговое расстояние Левенштейна для строк one и two хранится в dp[n][m]
        result = dp[n][m];

        return result;
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