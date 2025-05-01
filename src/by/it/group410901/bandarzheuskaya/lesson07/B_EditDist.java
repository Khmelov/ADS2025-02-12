package by.it.group410901.bandarzheuskaya.lesson07;

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
        int n = one.length();
        int m = two.length();

        // Создаём таблицу dp размером (n+1) x (m+1)
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация: если одна из строк пустая
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        // Заполняем таблицу dp
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Если символы равны, не нужно делать операцию
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Выбираем минимальное из трёх операций: вставка, удаление, замена
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j],        // Удаление
                            Math.min(
                                    dp[i][j - 1],    // Вставка
                                    dp[i - 1][j - 1] // Замена
                            )
                    );
                }
            }
        }

        // Возвращаем минимальное количество операций для преобразования обеих строк
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