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

    // Метод для итерационного вычисления расстояния Левенштейна
    int getDistanceEdinting(String one, String two) {
        // Получаем длины строк
        int m = one.length();
        int n = two.length();

        // Создаем таблицу размером (m+1) x (n+1) для хранения промежуточных расстояний
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация первой строки и первого столбца таблицы
        // Если одна из строк пустая, расстояние — длина другой строки
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // удаление i символов
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // вставка j символов
        }

        // Заполняем таблицу динамического программирования
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если символы совпадают, стоимость равна предыдущему значению
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Иначе, берем минимум из трех операций: вставка, удаление, замена
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j],     // удаление
                            Math.min(
                                    dp[i][j - 1], // вставка
                                    dp[i - 1][j - 1] // замена
                            )
                    );
                }
            }
        }

        // Результат — в правом нижнем углу таблицы
        return dp[m][n];
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
